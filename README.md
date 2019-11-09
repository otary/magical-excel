## mgical-excel：基于注解的Excel导入导出开源组件

![](https://img.shields.io/badge/jdk-1.7%2B-brightgreen)
![](https://img.shields.io/badge/poi-3.17-yellowgreen)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b1b9bc199a124f348801dcdb84905bf7)](https://www.codacy.com/manual/otary/magical-excel?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=otary/magical-excel&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/otary/magical-excel.svg?branch=master)](https://travis-ci.org/otary/magical-excel)
[![codecov](https://codecov.io/gh/otary/magical-excel/branch/master/graph/badge.svg)](https://codecov.io/gh/otary/magical-excel)


## 环境配置

#### maven配置

```` xml
<dependency>
	<groupId>cn.chenzw.excel</groupId>
    <artifactId>magical-excel</artifactId>
    <version>1.0</version>
</dependency>
````


#### 依赖

```` xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.17</version>
</dependency>
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.17</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.5</version>
</dependency>
<dependency>
    <groupId>cn.chenzw.excel</groupId>
    <artifactId>magical-excel</artifactId>
    <version>1.0</version>
</dependency>
 ````

 
---
  
## 核心注解

#### 导出

**@ExcelExport**：注解于类上，标注Sheet页数据
  + order:顺序（值越小，越靠前）
  + sheetName：Sheet页名称
  + maxRowsPerSheet：每个Sheet页允许的最大条数（超过将进行分Sheet）
  + rowStriped: 是否条纹显示
  + titleRowHeight: 标题行高度 
  + dataRowHeight：数据行高度

**@ExcelExportColumn**：注解于字段上
  + title：标题
  + colIndex:列索引（起始值:1）
  + cellType:列类型（默认:String）
	  + CellType.NUMERIC
	  + CellType.STRING
	  + CellType.FORMULA：公式
	  + BLANK
	  + BOOLEAN
	  + ERROR
  + dataFormat：数据格式
  + titleCellStyleBuilder：标题样式
  + dataCellStyleBuilder：数据样式
  + autoWidth：是否自动调整宽度（默认:false）
  + colWidth:自定义cell宽度（默认:16）


#### 导入

**@ExcelImport**：
  + sheetIndex：绑定的sheet页（可多个）
  + firstDataRow：起始数据行（默认:1）

**@ExcelImportColumn**：
  + colIndex：列索引（起始值:1）
  + allowBlank:是否允许为空（默认: true）
  + dateFormat：日期格式

## 应用场景

#### 模版导出

``` java
// 单sheet模版
try (FileOutputStream fos = new FileOutputStream(new File("template_by_model.xlsx"))) {
   ExcelWriter.newTemplateInstance(HolidayCfg.class).write(fos);
} catch (IOException e) {
   e.printStackTrace();
}

// 多Sheet模版
try (FileOutputStream fos = new FileOutputStream(new File("template_by_models.xlsx"))) {
   ExcelWriter.newTemplateInstance(HolidayCfg.class, User.class).write(fos);
} catch (IOException e) {
   e.printStackTrace();
}

````


```` java
import cn.chenzw.excel.magic.core.meta.annotation.*;
import cn.chenzw.excel.magic.core.meta.annotation.converter.ExcelKVConvert;
import cn.chenzw.excel.magic.core.meta.annotation.datavalidation.ExcelStringList;

import java.util.Date;

@ExcelExport(sheetName = "节假日")
public class HolidayCfg {

    @ExcelExportColumn(title = "节假日日期", colIndex = 1, dataFormat = @ExcelDataFormat("yyyy-MM-dd HH:mm:ss"))
    private Date holidayDate;

    @ExcelExportColumn(title = "节假日名称", colIndex = 2)
    private String holidayName;

    @ExcelKVConvert(kvmap = {"是=0", "否=1"})
    @ExcelExportColumn(title = "是否上班", colIndex = 3)
    private String isWork;

    @ExcelExportColumn(title = "备注", colIndex = 4)
    private String remark;
    
    ....
｝

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

    ....
}
````

#### 数据导出(基础)

```` java
// 单Sheet数据导出
try (FileOutputStream fos = new FileOutputStream(new File("single_sheet_data.xlsx"))) {
    ExcelWriter.newInstance().addData(users).write(fos);
} catch (IOException e) {
    e.printStackTrace();
}

// 多Sheet数据导出
try (FileOutputStream fos = new FileOutputStream(new File("multi_sheet_data.xlsx"))) {
    ExcelWriter.newInstance().addData(users).addData(holidayCfgs).write(fos);
    //ExcelWriter.newInstance().addData(users, holidayCfgs).write(fos);
} catch (IOException e) {
    e.printStackTrace();
}

````


#### Sheet分页

```` java
// 每个Sheet页50条数据
@ExcelExport(sheetName = "用户数据", maxRowsPerSheet = 50)
public class UserPaging {
    
    ...
}
````

#### 自定义单元格样式

```` java
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

    ....
}

````

#### 自定义行高、列宽

```` java
// 指定标题行高度、数据行高度
@ExcelExport(sheetName = "用户数据", titleRowHeight = 25, dataRowHeight = 20)
public class UserWH {

    // 自动宽度
    @ExcelExportColumn(colIndex = 1, title = "姓名", autoWidth = true)
    private String name;

    // 指定宽度
    @ExcelExportColumn(colIndex = 2, title = "年龄", colWidth = 20)
    private Integer age;

    ....
｝

````

#### 自定义复杂表头

```` java
@ExcelComplexHeader({
        @CellRange(firstCol = 1, lastCol = 4, firstRow = 1, lastRow = 2, title = "用户数据", cellStyleBuilder = LightGreenTitleCellStyleBuilder.class),
        @CellRange(firstRow = 3, lastRow = 3, firstCol = 1, lastCol = 2, title = "基本信息", cellStyleBuilder = LightGreenTitleCellStyleBuilder.class),
        @CellRange(firstRow = 3, lastRow = 3, firstCol = 3, lastCol = 4, title = "扩展信息", cellStyleBuilder = LightGreenTitleCellStyleBuilder.class)})
@ExcelExport(sheetName = "用户数据")
public class UserComplexHeader {
    ....
}

````

----
#### 导入

```` java
InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.xlsx");
ExcelReader excelReader = new ExcelReader(is);
List<HolidayCfg> holidayCfgs = excelReader.read(HolidayCfg.class);
````

```` java
@ExcelImport(sheetIndex = 1, firstDataRow = 2)
public class HolidayCfg {

    @ExcelImportColumn(colIndex = 1, dateFormat = "yyyy-MM-dd", allowBlank = false)
    private Date holidayDate;

    @ExcelImportColumn(colIndex = 2, allowBlank = false)
    private String holidayName;

    @ExcelKVConvert(kvmap = {"是=0", "否=1"})
    @ExcelImportColumn(colIndex = 3, allowBlank = false)
    private String isWork;

    @ExcelImportColumn(colIndex = 4)
    private String remark;
   
    ....
}

````

