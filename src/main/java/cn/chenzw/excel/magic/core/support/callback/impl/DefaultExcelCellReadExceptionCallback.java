package cn.chenzw.excel.magic.core.support.callback.impl;

import cn.chenzw.excel.magic.core.exception.ExcelException;
import cn.chenzw.excel.magic.core.exception.ExcelReaderException;
import cn.chenzw.excel.magic.core.meta.model.ExcelCellDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;
import cn.chenzw.excel.magic.core.support.callback.ExcelCellReadExceptionCallback;

/**
 * @author chenzw
 * @since 1.0.3
 */
public class DefaultExcelCellReadExceptionCallback implements ExcelCellReadExceptionCallback {

    @Override
    public void call(ExcelRowDefinition rowDefinition, ExcelCellDefinition cellDefinition, Exception ex) {
        if (ex instanceof ExcelReaderException) {
            throw (ExcelReaderException) ex;
        }
       throw new ExcelException(ex.getMessage(), ex);
    }
}
