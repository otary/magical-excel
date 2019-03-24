package cn.chenzw.excel.magic.core.meta.model;


import cn.chenzw.excel.magic.core.exception.ExcelException;
import cn.chenzw.excel.magic.core.exception.ExcelWriterException;
import cn.chenzw.excel.magic.core.meta.annotation.CellRange;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelComplexHeader;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelExport;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelExportColumn;
import cn.chenzw.excel.magic.core.util.ColorUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
public class AnnotationExcelWriterSheetDefinition implements ExcelWriterSheetDefinition {
    private Class<?> cls;
    private Map<Integer, Field> columnFields;
    private int firstDataRow = 1;
    private List<?> rowDatas;
    private int order;
    private String sheeName;
    private int maxRowsPerSheet;
    private boolean isRowStriped;
    private Color rowStripeColor;
    private int titleRowHeight;
    private int dataRowHeight;


    public AnnotationExcelWriterSheetDefinition(Class<?> clazz, List<?> rowDatas) {
        this.cls = clazz;
        this.columnFields = new HashMap<>();
        this.rowDatas = rowDatas;

        init();
    }

    private void init() {
        initSheetMeta();
        initColumnFields();
        this.firstDataRow = calFirstDataRow();

    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> clazz) {
        return this.cls.getAnnotation(clazz);
    }

    @Override
    public Class<?> getBindingModel() {
        return this.cls;
    }

    @Override
    public Map<Integer, Field> getColumnFields() {
        return this.columnFields;
    }

    @Override
    public int getFirstDataRow() {
        return this.firstDataRow;
    }

    @Override
    public List<?> getRowDatas() {
        return this.rowDatas;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public String getSheetName() {
        return this.sheeName;
    }

    @Override
    public void setSheetName(String sheetName) {
        this.sheeName = sheetName;
    }

    @Override
    public int getMaxRowsPerSheet() {
        return this.maxRowsPerSheet;
    }

    @Override
    public boolean isRowStriped() {
        return this.isRowStriped;
    }

    @Override
    public Color getRowStripeColor() {
        return this.rowStripeColor;
    }

    @Override
    public int getTitleRowHeight() {
        return this.titleRowHeight;
    }

    @Override
    public int getDataRowHeight() {
        return this.dataRowHeight;
    }

    private void initColumnFields() {
        Field[] fields = this.cls.getDeclaredFields();
        for (Field field : fields) {
            ExcelExportColumn exportColumn = field.getAnnotation(ExcelExportColumn.class);
            if (exportColumn != null) {
                if (exportColumn.colIndex() < 1) {
                    throw new ExcelException(
                            "The @ExcelExportColumn on Field [" + field.getName() + "] of Class[" + this.cls
                                    .getCanonicalName() + "] miss \"colIndex\" attribute or less than 1 !");
                }

                // exists colIndex
                if (this.columnFields.containsKey(exportColumn.colIndex())) {
                    throw new ExcelException(
                            "The @ExcelExportColumn on Field [" + field.getName() + "] of Class[" + this.cls
                                    .getCanonicalName() + "] has conflicting \"colIndex\" value => [" + exportColumn
                                    .colIndex() + "] !");
                }

                field.setAccessible(true);
                this.columnFields.put(exportColumn.colIndex(), field);
            }
        }
    }

    private void initSheetMeta() {
        ExcelExport excelExport = this.cls.getAnnotation(ExcelExport.class);
        if (excelExport == null) {
            throw new ExcelWriterException("Class[" + this.cls.getCanonicalName() + "] miss @ExcelExport!");
        }
        this.order = excelExport.order();
        this.sheeName = excelExport.sheetName();
        this.maxRowsPerSheet = excelExport.maxRowsPerSheet();
        this.isRowStriped = excelExport.rowStriped();
        if (this.isRowStriped && !StringUtils.isBlank(excelExport.rowStripeColor())) {
            this.rowStripeColor = ColorUtils.hexToRgb(excelExport.rowStripeColor());
        }
        this.titleRowHeight = excelExport.titleRowHeight();
        this.dataRowHeight = excelExport.dataRowHeight();

    }

    /**
     * 根据复杂表头，计算出数据起始行号
     * @return
     */
    private int calFirstDataRow() {
        ExcelComplexHeader complexHeader = this.cls.getAnnotation(ExcelComplexHeader.class);
        if (complexHeader != null) {
            CellRange[] cellRanges = complexHeader.value();
            int startRow = 1;
            for (CellRange cellRange : cellRanges) {
                if (cellRange.lastRow() > startRow) {
                    startRow = cellRange.lastRow();
                }
            }
            return (startRow + 1);
        }
        return 1;
    }


    @Override
    public int compareTo(Object o) {
        AnnotationExcelWriterSheetDefinition sheetDefinition = (AnnotationExcelWriterSheetDefinition) o;
        if (this.order == sheetDefinition.getOrder()) {
            return 0;
        }
        if (this.order > sheetDefinition.getOrder()) {
            return 1;
        }
        return -1;
    }
}
