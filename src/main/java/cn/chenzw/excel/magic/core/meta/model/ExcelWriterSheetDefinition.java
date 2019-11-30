package cn.chenzw.excel.magic.core.meta.model;


import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.awt.*;
import java.util.List;
import java.util.Map;

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

    Map<Integer, ExcelCellStyleDefinition> getColumnCellStyles(Workbook workbook);


}
