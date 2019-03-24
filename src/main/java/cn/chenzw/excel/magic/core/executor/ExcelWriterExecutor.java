package cn.chenzw.excel.magic.core.executor;


import cn.chenzw.excel.magic.core.context.ExcelWriterContext;
import cn.chenzw.excel.magic.core.exception.ExcelWriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenzw
 */
public class ExcelWriterExecutor extends AbstractExcelWriterExecutor {

    private Logger logger = LoggerFactory.getLogger(ExcelWriterExecutor.class);


    public ExcelWriterExecutor(ExcelWriterContext writerContext) {
        super(writerContext);
    }


    @Override
    public void beforeCallback() {
        super.beforeCallback();
    }

    @Override
    public void sheetPaging() {
        logger.debug("start sheet paging!");
        long startTimeMillis = System.currentTimeMillis();
        try {
            super.sheetPaging();
        } catch (Throwable e) {
            throw new ExcelWriterException(this.curSheetIndex, this.curRowIndex, this.curColIndex, "", "Sheet分页失败", e);
        } finally {
            logger.debug("finish sheet paging! [cost:{}ms]", (System.currentTimeMillis() - startTimeMillis));
        }

    }

    @Override
    public void handleComplexHeader() {
        logger.debug("start handle complex header!");
        long startTimeMillis = System.currentTimeMillis();
        try {
            super.handleComplexHeader();
        } catch (Throwable e) {
            throw new ExcelWriterException(this.curSheetIndex, this.curRowIndex, this.curColIndex, "", "复杂表头生成失败!", e);
        } finally {
            logger.debug("finish handle complex header! [cost:{}ms]", (System.currentTimeMillis() - startTimeMillis));
        }

    }

    @Override
    public void addDataValidation() {
        logger.debug("start add data validation!");
        long startTimeMillis = System.currentTimeMillis();
        try {
            super.addDataValidation();
        } catch (Throwable e) {
            throw new ExcelWriterException(this.curSheetIndex, this.curRowIndex, this.curColIndex, "", "添加单元格校验器失败",
                    e);
        } finally {
            logger.debug("finish add data validation! [cost:{}ms]", (System.currentTimeMillis() - startTimeMillis));
        }
    }

    @Override
    public void initHeadTitle() {
        logger.info("start write head title!");
        long startTimeMillis = System.currentTimeMillis();
        try {
            super.initHeadTitle();
        } catch (Throwable e) {
            throw new ExcelWriterException(this.curSheetIndex, this.curRowIndex, this.curColIndex, "", "初始化标题失败", e);
        } finally {
            logger.debug("finish write head title! [cost:{}ms]", (System.currentTimeMillis() - startTimeMillis));
        }
    }

    @Override
    public void initData() {
        logger.debug("start write data!");
        long startTimeMillis = System.currentTimeMillis();
        try {
            super.initData();
        } catch (Throwable e) {
            throw new ExcelWriterException(this.curSheetIndex, this.curRowIndex, this.curColIndex, "", "写入数据失败", e);
        } finally {
            logger.debug("finish write data! [cost:{}ms]", (System.currentTimeMillis() - startTimeMillis));
        }
    }

    @Override
    public void afterCallback() {
        super.afterCallback();
    }
}
