package cn.chenzw.excel.magic.core.meta.model;


import cn.chenzw.excel.magic.core.exception.ExcelReaderException;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelImport;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelImportColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationExcelReaderSheetDefinition implements ExcelReaderSheetDefinition {

    private Class<?> cls;
    private Map<Integer, Field> columnFields;
    private Map<Integer, String> columnTitles;

    private int sheetIndex;
    private int[] sheetIndexs;
    private int firstDataRow;


    public AnnotationExcelReaderSheetDefinition(Class<?> clazz) {
        this.cls = clazz;
        this.columnFields = new HashMap<>();
        this.columnTitles = new HashMap<>();
        init();
    }

    private void init() {
        initSheetMeta();
        initColumnFields();
    }

    private void initSheetMeta() {
        ExcelImport excelImport = this.cls.getAnnotation(ExcelImport.class);
        if (excelImport == null) {
            throw new ExcelReaderException("Class[" + this.cls.getCanonicalName() + "] miss @ExcelImport!");
        }
        this.sheetIndexs = excelImport.sheetIndex();
        this.firstDataRow = excelImport.firstDataRow();
    }

    private void initColumnFields() {
        Field[] fields = this.cls.getDeclaredFields();
        for (Field field : fields) {
            ExcelImportColumn importColumn = field.getAnnotation(ExcelImportColumn.class);
            if (importColumn != null) {
                field.setAccessible(true);
                this.columnFields.put(importColumn.colIndex(), field);
                this.columnTitles.put(importColumn.colIndex(), importColumn.title());
            }
        }
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
    public int getFirstDataRow() {
        return this.firstDataRow;
    }

    @Override
    public Map<Integer, Field> getColumnFields() {
        return this.columnFields;
    }

    @Override
    public Map<Integer, String> getColumnTitles() {
        return this.columnTitles;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public int getSheetIndex() {
        return this.sheetIndex;
    }

    @Override
    public int[] getSheetIndexs() {
        return this.sheetIndexs;
    }
}
