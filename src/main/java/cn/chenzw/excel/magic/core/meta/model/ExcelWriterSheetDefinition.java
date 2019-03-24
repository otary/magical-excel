package cn.chenzw.excel.magic.core.meta.model;


import java.awt.*;
import java.util.List;

public interface ExcelWriterSheetDefinition extends ExcelSheetDefinition {

    List<?> getRowDatas();

    int getOrder();

    String getSheetName();

    void setSheetName(String sheetName);

    int getMaxRowsPerSheet();

    boolean isRowStriped();

    Color getRowStripeColor();

    int getTitleRowHeight();

    int getDataRowHeight();
}
