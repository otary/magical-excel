package cn.chenzw.excel.magic.core.support.callback;

import cn.chenzw.excel.magic.core.meta.model.ExcelCellDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;

/**
 *
 * @author chenzw
 * @since 1.0.3
 */
public interface ExcelCellReadExceptionCallback {

    void call(ExcelRowDefinition rowDefinition, ExcelCellDefinition cellDefinition, Exception ex);
}
