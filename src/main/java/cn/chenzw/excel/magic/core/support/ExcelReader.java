package cn.chenzw.excel.magic.core.support;


import cn.chenzw.excel.magic.core.context.AnnotationExcelReaderContext;
import org.apache.poi.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author chenzw
 */
public class ExcelReader {

    private ByteArrayOutputStream baos;

    public ExcelReader(InputStream is) {
        this.baos = new ByteArrayOutputStream();
        try {
            IOUtils.copy(is, baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ExcelReader newInstance(InputStream is){
        return new ExcelReader(is);
    }

    public <T> List<T> read(Class<T> clazz) {
        return new AnnotationExcelReaderContext(new ByteArrayInputStream(baos.toByteArray()), clazz).getExecutor()
                .executeRead();
    }

}
