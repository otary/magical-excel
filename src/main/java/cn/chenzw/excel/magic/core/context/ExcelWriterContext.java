package cn.chenzw.excel.magic.core.context;

import java.util.List;

/**
 * @author chenzw
 */
public interface ExcelWriterContext extends ExcelContext {

    /**
     * 添加数据
     * @param datas
     */
    void addData(List<?>... datas);

    /**
     * 添加数据
     * @param datas
     */
    void addData(List<List<?>> datas);

    /**
     * 添加模型元数据
     * @param clazzs
     */
    void addModel(Class<?>... clazzs);

    /**
     * 移除Sheet定义
     * @param index
     */
    void removeSheet(int index);

}
