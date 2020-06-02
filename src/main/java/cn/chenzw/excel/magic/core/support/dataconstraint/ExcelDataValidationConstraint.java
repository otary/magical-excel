package cn.chenzw.excel.magic.core.support.dataconstraint;

public interface ExcelDataValidationConstraint<A> {

    void initialize(A annotation);

    String[] generate();

}
