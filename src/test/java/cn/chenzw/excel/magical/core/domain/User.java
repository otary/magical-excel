package cn.chenzw.excel.magical.core.domain;

import cn.chenzw.excel.magic.core.meta.annotation.ExcelDataFormat;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelExport;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelExportColumn;
import cn.chenzw.excel.magic.core.meta.annotation.ExcelImport;

import java.util.Date;

@ExcelImport(sheetIndex = 2, firstDataRow = 2)
@ExcelExport(sheetName = "用户数据")
public class User {

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
        return "User{" + "name='" + name + '\'' + ", age=" + age + ", birth=" + birth + ", height=" + height + '}';
    }
}
