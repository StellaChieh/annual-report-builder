package cwb.cmt.surface.process;

import static cwb.cmt.surface.utils.ConvertFileName.encodeFilename;
import static cwb.cmt.surface.utils.NumberConvert.expand2Strings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.util.SmartPdfSplitter;

import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.model.ClimaticElement;
import cwb.cmt.surface.model.MeanStationValues;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.tools.CreateTableImageInterface;
import cwb.cmt.surface.utils.Month;
import cwb.cmt.surface.utils.NumberConvert;
import cwb.cmt.surface.utils.PrepareAuxCliSum;
import cwb.cmt.surface.utils.PrepareCe;
import cwb.cmt.surface.utils.PrepareCliSum;
import cwb.cmt.surface.utils.PrepareStation;
import cwb.cmt.surface.utils.ReportFileUtils;


public abstract class Process {

	protected List<Station> stnList;
	protected List<AuxCliSum> radList;
	protected List<AuxCliSum> globalradList;
	protected List<AuxCliSum> radmaxList;
	protected List<Station> otherStnsList;
	
	protected List<CliSum> monthDataList;
	protected List<CliSum> hourAvgList;
	protected List<CliSum> dayAvgList;
	protected List<CliSum> hourSumList;
	protected List<CliSum> precpDays_10mmList;
	protected List<CliSum> precpDays_1mmList;
	protected List<CliSum> precpDays_01mmList;
	
	protected List<CliSum> weatherConditionList;
	protected List<CliSum> txMinAbs_LE0List;
	protected List<CliSum> txMinAbs_LE10List;
	protected List<CliSum> txMinAbs_GE20List;
	protected List<CliSum> txMaxAbs_GE25List;
	protected List<CliSum> txMaxAbs_GE30List;
	protected List<CliSum> txMaxAbs_GE35List;
	
	protected List<CliSum> precpDayList;
	protected List<CliSum> hazeDayList;
	protected List<CliSum> hailDayList;
	protected List<CliSum> dewDayList;
	protected List<CliSum> snowDateList;
	protected List<CliSum> frostDateList;
	
	protected List<ClimaticElement> ceXmlList;
	protected  List<MeanStationValues> ceList;
	
	private CreateTableImageInterface mTableImageCreator;
	
	
	@Resource(name="prepareStation")
	PrepareStation prepareStation;
	
	@Resource(name="prepareAuxCliSum")
	PrepareAuxCliSum prepareAuxCliSum;
	
	@Resource(name="prepareCliSum")
	PrepareCliSum prepareCliSum;
	
	@Resource(name="prepareCe")
	PrepareCe prepareCe;
    //Time: year
    @Resource(name="year")
    protected int year;
    
	// root path
	@Resource(name="rootPath")
	protected String rootPath;
	
	// tmp folder
	protected String outputTmpPdfPath;
	@Resource(name="outputTmpPdfPath")
	
	
	
	//set output pdf path 
	public void setOutputTmpPdfPath(String outputTmpPdfPath) throws FileNotFoundException {
		if(Files.exists(Paths.get(rootPath, outputTmpPdfPath), LinkOption.NOFOLLOW_LINKS)){
			
			this.outputTmpPdfPath = Paths.get(rootPath, outputTmpPdfPath).toString();
//			System.out.println(this.outputTmpPdfPath);
		}else {
			throw new FileNotFoundException("output pdf temp path not found.");
		}
	}
	
	public abstract void run() throws IOException;

	
	protected void prepareConfig_Stns(){
		// get stations
		stnList = prepareStation.getStation();
	}
	
	protected void prepareConfig_Rad(){
		// get stations
		stnList = prepareStation.getStation();
		// get AuxCliSum: radiation and sunshine
		radList = prepareAuxCliSum.getRadiation(stnList);
	}
	protected void prepareConfig_GlobalRad(){
		// get stations
		stnList = prepareStation.getStation();
		//get AuxCliSum: Global Solar Radiation
		globalradList = prepareAuxCliSum.getGlobalRad(stnList);
	}
	
	protected void prepareConfig_RadHrMax(){
		// get stations
		stnList = prepareStation.getStation();
		//get AuxCliSum: Global Solar Radiation Max 
		radmaxList = prepareAuxCliSum.getRadHrMax(stnList);
	}
	
	protected void prepareConfig_otherStns(){
		//get List of Weather Station Number of Other Organization 
		otherStnsList = prepareStation.getOtherStation();
	}
	
	protected void prepareConfig_CliSum(){
		// get stations
		stnList = prepareStation.getStation();
		//get clisum month data
		monthDataList = prepareCliSum.getMonthData(stnList);
		//get hour avg: hour>> Tx, Rh, CAmtTotal
		hourAvgList = prepareCliSum.getHourAvg(stnList);
		//get day avg: day>> TxMaxAbs, TxMinAbs
		dayAvgList = prepareCliSum.getDayAvg(stnList);
		//get hour sum: hour>> PrecpHour
		hourSumList = prepareCliSum.getHourSUM(stnList);
		//get PrecpDays_10mm
		precpDays_10mmList = prepareCliSum.getPrecpDays_10mm(stnList);
		//get PrecpDays_1mm
		precpDays_1mmList = prepareCliSum.getPrecpDays_1mm(stnList);
		//get PrecpDays_01mm
		precpDays_01mmList = prepareCliSum.getPrecpDays_01mm(stnList);
		
		//get weather condition
		weatherConditionList = prepareCliSum.getWeatherCondition(stnList);
		txMinAbs_LE0List = prepareCliSum.getTxMinAbs_LE0(stnList);
		txMinAbs_LE10List = prepareCliSum.getTxMinAbs_LE10(stnList);
		txMinAbs_GE20List = prepareCliSum.getTxMinAbs_GE20(stnList);
		txMaxAbs_GE25List = prepareCliSum.getTxMaxAbs_GE25(stnList);
		txMaxAbs_GE30List = prepareCliSum.getTxMaxAbs_GE30(stnList);
		txMaxAbs_GE35List = prepareCliSum.getTxMaxAbs_GE35(stnList);
		
		precpDayList = prepareCliSum.getPrecpDay(stnList);
		hazeDayList = prepareCliSum.getHazeDay(stnList);
		hailDayList = prepareCliSum.getHailDay(stnList);
		dewDayList = prepareCliSum.getDewDay(stnList);
		snowDateList = prepareCliSum.getSnowDate(stnList);
		frostDateList = prepareCliSum.getFrostDate(stnList);
	}
	
	protected void prepareConfig_CE(){
		//2.3-2.20: climatic element
		ceXmlList = prepareCe.getCeXml();
		ceList = prepareCe.getMeanStationValues();
	}
}
