package cn.chenzw.excel.magic.core.lifecycle;

import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;

public interface ExcelReaderLifecycle {


    /**
     * 是否空行
     * @param row
     * @return
     */
    boolean isEmptyRow(ExcelRowDefinition row);


    /**
     * 数据校验
     * @param row
     * @return
     */
    boolean validate(ExcelRowDefinition row);

    /**
     * 格式转换
     * @param row
     * @return
     */
    void format(ExcelRowDefinition row);

}
