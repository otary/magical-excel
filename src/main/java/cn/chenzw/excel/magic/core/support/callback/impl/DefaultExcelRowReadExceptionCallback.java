package cn.chenzw.excel.magic.core.support.callback.impl;

import cn.chenzw.excel.magic.core.exception.ExcelReaderException;
import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;
import cn.chenzw.excel.magic.core.support.callback.ExcelRowReadExceptionCallback;

public class DefaultExcelRowReadExceptionCallback implements ExcelRowReadExceptionCallback {

    @Override
    public void call(ExcelRowDefinition rowDefinition, Exception ex) {
        System.out.println("-----------------------");
        System.out.println(ex instanceof ExcelReaderException);
        if(ex instanceof ExcelReaderException){
            throw (ExcelReaderException) ex;
        }
        throw new ExcelReaderException(ex);
    }
}
