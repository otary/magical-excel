package cn.chenzw.excel.magic.core.executor;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * @author chenzw
 */
public interface ExcelExecutor {

    <T> List<T> executeRead();

    Workbook executeWrite();

}
