package cn.chenzw.excel.magic.core.support.callback;

import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;

/**
 * 行解析异常回调
 *
 * @author chenzw
 * @since 1.0.5
 */
public interface ExcelRowReadExceptionCallback {

    void call(ExcelRowDefinition rowDefinition, Exception ex);
}
