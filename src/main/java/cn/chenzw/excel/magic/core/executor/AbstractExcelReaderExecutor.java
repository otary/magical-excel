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
import java.text.ParseException;
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

    // private XSSFReader xssfReader;
    private List<T> datas = new ArrayList<>();
    private Cache<String, List<AbstractExcelColumnValidator>> columnValidatorCache;
    private Cache<String, List<AbstractExcelColumnConverter>> columnConverterCache;


    public AbstractExcelReaderExecutor(ExcelReaderContext readerContext) {
        this.readerContext = readerContext;
        this.columnValidatorCache = CacheBuilder.newBuilder().build();
        this.columnConverterCache = CacheBuilder.newBuilder().build();
    }


    protected abstract ExcelPerRowProcessor getExcelRowProcess();


    @Override
    public List<T> executeRead() {
        /*Map<Integer, ExcelSheetDefinition> sheetdefinitions = readerContext.getSheetDefinitions();
        // 延迟解析比率
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (OPCPackage pkg = OPCPackage.open(readerContext.getInputStream())) {
            this.xssfReader = new XSSFReader(pkg);
            XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
            ContentHandler xlsxAnalysisHandler = new XlsxAnalysisHandler(xssfReader.getStylesTable(),
                    xssfReader.getSharedStringsTable(), getExcelRowProcess());
            parser.setContentHandler(xlsxAnalysisHandler);
            for (Map.Entry<Integer, ExcelSheetDefinition> sheetDefinitionEntry : sheetdefinitions.entrySet()) {
                this.curSheetIndex = sheetDefinitionEntry.getKey();
                InputStream sheet = this.xssfReader.getSheet(ExcelConstants.SHEET_PRFIX + this.curSheetIndex);
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();
            }
            return datas;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        }*/

        Map<Integer, ExcelSheetDefinition> sheetdefinitions = readerContext.getSheetDefinitions();
        // 延迟解析比率
        ZipSecureFile.setMinInflateRatio(-1.0d);
        try (OPCPackage pkg = OPCPackage.open(readerContext.getInputStream())) {
            XSSFReader xssfReader = new XSSFReader(pkg);
            XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
            ContentHandler xlsxAnalysisHandler = new XlsxAnalysisHandler(xssfReader.getStylesTable(),
                    xssfReader.getSharedStringsTable(), getExcelRowProcess());
            parser.setContentHandler(xlsxAnalysisHandler);

            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            while (sheets.hasNext()) {
                InputStream sheet = sheets.next();

             /*   InputStream copy = cn.chenzw.toolkit.io.IOExtUtils.copy(sheet);
                System.out.println(org.apache.commons.io.IOUtils.toString(copy));*/

                if (sheetdefinitions.containsKey(this.curSheetIndex + 1)) {
                    this.curSheetIndex++;
                    InputSource sheetSource = new InputSource(sheet);
                    parser.parse(sheetSource);
                }
                sheet.close();
            }


            /*for (Map.Entry<Integer, ExcelSheetDefinition> sheetDefinitionEntry : sheetdefinitions.entrySet()) {
                this.curSheetIndex = sheetDefinitionEntry.getKey();
                InputStream sheet = this.xssfReader.getSheet(ExcelConstants.SHEET_PRFIX + this.curSheetIndex);
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();
            }*/
            return datas;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Workbook executeWrite() {
        throw new UnsupportedOperationException("不支持调用此方法！");
    }

    @Override
    public boolean isEmptyRow(ExcelRowDefinition row) {
        List<ExcelCellDefinition> excelCells = row.getExcelCells();
        for (ExcelCellDefinition excelCell : excelCells) {
            if (!StringUtils.isBlank(excelCell.getCellValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validate(ExcelRowDefinition row) {
        List<ExcelCellDefinition> excelCells = row.getExcelCells();
        Map<Integer, Field> columnFields = this.curSheet.getColumnFields();
        for (Map.Entry<Integer, Field> columnFieldEntity : columnFields.entrySet()) {
            this.curColIndex = columnFieldEntity.getKey();
            Field field = columnFieldEntity.getValue();
            ExcelCellDefinition cell = getCell(excelCells, columnFieldEntity.getKey());

            // 空值
            if (StringUtils.isBlank(cell.getCellValue())) {
                // 非空校验
                ExcelImportColumn importColumn = field.getAnnotation(ExcelImportColumn.class);
                if (!importColumn.allowBlank()) {
                    throw new ExcelException("该字段值为空!");
                }
            }

            List<AbstractExcelColumnValidator> columnValidators = columnValidatorCache.getIfPresent(field.getName());
            if (columnValidators == null) {
                columnValidators = findColumnValidators(field);
                this.columnValidatorCache.put(field.getName(), columnValidators);
            }

            for (AbstractExcelColumnValidator columnValidator : columnValidators) {
                if (!columnValidator.validate(cell.getCellValue())) {
                    throw new ExcelException("该字段数据校验不通过!");
                }
            }
        }
        return true;
    }

    @Override
    public void format(ExcelRowDefinition row) {
        Map<Integer, Field> columnFields = this.curSheet.getColumnFields();
        List<ExcelCellDefinition> excelCells = row.getExcelCells();
        Object instance = null;
        try {
            instance = this.curSheet.getBindingModel().newInstance();
        } catch (InstantiationException e) {
            throw new ExcelException("实例化对象" + this.curSheet.getBindingModel() + "失败!");
        } catch (IllegalAccessException e) {
            throw new ExcelException("实例化对象" + this.curSheet.getBindingModel() + "失败!");
        }

        for (int i = 0; i < excelCells.size(); i++) {
            this.curColIndex = i + 1;

            ExcelCellDefinition cell = excelCells.get(i);
            if (!StringUtils.isBlank(cell.getCellValue())) {
                Field field = columnFields.get(cell.getColIndex());

                Object cellValue = cell.getCellValue();
                // 值转换
                List<AbstractExcelColumnConverter> columnConverters = this.columnConverterCache.getIfPresent(field.getName());
                if (columnConverters == null) {
                    columnConverters = findColumnConverter(field);
                    this.columnConverterCache.put(field.getName(), columnConverters);
                }

                for (AbstractExcelColumnConverter columnConverter : columnConverters) {
                    cellValue = columnConverter.convert((String) cellValue);
                }

                try {
                    ExcelFieldUtils.setFieldValue(field, instance, cellValue,
                            field.getAnnotation(ExcelImportColumn.class).dateFormat());
                } catch (ParseException e) {
                    throw new ExcelException("字段值[ " + cellValue + " ]转换失败，字段类型: [" + cell.getCellType() + "]");
                } catch (IllegalAccessException e) {
                    throw new ExcelException("字段赋值失败!");
                }
            }
        }
        datas.add((T) instance);
    }

    /**
     * 是否标题行
     *
     * @param row
     * @return
     */
    protected boolean isTitleRow(ExcelRowDefinition row) {
        return row.getRowIndex() < this.curSheet.getFirstDataRow();
    }

    private ExcelCellDefinition getCell(List<ExcelCellDefinition> excelCells, int colIndex) {
        for (ExcelCellDefinition excelCell : excelCells) {
            if (excelCell.getColIndex() == colIndex) {
                return excelCell;
            }
        }
        return new ExcelCellDefinition();
    }

    private List<AbstractExcelColumnValidator> findColumnValidators(Field field) {
        List<AbstractExcelColumnValidator> columnValidators = new ArrayList<>();
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.isAnnotationPresent(ConstraintValidator.class)) {
                ConstraintValidator constraintValidator = aClass.getAnnotation(ConstraintValidator.class);
                try {
                    AbstractExcelColumnValidator columnValidator = constraintValidator.validator().newInstance();
                    columnValidator.initialize(annotation);
                    columnValidators.add(columnValidator);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new ExcelException("实例化校验器[" + constraintValidator.validator() + "]时失败!", e);
                }
            }
        }

        if (columnValidators.size() == 0) {
            columnValidators = Collections.singletonList((AbstractExcelColumnValidator) new ExcelDefaultValidator());
        }
        return columnValidators;
    }

    private List<AbstractExcelColumnConverter> findColumnConverter(Field field) {
        List<AbstractExcelColumnConverter> columnConverters = new ArrayList<>();
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.isAnnotationPresent(ExcelConverter.class)) {
                ExcelConverter excelConverter = aClass.getAnnotation(ExcelConverter.class);
                try {
                    AbstractExcelColumnConverter columnConverter = excelConverter.convertBy().newInstance();
                    columnConverter.initialize(annotation);
                    columnConverters.add(columnConverter);
                } catch (InstantiationException | IllegalAccessException e) {
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
