package cn.chenzw.excel.magic.core.processor;


import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;

/**
 * @author chenzw
 */
public interface ExcelPerRowProcessor {

    void processTotalRow(int totalRows);

    void processPerRow(ExcelRowDefinition row);
}
