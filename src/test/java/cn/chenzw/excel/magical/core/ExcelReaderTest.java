package cn.chenzw.excel.magical.core;

import cn.chenzw.excel.magic.core.support.ExcelReader;
import cn.chenzw.excel.magical.core.domain.ExcelDutyAdjustRecord;
import cn.chenzw.excel.magical.core.domain.ExcelDutyVacation;
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
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("multi_sheet_data.xlsx");
        ExcelReader excelReader = new ExcelReader(is);
        List<HolidayCfg> holidayCfgs = excelReader.read(HolidayCfg.class);
        System.out.println(holidayCfgs.size());
        // List<User> users = excelReader.read(User.class);
        // Assert.assertTrue(holidayCfgs.size() > 0);

        // Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void test2() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("duty_vacation.xlsx");
        ExcelReader excelReader = new ExcelReader(is);
        List<ExcelDutyVacation> excelDutyVacations = excelReader.read(ExcelDutyVacation.class);
        System.out.println(excelDutyVacations);
    }

    @Test
    public void test3() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("duty_adjust.xlsx");
        ExcelReader excelReader = new ExcelReader(is);
        List<ExcelDutyAdjustRecord> excelDutyAdjustRecords = excelReader.read(ExcelDutyAdjustRecord.class);
        System.out.println(excelDutyAdjustRecords);
    }

}
