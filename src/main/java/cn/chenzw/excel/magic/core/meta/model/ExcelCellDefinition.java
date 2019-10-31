package cn.chenzw.excel.magic.core.meta.model;

import cn.chenzw.excel.magic.core.constants.ExcelConstants;
import cn.chenzw.excel.magic.core.util.ExcelXmlCodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;

import java.util.Date;

/**
 * @author chenzw
 */
public class ExcelCellDefinition {

    private Integer sheetIndex;
    private Integer rowIndex;
    private Integer colIndex;
    private String colTitle;
    private String cellValue;
    private ExcelCellType cellType;

    public Integer getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public void setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
    }

    public String getColTitle() {
        return colTitle;
    }

    public void setColTitle(String colTitle) {
        this.colTitle = colTitle;
    }

    public String getCellValue() {
        return cellValue;
    }

    public void setCellValue(String cellValue) {
        this.cellValue = cellValue;
    }

    public ExcelCellType getCellType() {
        return cellType;
    }

    public void setCellType(ExcelCellType cellType) {
        this.cellType = cellType;
    }

    @Override
    public String toString() {
        return "ExcelCell{" + "sheetIndex=" + sheetIndex + ", rowIndex=" + rowIndex + ", colIndex=" + colIndex
                + ", colTitle='" + colTitle + '\'' + ", cellValue='" + cellValue + '\'' + ", cellType='" + cellType
                + '\'' + '}';
    }

    /**
     * 单元格格式
     * @author chenzw
     */
    public interface ExcelCellType {

        boolean matches(String name, Attributes attributes);

        String getValue(String value);
    }

    /**
     * 共享字符串格式
     */
    public static class ExcelStringCellType implements ExcelCellType {

        private SharedStringsTable sst;

        public ExcelStringCellType(SharedStringsTable sst) {
            this.sst = sst;
        }

        @Override
        public boolean matches(String name, Attributes attributes) {

            if (ExcelConstants.CELL_TAG.equals(name)) {
                // 字符串类型 t="s"
                if (ExcelConstants.CELL_STRING_TYPE.equals(attributes.getValue(ExcelConstants.CELL_TYPE_ATTR))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getValue(String value) {
            int idx = Integer.parseInt(value);
            return new XSSFRichTextString(this.sst.getEntryAt(idx)).toString();
        }
    }


    /**
     * 日期格式
     */
    public static class ExcelDateCellType implements ExcelCellType {

        private StylesTable stylesTable;

        public ExcelDateCellType(StylesTable stylesTable) {
            this.stylesTable = stylesTable;
        }

        @Override
        public boolean matches(String name, Attributes attributes) {

            if (ExcelConstants.CELL_TAG.equals(name)) {
                if (!StringUtils.isBlank(attributes.getValue(ExcelConstants.CELL_STYLE_ATTR)) ) {
                    int styleIndex = Integer.parseInt(attributes.getValue(ExcelConstants.CELL_STYLE_ATTR));
                    XSSFCellStyle cellStyle = this.stylesTable.getStyleAt(styleIndex);
                    short dataFormatIndex = cellStyle.getDataFormat();
                    String dataFormatString = cellStyle.getDataFormatString();
                    if (StringUtils.containsAny(dataFormatString, "y", "m", "d", "h", "s", "Y", "M","D")) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String getValue(String value) {
            Date date = HSSFDateUtil.getJavaDate(Double.parseDouble(value));
            return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * 数值类型
     */
    public static class ExcelNumbericCellType implements ExcelCellType {

        private StylesTable stylesTable;

        public ExcelNumbericCellType(StylesTable stylesTable) {
            this.stylesTable = stylesTable;
        }

        @Override
        public boolean matches(String name, Attributes attributes) {
            if (ExcelConstants.CELL_TAG.equals(name)) {
                // 非字符串
                if (!StringUtils.isBlank(attributes.getValue(ExcelConstants.CELL_STYLE_ATTR))) {

                    String dataFormat = ExcelXmlCodecUtils
                            .getDataFormat(Integer.parseInt(attributes.getValue(ExcelConstants.CELL_STYLE_ATTR)),
                                    this.stylesTable);
                    if (StringUtils.containsAny(dataFormat, "#", "0", "General")) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String getValue(String value) {
            return value;
        }
    }

    /**
     * 内联字符串（不使用共享池）
     */
    public static class ExcelInlinStrCellType implements ExcelCellType {

        @Override
        public boolean matches(String name, Attributes attributes) {

            if (ExcelConstants.CELL_TAG.equals(name)) {
                // t="inlinStr"
                if (ExcelConstants.CELL_INLINE_STR_TYPE.equals(attributes.getValue(ExcelConstants.CELL_TYPE_ATTR))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getValue(String value) {
            XSSFRichTextString rtsi = new XSSFRichTextString(value);
            return rtsi.toString();
        }
    }


    /**
     * 布尔值类型（1=true; 0=false）
     */
    public static class ExcelBooleanCellType implements ExcelCellType {

        @Override
        public boolean matches(String name, Attributes attributes) {
            if (ExcelConstants.CELL_TAG.equals(name)) {
                // t = "b"
                if (ExcelConstants.CELL_BOOLEAN_TYPE.equals(attributes.getValue(ExcelConstants.CELL_TYPE_ATTR))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getValue(String value) {
            return value;
        }
    }


    /**
     * 错误类型
     */
    public static class ExcelErrorCellType implements ExcelCellType {

        @Override
        public boolean matches(String name, Attributes attributes) {
            if (ExcelConstants.CELL_TAG.equals(name)) {
                // t = "e"
                if (ExcelConstants.CELL_ERROR_TYPE.equals(attributes.getValue(ExcelConstants.CELL_TYPE_ATTR))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getValue(String value) {
            return value;
        }
    }


    public static class ExcelNullCellType implements ExcelCellType {

        @Override
        public boolean matches(String name, Attributes attributes) {
            return true;
        }

        @Override
        public String getValue(String value) {
            return value;
        }
    }


}
