package cn.chenzw.excel.magic.core.context;

import cn.chenzw.excel.magic.core.support.callback.ExcelCellReadExceptionCallback;
import cn.chenzw.excel.magic.core.support.callback.ExcelRowReadExceptionCallback;

import java.io.InputStream;

/**
 * @author chenzw
 */
public interface ExcelReaderContext extends ExcelContext {

    InputStream getInputStream();

    /**
     * @return
     * @since 1.0.5
     */
    ExcelRowReadExceptionCallback getExcelRowReadExceptionCallback();

    /**
     * @return
     * @since 1.0.5
     */
    ExcelCellReadExceptionCallback getExcelCellReadExceptionCallback();

}

