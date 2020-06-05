package cn.chenzw.excel.magical.core.domain;

import cn.chenzw.excel.magic.core.meta.annotation.*;
import cn.chenzw.excel.magic.core.meta.annotation.converter.ExcelKVConvert;
import cn.chenzw.excel.magic.core.meta.annotation.datavalidation.ExcelStringList;

import java.util.Date;

@ExcelImport(sheetIndex = 1, firstDataRow = 2)
@ExcelExport(sheetName = "节假日")
public class HolidayCfg {

    @ExcelImportColumn(colIndex = 1, dateFormat = "yyyy-MM-dd", allowBlank = false)
    @ExcelExportColumn(title = "节假日日期", colIndex = 1, dataFormat = @ExcelDataFormat("yyyy-MM-dd HH:mm:ss"))
    private Date holidayDate;

    @ExcelImportColumn(colIndex = 2, allowBlank = false)
    @ExcelExportColumn(title = "节假日名称", colIndex = 2)
    private String holidayName;

    @ExcelKVConvert(kvmap = {"是=0", "否=1"})
    @ExcelImportColumn(colIndex = 3, allowBlank = false)
    @ExcelStringList({"是", "否"})
    @ExcelExportColumn(title = "是否上班", colIndex = 3)
    private String isWork;

    @ExcelImportColumn(colIndex = 4)
    @ExcelExportColumn(title = "备注", colIndex = 4)
    private String remark;


    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(final String holidayName) {
        this.holidayName = holidayName;
    }

    public String getIsWork() {
        return isWork;
    }

    public void setIsWork(final String isWork) {
        this.isWork = isWork;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(final String remark) {
        this.remark = remark;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(final Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    @Override
    public String toString() {
        return "HolidayCfg{" + "holidayDate='" + holidayDate + '\'' + ", holidayName='" + holidayName + '\''
                + ", isWork='" + isWork + '\'' + ", remark='" + remark + '\'' + '}';
    }
}
