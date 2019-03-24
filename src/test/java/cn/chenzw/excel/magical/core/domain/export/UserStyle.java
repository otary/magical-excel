package cn.chenzw.excel.magical.core.domain.export;

import cn.chenzw.excel.magic.core.meta.annotation.ExcelDataFormat;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelExport;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelExportColumn;
import cn.chenzw.excel.magic.core.meta.style.LightGreenTitleCellStyleBuilder;

import java.util.Date;

@ExcelExport(sheetName = "用户数据", rowStriped = true, rowStripeColor = "E2EFDA")
public class UserStyle {

    @ExcelExportColumn(colIndex = 1, title = "姓名", titleCellStyleBuilder = LightGreenTitleCellStyleBuilder.class)
    private String name;

    @ExcelExportColumn(colIndex = 2, title = "年龄", titleCellStyleBuilder = LightGreenTitleCellStyleBuilder.class)
    private Integer age;

    @ExcelExportColumn(colIndex = 3, title = "生日", dataFormat = @ExcelDataFormat("yyyy-MM-dd"), titleCellStyleBuilder = LightGreenTitleCellStyleBuilder.class)
    private Date birth;

    @ExcelExportColumn(colIndex = 4, title = "体重", dataFormat = @ExcelDataFormat("0.00"), titleCellStyleBuilder = LightGreenTitleCellStyleBuilder.class)
    private Double height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
