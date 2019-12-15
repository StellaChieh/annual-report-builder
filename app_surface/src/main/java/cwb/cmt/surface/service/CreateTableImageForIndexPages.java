package cwb.cmt.surface.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import ChartDirector.BaseChart;
import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.TextBox;
import ChartDirector.XYChart;
import cwb.cmt.surface.utils.ReportFileInfo;
import cwb.cmt.surface.utils.ReportFileInfoNew;
import cwb.cmt.surface.utils.RomanNumber;
import static cwb.cmt.surface.utils.NumberConvert.ROUND_CEILING;
import static cwb.cmt.surface.utils.NumberConvert.repeatChar;
import static cwb.cmt.surface.utils.NumberConvert.repeatString;
import static cwb.cmt.surface.utils.NumberConvert.digit2ChineseString;
import static cwb.cmt.surface.utils.NumberConvert.insertSpaceWithinCharacters;
import static cwb.cmt.surface.utils.NumberConvert.expand2Strings;
import static cwb.cmt.surface.utils.NumberConvert.expand2StringsExcludingNull;
import static cwb.cmt.surface.utils.NumberConvert.round;
import static java.util.Arrays.copyOfRange;
import static java.lang.String.join;

@Service("createTableImageForIndexPages")
public class CreateTableImageForIndexPages extends CreateTableImageIndexPage{
    private static final int ChartMarginTop = 12;
    private static final int ChartWidth = 2100;
    private static final int ChartHeight = 2970;
    
    private static final int PageTitleTextSize = 39;
    private static final int PageSubtitleTextSize = 35;
    private final int TableCellTextSize = 25;
    private final int TableCellHorizontalMargin = 6;
    private final int TABLE_CELL_ALIGNMENT_LEFT_CENTER_VERTICAL = 4;
    private final int TableCellVerticalMargin =7;
    
    private static final String FontfaceTitle = "mingliu.ttc";
    private static final String FontfaceContent = "kaiu.ttf";
    
    public CreateTableImageForIndexPages() {
        setMaxPageLineCount(46);
    }

    @SuppressWarnings("unchecked")
	protected BaseChart createChart(int pageNumber, Object... args) {
        return createChart(pageNumber, (int)args[0], (List<ReportFileInfo>)args[1]);
    }
    
    private BaseChart createChart(int pageNumber, int numberOfPages, List<ReportFileInfo> data) {
        
        Chart.setLicenseCode("DEVP-2LPQ-D6YH-UTLK-EBC0-3E26");
        
        XYChart chart = new XYChart(ChartWidth, ChartHeight);
        
        if (pageNumber == 1) {
            TextBox title = chart.addTitle(
                "PART  I  -  SURFACE DATA",
                FontfaceTitle,
                PageTitleTextSize
            );
            title.setPos(title.getLeftX(), title.getTopY()+60);
            
            TextBox subtitle = chart.addTitle(
                "目   錄   Contents",
                FontfaceTitle,
                PageSubtitleTextSize
            );
            subtitle.setPos(subtitle.getLeftX(), title.getTopY() + 77);
        }
        
        int table_marginTop = (pageNumber <= 1) ? 265 : 75;
        CDMLTable table = createTable(
                chart,
                0,
                table_marginTop,
                pageNumber,
                numberOfPages,
                data
        );
        table.setPos((chart.getWidth() - table.getWidth()) / 2 + 16, table_marginTop);    
        
        return chart;
    }
    
    private CDMLTable createTable(XYChart chart,
                                  int posX,
                                  int posY,
                                  int pageNumber,
                                  int numberOfPages,
                                  List<ReportFileInfo> list) {
        // Create a CDMLTable
        CDMLTable table = chart.addTable(
            posX, posY, 0,
            1,
            0
        );
        
        // Set cell's display attributes
        TextBox cellStyle = table.getStyle();
        cellStyle.setMargin(
            0, // left
            0, // right
            TableCellVerticalMargin + 2,   // top
            TableCellVerticalMargin + ((pageNumber == 1)? 3:2)    // bottom
        );
        cellStyle.setFontStyle(FontfaceContent);
        cellStyle.setFontSize(TableCellTextSize);
        cellStyle.setAlignment(TABLE_CELL_ALIGNMENT_LEFT_CENTER_VERTICAL);
        cellStyle.setBackground(0x00ffffff, 0xff000000);
        
        StringBuilder text = new StringBuilder();
        for (int row=0; row<list.size(); row++) {
            // get the n-th element
            final ReportFileInfoNew data = (ReportFileInfoNew) list.get(row);
            // newline
            boolean newlineBefore = (data.indexes.length == 2 && data.indexes[0] > 1 && data.indexes[1] == 0);
            boolean newlineWithin = (data.indexes.length == 3 && data.indexes[0] == 1 && data.indexes[1] == 4 && data.indexes[2] > 1);
            if (newlineBefore && row > 0) {
                text.append('\n');
            }
            // indentation
            int intentLevel = 0;
            if (data.indexes[0] > 0 && data.indexes[1] > 0) {
                intentLevel = (data.table == -1)? 1 : 2;
            }

            // text (chinese and english)
            if (data.table != -1) {
                int digitChineseStrLen = digit2ChineseString(data.table).length();
                String digitChineseString = ((digitChineseString = digit2ChineseString(data.table)).length() == 1)
                                             ?  "  " + digitChineseString + "  "
                                             :  (digitChineseStrLen <= 2)
                                                 ? " " + digitChineseString + " "
                                                 : digitChineseString;
                String chineseStation = (data.textChinese.length() > 2)
                                         ? data.textChinese
                                         : insertSpaceWithinCharacters(data.textChinese, "  ");
                text.append("第" + digitChineseString + "表" + "   " + chineseStation + "    " +
                            "Table " + data.table + "   " + data.textEnglish);
            }
            else {
                if (data.indexes[0] > 0) {
                    Integer[] indexes = (data.indexes.length == 2 && data.indexes[1] == 0)
                                         ? copyOfRange(data.Indexes, 0, 1)
                                         : data.Indexes;
                    text.append(join(".", expand2Strings(indexes)) + ((indexes.length == 1)? ".":"") + " ");
                }
                text.append(join((newlineWithin)? "\n  " : " ", expand2StringsExcludingNull(data.textChinese, data.textEnglish)));
            }            
            // insert indent
            text.insert(0, repeatString("  ", intentLevel));
            
            // page number
            int chapterValue = data.indexes[0] * 1000 + ((data.indexes.length >1)? data.indexes[1] : 0);
            int countOffset = (chapterValue > 1000 && chapterValue < 1003)? numberOfPages : 0;
            int pageNumValue = data.pageNum + countOffset;
            String pageNumberText = (pageNumValue < 0)
                                     ? ""
                                     : (chapterValue < 1003)
                                       ? RomanNumber.toRoman(pageNumValue).toLowerCase()
                                       : String.format("%s", pageNumValue);
            if (!(data.pageNum != -1 && data.indexes[data.indexes.length-1] != 0)) {
                pageNumberText = "";
            }            
            
            // append a new row
            table.appendRow();
            
            // text + '.' x N + pageNumber
            // count how many '.' are needed to fill the space between
            // the text (chinese and english text) and the page number.
            int c = 0;
            if (data.pageNum != -1 && data.indexes.length > 1 && data.indexes[data.indexes.length-1] != 0) {
                while (table.getCell(0, row).getWidth() < 1600) {
                    table.setCell(0, row, 1, 1, text.toString() + " " + repeatChar('.', ++c) + pageNumberText);
                    table.layout();
                }                
            }
            table.setCell(0, row, 1, 1, text.toString() + " " + repeatChar('.', c) + pageNumberText);
            // clean string buffer
            text.setLength(0);
        }
        return table;
    }
    
    public void createTableImage(List<ReportFileInfo> data) {
        if (data.size() > getMaxPageLineCount())
//        	System.out.println(data.size()+"getMaxPageLineCount()>> "+getMaxPageLineCount);
            setFilename(getFilename() + "#%d");
       
        int begin = 0, end = 0;
        int pageElementCount = 0;
        final int tocPageCount = (int) round((float) data.size() / getMaxPageLineCount(), 0, ROUND_CEILING);
        for (int i=0, r=0, page=1; i<tocPageCount; i++, page++, r+=pageElementCount) {
//        for (int i=0, r=0, page=1; i<46; i++, page++, r+=pageElementCount) {
            if (page == 1) {
                setMaxPageLineCount(46);
            }
            else {
                setMaxPageLineCount(49);  
            }
            pageElementCount = (r+getMaxPageLineCount() < data.size())
                               ? getMaxPageLineCount()
                               : data.size() - end;     
            begin = end;
            end = r + pageElementCount;
            List<ReportFileInfo> pageData = data.subList(begin, end); 
            if (pageData.isEmpty()) {
                return;
            }
            setFilename(String.format(getFilename(), i+1));
            super.createTableImage(page, tocPageCount, pageData);
//            super.createTableImage(page, 46, pageData);
            setFilename(getFilename().replace("#" + (i+1), "#%d"));
        }
    }

}
