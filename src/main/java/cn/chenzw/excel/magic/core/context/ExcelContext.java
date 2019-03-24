package cn.chenzw.excel.magic.core.context;


import cn.chenzw.excel.magic.core.executor.ExcelExecutor;
import cn.chenzw.excel.magic.core.meta.model.ExcelSheetDefinition;

import java.util.Map;

/**
 * @author chenzw
 */
public interface ExcelContext {

    Map<Integer, ExcelSheetDefinition> getSheetDefinitions();

    ExcelExecutor getExecutor();


}
