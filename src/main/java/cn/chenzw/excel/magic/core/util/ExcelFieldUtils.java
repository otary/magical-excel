package cn.chenzw.excel.magic.core.util;


import cn.chenzw.excel.magic.core.meta.model.ExcelCellDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;
import cn.chenzw.toolkit.commons.ConvertExtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * @author chenzw
 */
public class ExcelFieldUtils {

    private static final String[] TRY_DATE_FORMAT_LIST = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss"};

    public static void setCellValue(Cell cell, Object o, Field field) throws IllegalAccessException {
        Object value = field.get(o);
        if (value == null) {
            return;
        }
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Short) {
            cell.setCellValue((Short) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            throw new UnsupportedOperationException("不支持此数据类型 => [" + field.getType() + "]!");
        }
    }

    public static String getCellValue(ExcelRowDefinition row, int colIndex) {
        for (ExcelCellDefinition cell : row.getExcelCells()) {
            if (cell.getColIndex() == colIndex) {
                return cell.getCellValue();
            }
        }
        return "";
    }

    /**
     * 给字段赋值
     *
     * @param field
     * @param o
     * @param cellValue
     * @param dateFormat
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Field field, Object o, Object cellValue, String dateFormat)
            throws IllegalAccessException {

        if (field.getType() == String.class) {
            if (!StringUtils.isBlank(dateFormat)) {
                try {
                    field.set(o, DateUtils.parseDate((String) cellValue, dateFormat));
                } catch (ParseException e) {
                }
            }
        }
        field.set(o, ConvertExtUtils.convert(field.getType(), cellValue));

        /*if (field.getType() == String.class) {
            if (!StringUtils.isBlank(dateFormat)) {
                Date date = tryParseDate((String) cellValue, dateFormat);
                field.set(o, DateFormatUtils.format(date, dateFormat));
            } else {
                field.set(o, cellValue);
            }
        } else if (field.getType() == Long.class) {
            field.set(o, cellValue);
        } else if (field.getType() == Integer.class) {
            if (cellValue instanceof String) {
                if (((String) cellValue).contains(".")) {
                    field.set(o, Double.valueOf((String) cellValue).intValue());
                } else {
                    field.set(o, Integer.valueOf((String) cellValue));
                }
            } else {
                field.set(o, cellValue);
            }
        } else if (field.getType() == Date.class) {
            if (cellValue instanceof String) {
                field.set(o, tryParseDate((String) cellValue, ExcelConstants.DEFAULT_DATE_FORMAT));
            } else if (cellValue instanceof Date) {
                field.set(o, cellValue);
            } else {
                throw new ExcelException("字段[" + field.getName() + "]不支持[" + cellValue.getClass() + "]类型!");
            }
        } else if (field.getType() == Float.class) {
            field.set(o, cellValue);
        } else if (field.getType() == BigDecimal.class) {
            field.set(o, cellValue);
        } else if (field.getType() == Double.class) {
            field.set(o, Double.valueOf((String) cellValue));
        } else {
            field.set(o, cellValue);
        }*/
    }


   /* private static Date tryParseDate(String dateValue, String tryDateFormat) throws ParseException {
        if (!StringUtils.isBlank(tryDateFormat)) {
            try {
                return DateUtils.parseDate(dateValue, tryDateFormat);
            } catch (ParseException e) {
                for (String dateFormat : TRY_DATE_FORMAT_LIST) {
                    try {
                        return DateUtils.parseDate(dateValue, dateFormat);
                    } catch (ParseException e1) {
                    }
                }
            }
        }
        throw new ParseException("日期转换失败!", 0);
    }*/

}
