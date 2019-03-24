## mgical-excel：基于注解的Excel导入导出开源组件

## 环境配置

#### maven配置

````
<dependency>
	<groupId>cn.chenzw.excel</groupId>
    <artifactId>magical-excel</artifactId>
    <version>1.0</version>
</dependency>
````


#### 依赖
````
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
 
 #### JDK
 - JDK1.7+
 
 ---
  
  
  
## 核心注解

#### 导出

+ **@ExcelExport**
	+ order:顺序（值越小，越靠前）
	+ sheetName：Sheet页名称
	+ maxRowsPerSheet：每个Sheet页允许的最大条数（超过将进行分Sheet）
	+ rowStriped: 是否条纹显示
	+ titleRowHeight: 标题行高度 
	+ dataRowHeight：数据行高度

+ **@ExcelExportColumn**：
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

+ **@ExcelImport**：
	+ sheetIndex：绑定的sheet页（可多个）
	+ firstDataRow：起始数据行（默认:1）

+ **@ExcelImportColumn**：
	+ colIndex：列索引（起始值:1）
	+ allowBlank:是否允许为空（默认: true）
	+ dateFormat：日期格式

## 应用场景

#### 模版导出

````
// 单sheet
try (FileOutputStream fos = new FileOutputStream(new File("template_by_model.xlsx"))) {
   ExcelWriter.newTemplateInstance(HolidayCfg.class).write(fos);
} catch (IOException e) {
   e.printStackTrace();
}

// 多Sheet
try (FileOutputStream fos = new FileOutputStream(new File("template_by_models.xlsx"))) {
   ExcelWriter.newTemplateInstance(HolidayCfg.class, User.class).write(fos);
} catch (IOException e) {
   e.printStackTrace();
}

````

#### 下载

````
// 

ExcelWriter.newInstance().write(os);


````