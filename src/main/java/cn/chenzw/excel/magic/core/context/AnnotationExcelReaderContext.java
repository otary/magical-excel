package cn.chenzw.excel.magic.core.context;

import cn.chenzw.excel.magic.core.executor.ExcelExecutor;
import cn.chenzw.excel.magic.core.executor.ExcelReaderExecutor;
import cn.chenzw.excel.magic.core.meta.model.AnnotationExcelReaderSheetDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelReaderSheetDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelSheetDefinition;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenzw
 */
public class AnnotationExcelReaderContext implements ExcelReaderContext {

    private Map<Integer, ExcelSheetDefinition> sheetdefinitions;
    private ExcelExecutor excelExecutor;
    private InputStream inputStream;

    public <T> AnnotationExcelReaderContext(InputStream is, Class<T> clazz) {
        this.inputStream = is;
        this.sheetdefinitions = new HashMap<>();
        this.excelExecutor = new ExcelReaderExecutor<T>(this);

        ExcelReaderSheetDefinition sheetDefinition = new AnnotationExcelReaderSheetDefinition(clazz);
        int[] sheetIndexs = sheetDefinition.getSheetIndexs();
        for (int sheetIndex : sheetIndexs) {
            this.sheetdefinitions.put(sheetIndex, sheetDefinition);
        }
    }

    @Override
    public Map<Integer, ExcelSheetDefinition> getSheetDefinitions() {
        return this.sheetdefinitions;
    }

    @Override
    public ExcelExecutor getExecutor() {
        return this.excelExecutor;
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }


}
