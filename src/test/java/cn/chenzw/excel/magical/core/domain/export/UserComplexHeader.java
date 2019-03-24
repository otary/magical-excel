package cn.chenzw.excel.magical.core.domain.export;

import cn.chenzw.excel.magic.core.meta.annotation.*;
import cn.chenzw.excel.magic.core.meta.style.LightGreenTitleCellStyleBuilder;

import java.util.Date;

@ExcelComplexHeader({
        @CellRange(firstCol = 1, lastCol = 4, firstRow = 1, lastRow = 2, title = "用户数据", cellStyleBuilder = LightGreenTitleCellStyleBuilder.class),
        @CellRange(firstRow = 3, lastRow = 3, firstCol = 1, lastCol = 2, title = "基本信息", cellStyleBuilder = LightGreenTitleCellStyleBuilder.class),
        @CellRange(firstRow = 3, lastRow = 3, firstCol = 3, lastCol = 4, title = "扩展信息", cellStyleBuilder = LightGreenTitleCellStyleBuilder.class)})
@ExcelExport(sheetName = "用户数据")
public class UserComplexHeader {
    @ExcelExportColumn(colIndex = 1, title = "姓名")
    private String name;

    @ExcelExportColumn(colIndex = 2, title = "年龄")
    private Integer age;

    @ExcelExportColumn(colIndex = 3, title = "生日", dataFormat = @ExcelDataFormat("yyyy-MM-dd"))
    private Date birth;

    @ExcelExportColumn(colIndex = 4, title = "体重", dataFormat = @ExcelDataFormat("0.00"))
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

    @Override
    public String toString() {
        return "UserComplexHeader{" + "name='" + name + '\'' + ", age=" + age + ", birth=" + birth + ", height="
                + height + '}';
    }
}
