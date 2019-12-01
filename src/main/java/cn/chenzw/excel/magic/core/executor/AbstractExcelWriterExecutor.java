package cn.chenzw.excel.magic.core.executor;

import cn.chenzw.excel.magic.core.context.ExcelWriterContext;
import cn.chenzw.excel.magic.core.exception.ExcelException;
import cn.chenzw.excel.magic.core.exception.ExcelWriterException;
import cn.chenzw.excel.magic.core.lifecycle.ExcelWriterLifecycle;
import cn.chenzw.excel.magic.core.meta.annotation.CellRange;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelComplexHeader;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelDataFormat;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelExportColumn;
import cn.chenzw.excel.magic.core.meta.annotation.datavalidation.ExcelDataValidation;
import cn.chenzw.excel.magic.core.meta.model.ExcelCellStyleDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelSheetDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelWriterSheetDefinition;
import cn.chenzw.excel.magic.core.meta.style.CellStyleBuilder;
import cn.chenzw.excel.magic.core.support.dataconstraint.ExcelDataValidationConstraint;
import cn.chenzw.excel.magic.core.util.ExcelFieldUtils;
import cn.chenzw.excel.magic.core.util.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author chenzw
 */
public abstract class AbstractExcelWriterExecutor implements ExcelExecutor, ExcelWriterLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(AbstractExcelWriterExecutor.class);

    protected int curSheetIndex;
    protected int curRowIndex;
    protected int curColIndex;
    private ExcelWriterContext writerContext;
    private CellStyleCache cellStyleCache;
    private SXSSFWorkbook sxssfWorkbook;
    private Map<Integer, ExcelSheetDefinition> sheetDefinitions;

    public AbstractExcelWriterExecutor(ExcelWriterContext writerContext) {
        this.sxssfWorkbook = new SXSSFWorkbook(new XSSFWorkbook(), 1000);
        this.sxssfWorkbook.setCompressTempFiles(true);
        this.writerContext = writerContext;
        this.cellStyleCache = new CellStyleCache();
    }

    @Override
    public abstract void beforeCallback();

    @Override
    public void sheetPaging() {
        List<List<?>> sheetSegments = new ArrayList<>();
        for (Map.Entry<Integer, ExcelSheetDefinition> sheetDefinitionEntry : sheetDefinitions.entrySet()) {
            ExcelWriterSheetDefinition sheetDefinition = (ExcelWriterSheetDefinition) sheetDefinitionEntry.getValue();
            this.curSheetIndex = sheetDefinitionEntry.getKey();
            List<? extends List<?>> segments = ListUtils
                    .split(sheetDefinition.getRowDatas(), sheetDefinition.getMaxRowsPerSheet());
            if (segments.size() > 1) {
                sheetSegments.addAll(segments);
                writerContext.removeSheet(sheetDefinitionEntry.getKey());
            }
        }
        writerContext.addData(sheetSegments);
    }


    @Override
    public void handleComplexHeader() {
        for (Map.Entry<Integer, ExcelSheetDefinition> sheetDefinitionEntry : sheetDefinitions.entrySet()) {
            this.curSheetIndex = sheetDefinitionEntry.getKey();
            ExcelWriterSheetDefinition sheetDefinition = (ExcelWriterSheetDefinition) sheetDefinitionEntry.getValue();
            Sheet sheet = createSheet(sheetDefinitionEntry.getKey(), sheetDefinition.getSheetName());
            ExcelComplexHeader ExcelComplexHeader = sheetDefinition.getAnnotation(ExcelComplexHeader.class);
            if (ExcelComplexHeader != null) {
                CellRange[] cellRanges = ExcelComplexHeader.value();
                for (CellRange cellRange : cellRanges) {
                    this.curRowIndex = cellRange.firstRow();
                    this.curColIndex = cellRange.firstCol();
                    int firstRow = cellRange.firstRow() - 1;
                    int fristCol = cellRange.firstCol() - 1;
                    int lastRow = cellRange.lastRow() - 1;
                    int lastCol = cellRange.lastCol() - 1;

                    Row row = sheet.getRow(firstRow);
                    if (row == null) {
                        row = sheet.createRow(firstRow);
                    }
                    row.setHeightInPoints(cellRange.height());

                    Cell cell = row.createCell(fristCol);
                    cell.setCellValue(cellRange.title());

                    // 合并单元格
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, fristCol, lastCol);
                    sheet.addMergedRegion(cellRangeAddress);

                    // 设置样式
                    CellStyleBuilder cellStyleBuilder = this.cellStyleCache
                            .getCellStyleInstance(cellRange.cellStyleBuilder());
                    cell.setCellStyle(cellStyleBuilder.build(this.sxssfWorkbook, new ExcelCellStyleDefinition(this.sxssfWorkbook), cell));
                }
            }
        }
    }

    @Override
    public void addDataValidation() {
        for (Map.Entry<Integer, ExcelSheetDefinition> sheetDefinitionEntry : sheetDefinitions.entrySet()) {
            this.curSheetIndex = sheetDefinitionEntry.getKey();
            ExcelWriterSheetDefinition sheetDefinition = (ExcelWriterSheetDefinition) sheetDefinitionEntry.getValue();
            Sheet sheet = createSheet(sheetDefinitionEntry.getKey(), sheetDefinition.getSheetName());
            Map<Integer, Field> columnFields = sheetDefinition.getColumnFields();
            for (Map.Entry<Integer, Field> columnFieldEntry : columnFields.entrySet()) {
                Field field = columnFieldEntry.getValue();
                int colIndex = this.curColIndex = columnFieldEntry.getKey();

                if (colIndex < 1) {
                    throw new IllegalArgumentException(field.getName() + "' colIndex less than 1");
                }

                String[] dataValidationConstraintList = getDataValidationConstraint(field);
                if (dataValidationConstraintList != null) {

                    DataValidationHelper helper = sheet.getDataValidationHelper();
                    //加载下拉列表内容
                    DataValidationConstraint dataConstraint = helper
                            .createExplicitListConstraint(dataValidationConstraintList);
                    dataConstraint.setExplicitListValues(dataValidationConstraintList);
                    CellRangeAddressList regions = new CellRangeAddressList(sheetDefinition.getFirstDataRow(), 999,
                            colIndex - 1, colIndex - 1);

                    DataValidation dataValidation = helper.createValidation(dataConstraint, regions);

                    dataValidation.setSuppressDropDownArrow(true);
                    dataValidation.createPromptBox("提示", "可选值:" + Arrays.toString(dataValidationConstraintList));
                    dataValidation
                            .createErrorBox("错误提示", "您的输入有误, 可选值:" + Arrays.toString(dataValidationConstraintList));
                    dataValidation.setShowPromptBox(true);
                    dataValidation.setShowErrorBox(true);

                    sheet.addValidationData(dataValidation);
                }
            }
        }
    }


    @Override
    public void initHeadTitle() {
        for (Map.Entry<Integer, ExcelSheetDefinition> sheetDefinitionEntry : sheetDefinitions.entrySet()) {
            this.curSheetIndex = sheetDefinitionEntry.getKey();
            ExcelWriterSheetDefinition sheetDefinition = (ExcelWriterSheetDefinition) sheetDefinitionEntry.getValue();
            Sheet sheet = createSheet(sheetDefinitionEntry.getKey(), sheetDefinition.getSheetName());

            Row row = sheet.createRow(sheetDefinition.getFirstDataRow() - 1);
            row.setHeightInPoints(sheetDefinition.getTitleRowHeight());

            this.curRowIndex = row.getRowNum() + 1;
            Map<Integer, Field> columnFields = sheetDefinition.getColumnFields();
            for (Map.Entry<Integer, Field> columnFieldEntry : columnFields.entrySet()) {
                Field field = columnFieldEntry.getValue();
                int colIndex = this.curColIndex = columnFieldEntry.getKey();

                if (colIndex < 1) {
                    throw new IllegalArgumentException(field.getName() + "' colIndex less than 1");
                }

                ExcelExportColumn exportColumn = field.getAnnotation(ExcelExportColumn.class);
                Cell cell = row.createCell(colIndex - 1);
                cell.setCellValue(exportColumn.title());
                cell.setCellType(CellType.STRING);

                // 设置标题样式
                CellStyleBuilder cellStyleBuilder = this.cellStyleCache
                        .getCellStyleInstance(exportColumn.titleCellStyleBuilder());

                CellStyle cellStyle = cellStyleBuilder.build(this.sxssfWorkbook, new ExcelCellStyleDefinition(this.sxssfWorkbook), cell);
                cell.setCellStyle(cellStyle);
                if (!exportColumn.autoWidth()) {
                    sheet.setColumnWidth(colIndex - 1, exportColumn.colWidth() * 256);
                }
            }
        }
    }


    @Override
    public void initData() {
        for (Map.Entry<Integer, ExcelSheetDefinition> sheetDefinitionEntry : sheetDefinitions.entrySet()) {
            this.curSheetIndex = sheetDefinitionEntry.getKey();
            ExcelWriterSheetDefinition sheetDefinition = (ExcelWriterSheetDefinition) sheetDefinitionEntry.getValue();
            Sheet sheet = createSheet(sheetDefinitionEntry.getKey(), sheetDefinition.getSheetName());
            Map<Integer, Field> columnFields = sheetDefinition.getColumnFields();

            Map<Integer, ExcelCellStyleDefinition> columnCellStyles = sheetDefinition.getColumnCellStyles(sxssfWorkbook);

            List<?> rowDatas = sheetDefinition.getRowDatas();
            for (int rowIndex = 0; rowIndex < rowDatas.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + sheetDefinition.getFirstDataRow());
                row.setHeightInPoints(sheetDefinition.getDataRowHeight());

                this.curRowIndex = row.getRowNum() + 1;
                Object rowData = rowDatas.get(rowIndex);
                for (Map.Entry<Integer, Field> columnFieldEntry : columnFields.entrySet()) {
                    this.curColIndex = columnFieldEntry.getKey();
                    Field field = columnFieldEntry.getValue();
                    Cell cell = row.createCell(columnFieldEntry.getKey() - 1);

                    ExcelExportColumn exportColumn = field.getAnnotation(ExcelExportColumn.class);
                    cell.setCellType(exportColumn.cellType());

                    ExcelCellStyleDefinition cellStyleDefinition = null;
                    if (sheetDefinition.isRowStriped()) {
                        if (rowIndex % 2 == 0) {
                            cellStyleDefinition = columnCellStyles.get(columnFieldEntry.getKey() * 2 - 1);
                        } else {
                            cellStyleDefinition = columnCellStyles.get(columnFieldEntry.getKey() * 2);
                        }
                    } else {
                        cellStyleDefinition = columnCellStyles.get(columnFieldEntry.getKey());
                    }
                    CellStyle cellStyle = cellStyleDefinition.getCellStyle();

                    // 设置数据样式
                    CellStyleBuilder cellStyleBuilder = this.cellStyleCache
                            .getCellStyleInstance(exportColumn.dataCellStyleBuilder());
                    cellStyle = cellStyleBuilder.build(this.sxssfWorkbook, cellStyleDefinition, cell);

                    // 设置数据格式
                    ExcelDataFormat excelDataFormat = exportColumn.dataFormat();
                    if (!StringUtils.isBlank(excelDataFormat.value())) {
                        DataFormat dataFormat = this.sxssfWorkbook.createDataFormat();
                        cellStyle.setDataFormat(dataFormat.getFormat(excelDataFormat.value()));
                    }

                    cell.setCellStyle(cellStyle);

                    try {
                        ExcelFieldUtils.setCellValue(cell, rowData, field);
                    } catch (IllegalAccessException e) {
                        throw new ExcelWriterException("", e);
                    }
                }
            }

            // 设置列自动大小
            if (sheet instanceof SXSSFSheet) {
                SXSSFSheet sxssfSheet = (SXSSFSheet) sheet;
                sxssfSheet.trackAllColumnsForAutoSizing();

                for (Map.Entry<Integer, Field> columnFieldEntry : columnFields.entrySet()) {
                    Field field = columnFieldEntry.getValue();
                    ExcelExportColumn exportColumn = field.getAnnotation(ExcelExportColumn.class);
                    if (exportColumn.autoWidth()) {
                        sxssfSheet.autoSizeColumn(columnFieldEntry.getKey());
                    }
                }
            }
        }
    }

    @Override
    public abstract void afterCallback();

    @Override
    public <T> List<T> executeRead() {
        throw new UnsupportedOperationException("不支持此操作!");
    }

    @Override
    public Workbook executeWrite() {
        logger.debug("start write!");

        long startTimeMillis = System.currentTimeMillis();
        this.sheetDefinitions = this.writerContext.getSheetDefinitions();
        this.sheetPaging();
        this.handleComplexHeader();
        this.addDataValidation();
        this.initHeadTitle();
        this.initData();

        logger.debug("finish write![total cost {}ms]", (System.currentTimeMillis() - startTimeMillis));
        return this.sxssfWorkbook;
    }

    /**
     * 获取列的下拉校验值列表
     *
     * @param field
     * @return
     */
    private String[] getDataValidationConstraint(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            if (aClass.isAnnotationPresent(ExcelDataValidation.class)) {
                ExcelDataValidation dataValidation = aClass.getAnnotation(ExcelDataValidation.class);
                ExcelDataValidationConstraint dataValidationConstraint = null;
                try {
                    dataValidationConstraint = dataValidation.dataConstraint().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new ExcelException("实例化下拉值约束[" + dataValidation.dataConstraint() + "]时失败!", e);
                }
                dataValidationConstraint.initialize(annotation);
                return dataValidationConstraint.generate();
            }
        }
        return null;
    }

    private Sheet createSheet(int sheetIndex, String sheetName) {
        SXSSFSheet sheet = null;
        if (sheetIndex >= this.sxssfWorkbook.getNumberOfSheets()) {
            sheet = this.sxssfWorkbook.createSheet(sheetName);
        } else {
            sheet = this.sxssfWorkbook.getSheetAt(sheetIndex);
        }
        return sheet;
    }

    /**
     * 单元格样式缓存
     */
    private static class CellStyleCache {
        private Map<Class<?>, CellStyleBuilder> cellStyleCacheMap;

        public CellStyleCache() {
            this.cellStyleCacheMap = new HashMap<>();
        }

        public void addCache(Class<?> clazz, CellStyleBuilder cellStyleBuilder) {
            this.cellStyleCacheMap.put(clazz, cellStyleBuilder);
        }

        public CellStyleBuilder getCellStyleInstance(Class<?> clazz) {
            CellStyleBuilder cellStyleBuilder = this.getCache(clazz);
            if (cellStyleBuilder == null) {
                try {
                    if (CellStyleBuilder.class.isAssignableFrom(clazz)) {
                        cellStyleBuilder = (CellStyleBuilder) clazz.newInstance();
                    } else {
                        throw new ExcelWriterException("CellStyle [" + clazz + "] not assignable from CellStyleBuilder.class");
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                this.addCache(clazz, cellStyleBuilder);
            }
            return cellStyleBuilder;
        }

        public CellStyleBuilder getCache(Class<?> clazz) {
            return this.cellStyleCacheMap.get(clazz);
        }

        public void removeCache(Class<?> clazz) {
            this.cellStyleCacheMap.remove(clazz);
        }

    }
}
