package cn.chenzw.excel.magic.core.support.validator;

import java.lang.annotation.Annotation;

/**
 * 默认校验器
 * @author chenzw
 */
public class ExcelDefaultValidator implements AbstractExcelColumnValidator {

    @Override
    public void initialize(Annotation annotation) {

    }

    @Override
    public boolean validate(String value) {
        return true;
    }
}
