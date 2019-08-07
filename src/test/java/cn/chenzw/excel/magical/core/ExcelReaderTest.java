package cn.chenzw.excel.magical.core;

import cn.chenzw.excel.magic.core.support.ExcelReader;
import cn.chenzw.excel.magical.core.domain.ExcelDutyAdjustRecord;
import cn.chenzw.excel.magical.core.domain.ExcelDutyVacation;
import cn.chenzw.excel.magical.core.domain.HolidayCfg;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.InputStream;
import java.util.List;

@RunWith(JUnit4.class)
public class ExcelReaderTest {

    private static final String EXCEL_TEMPLATE_DIR = "excel-template/";

    @Test
    public void test() {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(EXCEL_TEMPLATE_DIR + "multi_sheet_data.xlsx");
        ExcelReader excelReader = new ExcelReader(is);
        List<HolidayCfg> holidayCfgs = excelReader.read(HolidayCfg.class);

        Assert.assertEquals(1000, holidayCfgs.size());
    }

    @Test
    public void test2() {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(EXCEL_TEMPLATE_DIR + "duty_vacation.xlsx");
        ExcelReader excelReader = new ExcelReader(is);
        List<ExcelDutyVacation> excelDutyVacations = excelReader.read(ExcelDutyVacation.class);

        Assert.assertEquals(9, excelDutyVacations.size());
    }

    @Test
    public void test3() {
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(EXCEL_TEMPLATE_DIR + "duty_adjust.xlsx");
        ExcelReader excelReader = new ExcelReader(is);
        List<ExcelDutyAdjustRecord> excelDutyAdjustRecords = excelReader.read(ExcelDutyAdjustRecord.class);

        Assert.assertEquals(5, excelDutyAdjustRecords.size());
    }

}
