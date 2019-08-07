package cn.chenzw.excel.magical.core.domain;

import cn.chenzw.excel.magic.core.meta.annotation.*;

import java.util.Date;

@ExcelImport(sheetIndex = 1, firstDataRow = 2)
@ExcelExport
public class ExcelDutyAdjustRecord {

    @ExcelExportColumn(colIndex = 1, title = "值班组名称")
    @ExcelImportColumn(colIndex = 1, allowBlank = false)
    private String dutyName;


    @ExcelExportColumn(colIndex = 2, title = "调班人员岗位名称")
    @ExcelImportColumn(colIndex = 2, allowBlank = false)
    private String adjustOrder;

    /**
     * 调班人员
     **/
    @ExcelExportColumn(colIndex = 3, title = "调班人员用户名")
    @ExcelImportColumn(colIndex = 3, allowBlank = false)
    private String adjustStaff;

    /**
     * 调班时间
     **/
    @ExcelExportColumn(colIndex = 4, title = "调班时间", dataFormat = @ExcelDataFormat("yyyy/MM/dd"))
    @ExcelImportColumn(colIndex = 4, allowBlank = false, dateFormat = "yyyy/MM/dd")
    private Date adjustDate;


    @ExcelExportColumn(colIndex = 5, title = "被调班人员岗位名称")
    @ExcelImportColumn(colIndex = 5, allowBlank = false)
    private String beAdjustOrder;

    /**
     * 被调班人员
     **/
    @ExcelExportColumn(colIndex = 6, title = "被调班人员用户名")
    @ExcelImportColumn(colIndex = 6, allowBlank = false)
    private String beAdjustStaff;

    /**
     * 被调班时间
     **/
    @ExcelExportColumn(colIndex = 7, title = "被调班日期", dataFormat = @ExcelDataFormat("yyyy/MM/dd"))
    @ExcelImportColumn(colIndex = 7, allowBlank = false, dateFormat = "yyyy/MM/dd")
    private Date beAdjustDate;

    /**
     * 备注
     **/
    @ExcelExportColumn(colIndex = 8, title = "备注")
    @ExcelImportColumn(colIndex = 8)
    private String remark;


    /**
     * 状态：0-审核中，1-有效，2-无效
     **/
    private Integer state;


    /**
     * 登记人
     **/
    private Long creator;


    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getAdjustOrder() {
        return adjustOrder;
    }

    public void setAdjustOrder(String adjustOrder) {
        this.adjustOrder = adjustOrder;
    }

    public String getAdjustStaff() {
        return adjustStaff;
    }

    public void setAdjustStaff(String adjustStaff) {
        this.adjustStaff = adjustStaff;
    }

    public Date getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(Date adjustDate) {
        this.adjustDate = adjustDate;
    }

    public String getBeAdjustOrder() {
        return beAdjustOrder;
    }

    public void setBeAdjustOrder(String beAdjustOrder) {
        this.beAdjustOrder = beAdjustOrder;
    }

    public String getBeAdjustStaff() {
        return beAdjustStaff;
    }

    public void setBeAdjustStaff(String beAdjustStaff) {
        this.beAdjustStaff = beAdjustStaff;
    }

    public Date getBeAdjustDate() {
        return beAdjustDate;
    }

    public void setBeAdjustDate(Date beAdjustDate) {
        this.beAdjustDate = beAdjustDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "ExcelDutyAdjustRecord{" + "dutyName='" + dutyName + '\'' + ", adjustOrder='" + adjustOrder + '\''
                + ", adjustStaff='" + adjustStaff + '\'' + ", adjustDate=" + adjustDate + ", beAdjustOrder='"
                + beAdjustOrder + '\'' + ", beAdjustStaff=" + beAdjustStaff + ", beAdjustDate=" + beAdjustDate
                + ", remark='" + remark + '\'' + ", state=" + state + ", creator=" + creator + '}';
    }
}
