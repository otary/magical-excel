package cn.chenzw.excel.magic.core.support.callback.impl;

import cn.chenzw.excel.magic.core.exception.ExcelReaderException;
import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;
import cn.chenzw.excel.magic.core.support.callback.ExcelRowReadExceptionCallback;

public class DefaultExcelRowReadExceptionCallback implements ExcelRowReadExceptionCallback {

    @Override
    public void call(final ExcelRowDefinition rowDefinition, final Exception ex) {
        if(ex instanceof ExcelReaderException){
            throw (ExcelReaderException) ex;
        }
        throw new ExcelReaderException(ex);
    }
}
