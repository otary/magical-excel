package cn.chenzw.excel.magic.core.support.validator;

import java.lang.annotation.Annotation;

/**
 * @author chenzw
 */
public interface AbstractExcelColumnValidator<A extends Annotation> {

    void initialize(A annotation);

    boolean validate(String value);
}
