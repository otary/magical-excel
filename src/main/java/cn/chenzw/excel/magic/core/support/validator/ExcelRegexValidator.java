package cn.chenzw.excel.magic.core.support.validator;

import cn.chenzw.excel.magic.core.meta.annotation.validation.ExcelRegexValue;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ExcelRegexValidator implements AbstractExcelColumnValidator<ExcelRegexValue> {


    private Pattern pattern;
    private String regex;

    @Override
    public void initialize(ExcelRegexValue excelRegexValue) {
        this.regex = excelRegexValue.regex();
        if (StringUtils.isBlank(this.regex)) {
            throw new IllegalArgumentException("正则表达式为空!");
        }
        this.pattern = Pattern.compile(this.regex);
    }

    @Override
    public boolean validate(String value) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        if (pattern.matcher(value).matches()) {
            return true;
        }
        return false;
    }
}
