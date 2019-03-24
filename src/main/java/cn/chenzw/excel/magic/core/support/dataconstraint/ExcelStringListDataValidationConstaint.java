package cn.chenzw.excel.magic.core.support.dataconstraint;


import cn.chenzw.excel.magic.core.meta.annotation.datavalidation.ExcelStringList;

/**
 * @author chenzw
 */
public class ExcelStringListDataValidationConstaint
        implements ExcelDataValidationConstraint<ExcelStringList> {

    private String[] sList;

    @Override
    public void initialize(ExcelStringList annotation) {
        this.sList = annotation.value();
    }

    @Override
    public String[] generate() {
        return sList;
    }
}
