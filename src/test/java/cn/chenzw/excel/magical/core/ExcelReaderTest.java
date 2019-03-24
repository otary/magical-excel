package cn.chenzw.excel.magical.core;

import cn.chenzw.excel.magic.core.support.ExcelReader;
import cn.chenzw.excel.magical.core.domain.HolidayCfg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.InputStream;
import java.util.List;

@RunWith(JUnit4.class)
public class ExcelReaderTest {


    @Test
    public void test() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xlsx");
        ExcelReader excelReader = new ExcelReader(is);
        List<HolidayCfg> read = excelReader.read(HolidayCfg.class);
    }
}
