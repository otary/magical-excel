package cn.chenzw.excel.magic.core.support.converter;

public class ExcelDefaultConverter implements AbstractExcelColumnConverter {
    @Override
    public void initialize(Object annotation) {

    }

    @Override
    public Object convert(String value) {
        return value;
    }
}
