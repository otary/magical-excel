package cn.chenzw.excel.magic.core.analysis;

import cn.chenzw.excel.magic.core.constants.ExcelConstants;
import cn.chenzw.excel.magic.core.meta.model.ExcelCellDefinition;
import cn.chenzw.excel.magic.core.meta.model.ExcelRowDefinition;
import cn.chenzw.excel.magic.core.processor.ExcelPerRowProcessor;
import cn.chenzw.excel.magic.core.util.ExcelXmlCodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzw
 */
public class XlsxAnalysisHandler extends DefaultHandler {

    private StylesTable stylesTable;
    private SharedStringsTable sst;
    private ExcelPerRowProcessor perRowProcessor;
    private int totalRow = 0;
    private String tagValue;
    private ExcelRowDefinition curExcelRow;
    private ExcelCellDefinition curExcelCell;
    private List<ExcelCellDefinition.ExcelCellType> excelCellTypes = new ArrayList<>();

    public XlsxAnalysisHandler(StylesTable stylesTable, SharedStringsTable sst, ExcelPerRowProcessor perRowProcessor) {
        this.sst = sst;
        this.stylesTable = stylesTable;
        this.perRowProcessor = perRowProcessor;

        registerExcelCellTypes();
    }

    /**
     * 注册单元格类型列表
     */
    private void registerExcelCellTypes() {
        this.excelCellTypes.add(new ExcelCellDefinition.ExcelStringCellType(this.sst));
        this.excelCellTypes.add(new ExcelCellDefinition.ExcelNumbericCellType(this.stylesTable));
        this.excelCellTypes.add(new ExcelCellDefinition.ExcelDateCellType(this.stylesTable));
        this.excelCellTypes.add(new ExcelCellDefinition.ExcelBooleanCellType());
        this.excelCellTypes.add(new ExcelCellDefinition.ExcelInlinStrCellType());
        this.excelCellTypes.add(new ExcelCellDefinition.ExcelErrorCellType());
        this.excelCellTypes.add(new ExcelCellDefinition.ExcelNullCellType());
    }


    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

        // 总行数
        if (ExcelConstants.DIMENSION_TAG.equals(name)) {
            String refAttr = attributes.getValue(ExcelConstants.DIMENSION_REF_ATTR);
            this.totalRow = ExcelXmlCodecUtils.getTotalRow(refAttr);
            return;
        }

        // 行
        if (ExcelConstants.ROW_TAG.equals(name)) {
            this.curExcelRow = new ExcelRowDefinition();
            this.curExcelRow.setRowIndex(Integer.valueOf(attributes.getValue(ExcelConstants.ROW_INDEX_ATTR)));
        }

        // 单元格
        if (ExcelConstants.CELL_TAG.equals(name)) {
            this.curExcelCell = new ExcelCellDefinition();
            String abcColIndex = attributes.getValue(ExcelConstants.CELL_ABC_INDEX_ATTR);
            this.curExcelCell.setColIndex(ExcelXmlCodecUtils.getColIndex(abcColIndex));
            this.curExcelCell.setRowIndex(this.curExcelRow.getRowIndex());

            // 设置单元格处理器
            for (ExcelCellDefinition.ExcelCellType excelCellType : this.excelCellTypes) {
                if (excelCellType.matches(name, attributes)) {
                    this.curExcelCell.setCellType(excelCellType);
                    break;
                }
            }
        }
        this.tagValue = "";

    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {

        if (ExcelConstants.DIMENSION_TAG.equals(name)) {
            this.perRowProcessor.processTotalRow(this.totalRow);
        }

        // 行
        if (ExcelConstants.ROW_TAG.equals(name)) {
            this.perRowProcessor.processPerRow(this.curExcelRow);
        }

        // 单元格
        if (ExcelConstants.CELL_TAG.equals(name)) {
            this.curExcelCell.setCellValue(StringUtils.trim(curExcelCell.getCellType().getValue(tagValue)));
            this.curExcelRow.addExceCell(this.curExcelCell);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        this.tagValue += new String(ch, start, length);
    }


}
