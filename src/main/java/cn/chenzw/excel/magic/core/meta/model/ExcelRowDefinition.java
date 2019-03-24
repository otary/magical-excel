package cn.chenzw.excel.magic.core.meta.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzw
 */
public class ExcelRowDefinition {

    private Integer sheetIndex;
    private Integer rowIndex;
    private List<ExcelCellDefinition> excelCells = new ArrayList<>();

    public void addExceCell(ExcelCellDefinition excelCell) {
        this.excelCells.add(excelCell);
    }

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

    public List<ExcelCellDefinition> getExcelCells() {
        return excelCells;
    }

    public void setExcelCells(List<ExcelCellDefinition> excelCells) {
        this.excelCells = excelCells;
    }

    @Override
    public String toString() {
        return "ExcelRowDefinition{" + "sheetIndex=" + sheetIndex + ", rowIndex=" + rowIndex + ", excelCells="
                + excelCells + '}';
    }
}
