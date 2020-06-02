package cn.chenzw.excel.magic.core.context;

import cn.chenzw.excel.magic.core.executor.ExcelExecutor;
import cn.chenzw.excel.magic.core.executor.ExcelReaderExecutor;
import cn.chenzw.excel.magic.core.meta.model.AnnotationExcelReaderSheetDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelReaderSheetDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelSheetDefinition;
import cn.chenzw.excel.magic.core.support.callback.ExcelCellReadExceptionCallback;
import cn.chenzw.excel.magic.core.support.callback.ExcelRowReadExceptionCallback;
import cn.chenzw.excel.magic.core.support.callback.impl.DefaultExcelCellReadExceptionCallback;
import cn.chenzw.excel.magic.core.support.callback.impl.DefaultExcelRowReadExceptionCallback;

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
    private ExcelRowReadExceptionCallback rowReadExceptionCallback = new DefaultExcelRowReadExceptionCallback();
    private ExcelCellReadExceptionCallback cellReadExceptionCallback = new DefaultExcelCellReadExceptionCallback();


    public <T> AnnotationExcelReaderContext(InputStream is, Class<T> clazz, ExcelRowReadExceptionCallback rowReadExceptionCallback, ExcelCellReadExceptionCallback cellReadExceptionCallback) {
        this.inputStream = is;
        this.sheetdefinitions = new HashMap<>();
        if (rowReadExceptionCallback != null) {
            this.rowReadExceptionCallback = rowReadExceptionCallback;
        }
        if (cellReadExceptionCallback != null) {
            this.cellReadExceptionCallback = cellReadExceptionCallback;
        }
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

    @Override
    public ExcelRowReadExceptionCallback getExcelRowReadExceptionCallback() {
        return this.rowReadExceptionCallback;
    }

    @Override
    public ExcelCellReadExceptionCallback getExcelCellReadExceptionCallback() {
        return this.cellReadExceptionCallback;
    }


}
