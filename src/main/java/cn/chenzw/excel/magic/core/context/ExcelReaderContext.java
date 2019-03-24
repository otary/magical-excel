package cn.chenzw.excel.magic.core.context;

import java.io.InputStream;

/**
 * @author chenzw
 */
public interface ExcelReaderContext extends ExcelContext {

    InputStream getInputStream();

}

