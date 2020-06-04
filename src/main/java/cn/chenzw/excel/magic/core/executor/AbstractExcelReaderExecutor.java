package cn.chenzw.excel.magic.core.executor;

import cn.chenzw.excel.magic.core.analysis.XlsxAnalysisHandler;
import cn.chenzw.excel.magic.core.context.ExcelReaderContext;
import cn.chenzw.excel.magic.core.exception.ExcelException;
import cn.chenzw.excel.magic.core.lifecycle.ExcelReaderLifecycle;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelImportColumn;
import cn.chenzw.excel.magic.core.meta.annotation.converter.ExcelConverter;
import cn.chenzw.excel.magic.core.meta.annotation.validation.ConstraintValidator;
import cn.chenzw.excel.magic.core.meta.model.ExcelCellDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelSheetDefinition;
import cn.chenzw.excel.magic.core.processor.ExcelPerRowProcessor;
import cn.chenzw.excel.magic.core.support.converter.AbstractExcelColumnConverter;
import cn.chenzw.excel.magic.core.support.converter.ExcelDefaultConverter;
import cn.chenzw.excel.magic.core.support.validator.AbstractExcelColumnValidator;
import cn.chenzw.excel.magic.core.support.validator.ExcelDefaultValidator;
import cn.chenzw.excel.magic.core.util.ExcelFieldUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author chenzw
 */
public abstract class AbstractExcelReaderExecutor<T> implements ExcelReaderLifecycle, ExcelExecutor {

    protected int curSheetIndex = 0;
    protected int curRowIndex;
    protected int curColIndex;
    protected int totalRows;

    protected ExcelReaderContext readerContext;
    protected ExcelSheetDefinition curSheet;

    private List<T> datas = new ArrayList<>();
    private Cache<String, List<AbstractExcelColumnValidator>> columnValidatorCache;
    private Cache<String, List<AbstractExcelColumnConverter>> columnConverterCache;


    public AbstractExcelReaderExecutor(final ExcelReaderContext readerContext) {
        this.readerContext = readerContext;
        this.columnValidatorCache = CacheBuilder.newBuilder().build();
        this.columnConverterCache = CacheBuilder.newBuilder().build();
    }

    protected abstract ExcelPerRowProcessor getExcelRowProcess();


    @Override
    public List<T> executeRead() {
        final Map<Integer, ExcelSheetDefinition> sheetDefinitions = readerContext.getSheetDefinitions();
        // 延迟解析比率
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (final OPCPackage pkg = OPCPackage.open(readerContext.getInputStream())) {
            final XSSFReader xssfReader = new XSSFReader(pkg);
            final XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
            final ContentHandler xlsxAnalysisHandler = new XlsxAnalysisHandler(xssfReader.getStylesTable(),
                    xssfReader.getSharedStringsTable(), getExcelRowProcess(), readerContext.getExcelRowReadExceptionCallback());
            parser.setContentHandler(xlsxAnalysisHandler);

            final Iterator<InputStream> sheets = xssfReader.getSheetsData();
            while (sheets.hasNext()) {
                final InputStream sheet = sheets.next();

              /*  InputStream copy = cn.chenzw.toolkit.io.IOExtUtils.copy(sheet);
                System.out.println(org.apache.commons.io.IOUtils.toString(copy));*/

                if (sheetDefinitions.containsKey(this.curSheetIndex + 1)) {
                    this.curSheetIndex++;
                    final InputSource sheetSource = new InputSource(sheet);
                    parser.parse(sheetSource);
                }
                sheet.close();
            }
            return datas;
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final OpenXML4JException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Workbook executeWrite() {
        throw new UnsupportedOperationException("不支持调用此方法！");
    }

    @Override
    public boolean isEmptyRow(final ExcelRowDefinition row) {
        final List<ExcelCellDefinition> excelCells = row.getExcelCells();
        for (final ExcelCellDefinition excelCell : excelCells) {
            if (!StringUtils.isBlank(excelCell.getCellValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void preSet(final ExcelRowDefinition row) {
        final Map<Integer, String> columnTitles = this.curSheet.getColumnTitles();
        final List<ExcelCellDefinition> excelCells = row.getExcelCells();

        for (final ExcelCellDefinition excelCell : excelCells) {
            final String title = columnTitles.get(excelCell.getColIndex());
            excelCell.setColTitle(title);
            excelCell.setSheetIndex(this.curSheetIndex);
        }
    }

    @Override
    public boolean validate(final ExcelRowDefinition row) {
        final List<ExcelCellDefinition> excelCells = row.getExcelCells();
        final Map<Integer, Field> columnFields = this.curSheet.getColumnFields();
        boolean allPassed = true;
        for (final Map.Entry<Integer, Field> columnFieldEntity : columnFields.entrySet()) {
            this.curColIndex = columnFieldEntity.getKey();
            final Field field = columnFieldEntity.getValue();
            final ExcelCellDefinition cell = getCell(excelCells, columnFieldEntity.getKey());
            try {
                columnVaildate(cell, field);
            } catch (final Exception ex) {
                allPassed = false;
                readerContext.getExcelCellReadExceptionCallback().call(row, cell, ex);
            }
        }
        return allPassed;
    }

    private boolean columnVaildate(final ExcelCellDefinition cell, final Field field) {
        // 空值
        if (StringUtils.isBlank(cell.getCellValue())) {
            // 非空校验
            final ExcelImportColumn importColumn = field.getAnnotation(ExcelImportColumn.class);
            if (!importColumn.allowBlank()) {
                throw new ExcelException("该字段值为空!");
            }
        }

        List<AbstractExcelColumnValidator> columnValidators = columnValidatorCache.getIfPresent(field.getName());
        if (columnValidators == null) {
            columnValidators = findColumnValidators(field);
            this.columnValidatorCache.put(field.getName(), columnValidators);
        }

        for (final AbstractExcelColumnValidator columnValidator : columnValidators) {
            if (!columnValidator.validate(cell.getCellValue())) {
                throw new ExcelException("该字段数据校验不通过!");
            }
        }
        return true;
    }

    @Override
    public void format(final ExcelRowDefinition row) {
        final Map<Integer, Field> columnFields = this.curSheet.getColumnFields();
        final List<ExcelCellDefinition> excelCells = row.getExcelCells();
        boolean allPassed = true;
        Object instance = null;
        try {
            instance = this.curSheet.getBindingModel().newInstance();
        } catch (final InstantiationException e) {
            throw new ExcelException("实例化对象" + this.curSheet.getBindingModel() + "失败!");
        } catch (final IllegalAccessException e) {
            throw new ExcelException("实例化对象" + this.curSheet.getBindingModel() + "失败!");
        }

        for (int i = 0; i < excelCells.size(); i++) {
            this.curColIndex = i + 1;
            final ExcelCellDefinition cell = excelCells.get(i);

            if (!StringUtils.isBlank(cell.getCellValue())) {
                final Field field = columnFields.get(cell.getColIndex());

                Object cellValue = cell.getCellValue();
                // 值转换
                List<AbstractExcelColumnConverter> columnConverters = this.columnConverterCache.getIfPresent(field.getName());
                if (columnConverters == null) {
                    columnConverters = findColumnConverter(field);
                    this.columnConverterCache.put(field.getName(), columnConverters);
                }

                try {
                    for (final AbstractExcelColumnConverter columnConverter : columnConverters) {
                        cellValue = columnConverter.convert((String) cellValue);
                    }
                } catch (final Exception ex) {
                    allPassed = false;
                    readerContext.getExcelCellReadExceptionCallback().call(row, cell, ex);
                    break;
                }

                try {
                    ExcelFieldUtils.setFieldValue(field, instance, cellValue,
                            field.getAnnotation(ExcelImportColumn.class).dateFormat());
                } catch (final Exception ex) {
                    allPassed = false;
                    readerContext.getExcelCellReadExceptionCallback().call(row, cell, new ExcelException("字段赋值失败!", ex));
                    break;
                }
            }
        }

        // 只有全部字段都解析成功，才将数据添加到返回的列表中
        if (allPassed) {
            datas.add((T) instance);
        }
    }

    /**
     * 是否标题行
     *
     * @param row
     * @return
     */
    protected boolean isTitleRow(final ExcelRowDefinition row) {
        return row.getRowIndex() < this.curSheet.getFirstDataRow();
    }

    private ExcelCellDefinition getCell(final List<ExcelCellDefinition> excelCells, final int colIndex) {
        for (final ExcelCellDefinition excelCell : excelCells) {
            if (excelCell.getColIndex() == colIndex) {
                return excelCell;
            }
        }
        final ExcelCellDefinition cloneCellDefinition = cloneExcelCellDefinition(excelCells.get(0));
        cloneCellDefinition.setColIndex(colIndex);
        return cloneCellDefinition;
    }

    private ExcelCellDefinition cloneExcelCellDefinition(final ExcelCellDefinition cellDefinition) {
        final ExcelCellDefinition _cellDefinition = new ExcelCellDefinition();
        _cellDefinition.setSheetIndex(cellDefinition.getSheetIndex());
        _cellDefinition.setColIndex(cellDefinition.getColIndex());
        _cellDefinition.setRowIndex(cellDefinition.getRowIndex());
        return _cellDefinition;
    }

    private List<AbstractExcelColumnValidator> findColumnValidators(final Field field) {
        List<AbstractExcelColumnValidator> columnValidators = new ArrayList<>();
        final Annotation[] annotations = field.getAnnotations();
        for (final Annotation annotation : annotations) {
            final Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.isAnnotationPresent(ConstraintValidator.class)) {
                final ConstraintValidator constraintValidator = aClass.getAnnotation(ConstraintValidator.class);
                try {
                    final AbstractExcelColumnValidator columnValidator = constraintValidator.validator().newInstance();
                    columnValidator.initialize(annotation);
                    columnValidators.add(columnValidator);
                } catch (final InstantiationException | IllegalAccessException e) {
                    throw new ExcelException("实例化校验器[" + constraintValidator.validator() + "]时失败!", e);
                }
            }
        }

        if (columnValidators.size() == 0) {
            columnValidators = Collections.singletonList((AbstractExcelColumnValidator) new ExcelDefaultValidator());
        }
        return columnValidators;
    }

    private List<AbstractExcelColumnConverter> findColumnConverter(final Field field) {
        List<AbstractExcelColumnConverter> columnConverters = new ArrayList<>();
        final Annotation[] annotations = field.getAnnotations();
        for (final Annotation annotation : annotations) {
            final Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.isAnnotationPresent(ExcelConverter.class)) {
                final ExcelConverter excelConverter = aClass.getAnnotation(ExcelConverter.class);
                try {
                    final AbstractExcelColumnConverter columnConverter = excelConverter.convertBy().newInstance();
                    columnConverter.initialize(annotation);
                    columnConverters.add(columnConverter);
                } catch (final InstantiationException | IllegalAccessException e) {
                    throw new ExcelException("实例化转换器[" + excelConverter.convertBy() + "]时失败!", e);
                }
            }
        }

        if (columnConverters.size() == 0) {
            columnConverters = Collections.singletonList((AbstractExcelColumnConverter) new ExcelDefaultConverter());
        }

        return columnConverters;
    }
}
