package cn.chenzw.excel.magic.core.support.converter;

/**
 * 字段值转换器
 * @param <A>
 * @param <T>
 */
public interface AbstractExcelColumnConverter<A, T> {

    void initialize(A annotation);

    T convert(String value);
}
