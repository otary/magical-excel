package cn.chenzw.excel.magic.core.context;

import cn.chenzw.excel.magic.core.executor.ExcelExecutor;
import cn.chenzw.excel.magic.core.executor.ExcelWriterExecutor;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelExport;
import cn.chenzw.excel.magic.core.meta.model.AnnotationExcelWriterSheetDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelSheetDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelWriterSheetDefinition;
import cn.chenzw.excel.magic.core.util.ListUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author chenzw
 */
public class AnnotationExcelWriterContext implements ExcelWriterContext {

    private List<ExcelWriterSheetDefinition> sheetDefinitions;
    private Map<Integer, ExcelSheetDefinition> sheetDefinitionMap;
    private ExcelExecutor excelExecutor;
    private LoadingCache<String, Integer> sheetNameCache;

    public AnnotationExcelWriterContext() {
        this.sheetDefinitions = new ArrayList<>();
        this.sheetDefinitionMap = new TreeMap<>();

        this.sheetNameCache = CacheBuilder.newBuilder().maximumSize(10).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(String key) throws Exception {
                return 1;
            }
        });
        this.excelExecutor = new ExcelWriterExecutor(this);
    }


    @Override
    public Map<Integer, ExcelSheetDefinition> getSheetDefinitions() {
        return this.sheetDefinitionMap;
    }

    @Override
    public ExcelExecutor getExecutor() {
        return this.excelExecutor;
    }


    @Override
    public void addData(List<?>... datas) {
        this.addData(Arrays.asList(datas));
    }

    @Override
    public void addData(List<List<?>> datas) {
        for (List<?> data : datas) {
            sheetDefinitions.add(new AnnotationExcelWriterSheetDefinition(ListUtils.getGenericClass(data), data));
        }
        Collections.sort(sheetDefinitions);

        buildSheetDefinitionMap();
    }

    @Override
    public void addModel(Class<?>... clazzs) {
        for (Class<?> clazz : clazzs) {
            sheetDefinitions.add(new AnnotationExcelWriterSheetDefinition(clazz, Collections.emptyList()));
        }
        Collections.sort(sheetDefinitions);

        buildSheetDefinitionMap();
    }

    @Override
    public void removeSheet(int index) {
        sheetDefinitions.remove(index);
    }

    /**
     * 获取定义的SheetName
     * @param sheetDefinition
     * @return
     */
    private String getOrignalSheetName(ExcelSheetDefinition sheetDefinition) {
        ExcelExport excelExport = sheetDefinition.getAnnotation(ExcelExport.class);
        return excelExport.sheetName();
    }

    /**
     * 生成唯一性的sheetName
     * @param sheetName
     * @return
     * @throws ExecutionException
     */
    private String buildUniqueSheetName(String sheetName) {
        try {
            int cnt = this.sheetNameCache.get(sheetName);
            this.sheetNameCache.put(sheetName, cnt + 1);
            if (cnt > 1) {
                return sheetName + "_" + cnt;
            } else {
                return sheetName;
            }
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
        return "数据";
    }

    private void buildSheetDefinitionMap() {
        sheetDefinitionMap.clear();
        sheetNameCache.invalidateAll();

        for (int i = 0; i < sheetDefinitions.size(); i++) {
            ExcelWriterSheetDefinition tSheetDefinition = sheetDefinitions.get(i);
            String orignalSheetName = getOrignalSheetName(tSheetDefinition);
            tSheetDefinition.setSheetName(buildUniqueSheetName(orignalSheetName));
            this.sheetDefinitionMap.put(i, tSheetDefinition);
        }
    }
}
