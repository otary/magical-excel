package cn.chenzw.excel.magic.core.exception;

/**
 * 字段值转换异常
 *
 * @author chenzw
 */
public class ExcelConvertException extends ExcelException {

    public ExcelConvertException() {
        super();
    }

    public ExcelConvertException(String message) {
        super(message);
    }

    public ExcelConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelConvertException(Throwable cause) {
        super(cause);
    }

    protected ExcelConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
