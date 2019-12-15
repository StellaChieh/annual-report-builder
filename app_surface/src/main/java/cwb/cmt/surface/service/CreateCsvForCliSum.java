package cwb.cmt.surface.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import ChartDirector.TextBox;
import cwb.cmt.surface.model.AuxCliSum;
import cwb.cmt.surface.model.CliSum;
import cwb.cmt.surface.model.Station;
import cwb.cmt.surface.utils.SpecialValue;
import cwb.cmt.surface.utils.StatisticalData;

@Service("createCsvForCliSum")
public class CreateCsvForCliSum {
    @Resource(name="year")
    protected int year;
    @Resource(name="month")
    protected int month;
    @Resource(name="statisticalData")
    StatisticalData statisticalData;
	protected String path;
	protected String filename;
	
	
	private static CellProcessor[] getTable1Processors() {
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                null, 
                null,
                null, 
                null, 
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
        };
        return processors;
	}
	
	private static CellProcessor[] getTable2Processors() {
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                null, 
                null,
                null, 
                null, 
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
        };
        return processors;
	}
	private static CellProcessor[] getTable3Processors() {
        final CellProcessor[] processors = new CellProcessor[] { 
                new NotNull(), 
                new NotNull(), 
                new NotNull(), 
                null, 
                null,
                null, 
                null, 
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
        };
        return processors;
	}
	
	
	
	public void createCsv(Object...objects) throws IOException {
		String StnCName = (String)objects[0];
		String StnEName = (String)objects[1];
		@SuppressWarnings("unchecked")
		List<CliSum> monthDataList = (List<CliSum>) objects[2];
		@SuppressWarnings("unchecked")
		List<CliSum> hourAvgList = (List<CliSum>) objects[3];
		@SuppressWarnings("unchecked")
		List<CliSum> dayAvgList = (List<CliSum>)objects[4];
		@SuppressWarnings("unchecked")
		List<CliSum> hourSumList = (List<CliSum>) objects[5];
		@SuppressWarnings("unchecked")
		List<CliSum> precpDays_10mmList = (List<CliSum>) objects[6];
		@SuppressWarnings("unchecked")
		List<CliSum> precpDays_1mmList = (List<CliSum>) objects[7];
		@SuppressWarnings("unchecked")
		List<CliSum> precpDays_01mmList = (List<CliSum>) objects[8];
		@SuppressWarnings("unchecked")
		List<CliSum> weatherConditionList = (List<CliSum>) objects[9];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_LE0List = (List<CliSum>)objects[10];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_LE10List = (List<CliSum>)objects[11];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_GE20List = (List<CliSum>)objects[12];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_GE25List = (List<CliSum>)objects[13];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_GE30List = (List<CliSum>)objects[14];
		@SuppressWarnings("unchecked")
		List<CliSum> TxMinAbs_GE35List = (List<CliSum>)objects[15];
		
		@SuppressWarnings("unchecked")
		List<CliSum> precpDayList = (List<CliSum>)objects[16];
		@SuppressWarnings("unchecked")
		List<CliSum> hazeDayList = (List<CliSum>)objects[17];
		@SuppressWarnings("unchecked")
		List<CliSum> hailDayList =(List<CliSum>)objects[18];
		@SuppressWarnings("unchecked")
		List<CliSum> dewDayList = (List<CliSum>)objects[19];
		@SuppressWarnings("unchecked")
		List<CliSum> snowDateList = (List<CliSum>)objects[20];
		@SuppressWarnings("unchecked")
		List<CliSum> frostDateList = (List<CliSum>)objects[21];
		ICsvMapWriter mapWriter = (ICsvMapWriter) objects[22];
		String[] table1Header = (String[]) objects[23];
		String[] table2Header = (String[]) objects[24];
		String[] table3Header = (String[]) objects[25];
		int head =  (int) objects[26];
		if(head==0) {
			create1stTable(StnCName, StnEName, monthDataList, hourAvgList, dayAvgList, hourSumList, mapWriter, table1Header);
		}else if(head==1) {
			create2ndTable(StnCName, StnEName, monthDataList, hourAvgList, dayAvgList, hourSumList, precpDays_10mmList, precpDays_1mmList, precpDays_01mmList,
					mapWriter, table2Header);
		}else if(head==2) {
			create3rdTable(StnCName, StnEName, monthDataList, hourAvgList, dayAvgList, weatherConditionList, TxMinAbs_LE0List, TxMinAbs_LE10List,
	        		TxMinAbs_GE20List, TxMinAbs_GE25List, TxMinAbs_GE30List, TxMinAbs_GE35List, precpDayList,
	        		hazeDayList, hailDayList, dewDayList, snowDateList, frostDateList, mapWriter, table3Header);
		}
	}
	
	
	public void create1stTable(String StnCName, String StnEName, List<CliSum> monthDataList, List<CliSum> hourAvgList, List<CliSum> dayAvgList,
            List<CliSum> hourSumList, ICsvMapWriter mapWriter, String []header) throws IOException {
		List<String> specialValueList = Arrays.asList(
				SpecialValue.EquipmentMalfunction.getNumber(), 
				SpecialValue.CeUnknown.getNumber(),
				SpecialValue.NewStation.getNumber(), 
				SpecialValue.CancelStation.getNumber(),
				SpecialValue.Trace.getNumber(),
				SpecialValue.SubstituteZero.getNumber()
				);
		final CellProcessor[] processors = getTable1Processors();
		Map<String, Object> cliSumMap = new HashMap<String, Object>();
		Map<String, Object> annualCliSumMap = new HashMap<String, Object>();
		
		int count = 0;
		int rowRange = (monthDataList.size() < month)? monthDataList.size() : month;// cancel station=6; general=7
        int countRange = (hourAvgList.size() < (month*3)) ? hourAvgList.size() : (month*3);
        
        CliSum annualCliSum  = new CliSum();
        Float sunshineSum = 0.0f;
        Float stnPresSum =  0.0f;
        Float tx5HSum = 0.0f;
        Float tx14HSum = 0.0f;
        Float tx21HSum = 0.0f;
        Float txSum = 0.0f;
        Float txMaxAvg = 0.0f;
        Float txMinAvg = 0.0f;
        List<String> txMaxAbs = new ArrayList<>();
        List<String> txMinAbs = new ArrayList<>();
        List<String> rhMin = new ArrayList<>();
        Map<Float, LocalDateTime> txMaxAbsTime = new HashMap<>();
        Map<Float, LocalDateTime> txMinAbsTime = new HashMap<>();
        Float rh5HSum = 0.0f;
        Float rh14HSum = 0.0f;
        Float rh21HSum = 0.0f;
        Float rhSum = 0.0f;
        Float sunshineRateSum = 0.0f;
        
		cliSumMap.put(header[0], StnCName);
		cliSumMap.put(header[1], StnEName);
		cliSumMap.put(header[2], year);
		for(int r=0; r<rowRange; r++) { // condition: cancel station (r=rowRange=7, loop end)
			if(count<countRange) {
				cliSumMap.put(header[3], monthDataList.get(r).getObsTime().getMonthValue());
				cliSumMap.put(header[4], StringToBigdecimal(monthDataList.get(r).getStnPres()));
				cliSumMap.put(header[5], StringToBigdecimal(hourAvgList.get(count).getTxAvg()));
				cliSumMap.put(header[6], StringToBigdecimal(hourAvgList.get(count+1).getTxAvg()));  // 氣溫 >> 14h
				cliSumMap.put(header[7], StringToBigdecimal(hourAvgList.get(count+2).getTxAvg()));  // 氣溫 >> 21h
		    	cliSumMap.put(header[8], StringToBigdecimal(monthDataList.get(r).getTx()));  // 氣溫 >> 平均 Mean of obs.
		    	cliSumMap.put(header[9], StringToBigdecimal(dayAvgList.get(r).getTxMaxAvg()));  // 氣溫 >> 平均 Mean >> 最高
		    	cliSumMap.put(header[10], StringToBigdecimal(dayAvgList.get(r).getTxMinAvg()));  // 氣溫 >> 平均 Mean >> 最低
		    	cliSumMap.put(header[11], StringToBigdecimal(monthDataList.get(r).getTxMaxAbs())); // 氣溫 >> 絕對 >> 最高
		    	cliSumMap.put(header[12], String.valueOf(monthDataList.get(r).getTxMaxAbsTime().getDayOfMonth())); // 氣溫 >> 絕對 >> 月 日
		    	cliSumMap.put(header[13], StringToBigdecimal(monthDataList.get(r).getTxMinAbs())); // 氣溫 >> 絕對 >> 最低
		    	cliSumMap.put(header[14], String.valueOf(monthDataList.get(r).getTxMinAbsTime().getDayOfMonth())); // 氣溫 >> 絕對 >> 月 日
		    	// relative humidity
		    	cliSumMap.put(header[15], StringToBigdecimal2(hourAvgList.get(count).getRhAvg())); // 相對濕度 >> 5h
		    	cliSumMap.put(header[16], StringToBigdecimal2(hourAvgList.get(count+1).getRhAvg())); // 相對濕度 >> 14h
		    	cliSumMap.put(header[17], StringToBigdecimal2(hourAvgList.get(count+2).getRhAvg())); // 相對濕度 >> 21h
		    	cliSumMap.put(header[18], StringToBigdecimal2(monthDataList.get(r).getRh())); // 相對濕度 >> 平均
		    	cliSumMap.put(header[19], StringToBigdecimal2(monthDataList.get(r).getRhMin())); // 相對濕度 >> 最小
		    	// sunshine
		    	cliSumMap.put(header[20], StringToBigdecimal(monthDataList.get(r).getSunshine())); // 日照時數 >> 總計
		    	cliSumMap.put(header[21], StringToBigdecimal(monthDataList.get(r).getSunshineRate())); // 相對濕度 >> 百分%
		    	
		    	//sum sunshine; return float value
		    	stnPresSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getStnPres()));
		    	tx5HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count).getTxAvg()));
		    	tx14HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+1).getTxAvg()));
		    	tx21HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+2).getTxAvg()));
		    	txSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getTx()));
		    	txMaxAvg += statisticalData.Sum(StringToBigdecimal(dayAvgList.get(r).getTxMaxAvg()));
		    	txMinAvg += statisticalData.Sum(StringToBigdecimal(dayAvgList.get(r).getTxMinAvg()));
		    	txMaxAbs.add(StringToBigdecimal(monthDataList.get(r).getTxMaxAbs()));
		    	//getTxMaxAbsTime()
		    	txMaxAbsTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getTxMaxAbs())), monthDataList.get(r).getTxMaxAbsTime());
		    	txMinAbs.add(StringToBigdecimal(monthDataList.get(r).getTxMinAbs()));
		    	txMinAbsTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getTxMinAbs())), monthDataList.get(r).getTxMinAbsTime());
		    	
		    	rh5HSum += statisticalData.Sum(StringToBigdecimal2(hourAvgList.get(count).getRhAvg()));
		    	rh14HSum += statisticalData.Sum(StringToBigdecimal2(hourAvgList.get(count+1).getRhAvg()));
		    	rh21HSum += statisticalData.Sum(StringToBigdecimal2(hourAvgList.get(count+2).getRhAvg()));
		    	rhSum += statisticalData.Sum(StringToBigdecimal2(monthDataList.get(r).getRh()));
		    	rhMin.add(StringToBigdecimal2(monthDataList.get(r).getRhMin()));
		    	sunshineSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getSunshine()));
		    	sunshineRateSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getSunshineRate()));
			}
			count+=3;//calculate 
			//annual 
			annualCliSum.setStnPres(StringToBigdecimal(String.valueOf(stnPresSum/rowRange)));
			annualCliSum.setTx_5H((StringToBigdecimal(String.valueOf(tx5HSum/rowRange))));
			annualCliSum.setTx_14H(StringToBigdecimal(String.valueOf(tx14HSum/rowRange)));
			annualCliSum.setTx_21H(StringToBigdecimal(String.valueOf(tx21HSum/rowRange)));
			annualCliSum.setTx(StringToBigdecimal(String.valueOf(txSum/rowRange)));
			annualCliSum.setTxMaxAvg(StringToBigdecimal(String.valueOf(txMaxAvg/rowRange)));
			annualCliSum.setTxMinAvg(StringToBigdecimal(String.valueOf(txMinAvg/rowRange)));
			annualCliSum.setTxMaxAbs(StringToBigdecimal(String.valueOf(statisticalData.Max(txMaxAbs))));
			
			annualCliSum.setTxMinAbs(StringToBigdecimal(String.valueOf(statisticalData.Min(txMinAbs))));
			
			annualCliSum.setRh_5H((StringToBigdecimal2(String.valueOf(rh5HSum/rowRange))));
			annualCliSum.setRh_14H(StringToBigdecimal2(String.valueOf(rh14HSum/rowRange)));
			annualCliSum.setRh_21H(StringToBigdecimal2(String.valueOf(rh21HSum/rowRange)));
			annualCliSum.setRh(StringToBigdecimal2(String.valueOf(rhSum/rowRange)));
			annualCliSum.setRhMin(StringToBigdecimal2(String.valueOf(statisticalData.Min(rhMin))));
			annualCliSum.setSunshine(StringToBigdecimal(String.valueOf(sunshineSum)));
			annualCliSum.setSunshineRate((StringToBigdecimal(String.valueOf(sunshineRateSum/rowRange))));
			
			// write the customer maps
		    mapWriter.write(cliSumMap, header, processors);
		}
			annualCliSumMap.put(header[0], StnCName);
			annualCliSumMap.put(header[1], StnEName);
			annualCliSumMap.put(header[2], year);
			annualCliSumMap.put(header[3], "");
			// air temperature
			annualCliSumMap.put(header[4], annualCliSum.getStnPres());
			// temperature
			annualCliSumMap.put(header[5], annualCliSum.getTx_5H());	
			annualCliSumMap.put(header[6], annualCliSum.getTx_14H());	
			annualCliSumMap.put(header[7], annualCliSum.getTx_21H());	
			annualCliSumMap.put(header[8], annualCliSum.getTx());	
			annualCliSumMap.put(header[9], annualCliSum.getTxMaxAvg());	
			annualCliSumMap.put(header[10], annualCliSum.getTxMinAvg());	
			annualCliSumMap.put(header[11], annualCliSum.getTxMaxAbs());
			annualCliSumMap.put(header[12], statisticalData.MaxTime(txMaxAbsTime));	
			annualCliSumMap.put(header[13], annualCliSum.getTxMinAbs());	
			annualCliSumMap.put(header[14], statisticalData.MinTime(txMinAbsTime));
			// relative humidity
			annualCliSumMap.put(header[15], annualCliSum.getRh_5H());	
			annualCliSumMap.put(header[16], annualCliSum.getRh_14H());	
			annualCliSumMap.put(header[17], annualCliSum.getRh_21H());	
			annualCliSumMap.put(header[18], annualCliSum.getRh());	
			annualCliSumMap.put(header[19], annualCliSum.getRhMin());	
			// sunshine
			annualCliSumMap.put(header[20], annualCliSum.getSunshine());	
			annualCliSumMap.put(header[21], annualCliSum.getSunshineRate());	
			// write the customer maps
		    mapWriter.write(annualCliSumMap, header, processors);
	}
	
	public void create2ndTable(String StnCName, String StnEName, List<CliSum> monthDataList, List<CliSum> hourAvgList,
              List<CliSum> dayAvgList, List<CliSum> hourSumList, List<CliSum> precpDays_10mmList, 
              List<CliSum> precpDays_1mmList, List<CliSum> precpDays_01mmList, ICsvMapWriter mapWriter, String []header) throws IOException {
		
		final CellProcessor[] processors = getTable2Processors();
		Map<String, Object> cliSumMap = new HashMap<String, Object>();
		Map<String, Object> annualCliSumMap = new HashMap<String, Object>();
		int rowRange = (monthDataList.size() < month)? monthDataList.size() : month;// cancel station=6; general=7
        int countRange = (hourAvgList.size() < (month*3)) ? hourAvgList.size() : (month*3);
        
        CliSum annualCliSum  = new CliSum();
        Float ws = 0.0f;
        List<Float> wd = new ArrayList<>();
        Float cAmtTotal5HSum = 0.0f;
        Float cAmtTotal5HSum_flag = 0.0f;
        Float cAmtTotal14HSum = 0.0f;
        Float cAmtTotal14HSum_flag = 0.0f;
        Float cAmtTotal21HSum = 0.0f;
        Float cAmtTotal21HSum_flag = 0.0f;
        Float cAmtMean = 0.0f;
        Float cAmtMean_flag = 0.0f;
        List<String> wsMax = new ArrayList<>();
        Map<Float, Float> wdMax = new HashMap<>();
        List<String> wsGust = new ArrayList<>();
        List<String> precp1DayMax = new ArrayList<>();
        Map<Float, LocalDateTime> wMaxTime = new HashMap<>();
        Map<Float, LocalDateTime> wGustMaxTime = new HashMap<>();
        Map<Float, LocalDateTime> precp1DayMaxTime = new HashMap<>();
        Float visbMean = 0.0f;
        Float visbMean_flag = 0.0f;
        Float precpSum = 0.0f;
        Float precpHourSum = 0.0f;
        Float precpHourSum_flag = 0.0f;
        Float precpDays_01mm= 0.0f;
        Float precpDays_1mm = 0.0f;
        Float precpDays_10mm = 0.0f;
        
		int count = 0;
		cliSumMap.put(header[0], StnCName);
		cliSumMap.put(header[1], StnEName);
		cliSumMap.put(header[2], year);
		for(int r=0; r<rowRange; r++) {
			if(count < countRange) {
				cliSumMap.put(header[3],monthDataList.get(r).getObsTime().getMonthValue());
				// wind
				cliSumMap.put(header[4], StringToBigdecimal(monthDataList.get(r).getWs()));  // 風 >> 平均風速
				cliSumMap.put(header[5], StringToBigdecimal(monthDataList.get(r).getWd()));  // 風 >> 最多風向
				cliSumMap.put(header[6], StringToBigdecimal(monthDataList.get(r).getWsMax()));  // 風 >> 最大 >> 速
				cliSumMap.put(header[7], StringToBigdecimal(monthDataList.get(r).getWdMax()));  // 風 >> 最大 >> 方向
				cliSumMap.put(header[8], String.valueOf(monthDataList.get(r).getwMaxTime().getDayOfMonth()));  // 風 >> 最大 >> 月 日
				cliSumMap.put(header[9], StringToBigdecimal(monthDataList.get(r).getWsGust()));  // 最大陣風 >> Gust >> 速
				cliSumMap.put(header[10], String.valueOf(monthDataList.get(r).getwGustTime().getDayOfMonth()));  // 最大陣風 >> Gust >> 月日
				// cloud amount
				cliSumMap.put(header[11], StringToBigdecimal(hourAvgList.get(count).getcAmtTotalAvg()));  // 雲量 >> 5h
				cliSumMap.put(header[12], StringToBigdecimal(hourAvgList.get(count+1).getcAmtTotalAvg()));  // 雲量 >> 14h
				cliSumMap.put(header[13], StringToBigdecimal(hourAvgList.get(count+2).getcAmtTotalAvg())); // 雲量 >> 21h
				cliSumMap.put(header[14], StringToBigdecimal(monthDataList.get(r).getcAmtMean())); // 雲量 >> 平均 (Mean of obs.)
				// visibility
				cliSumMap.put(header[15], StringToBigdecimal(monthDataList.get(r).getVisbMean())); // 能見度
				// precipitation
				cliSumMap.put(header[16], StringToBigdecimal(monthDataList.get(r).getPrecp())); // 降水量 >> 總計
				cliSumMap.put(header[17], StringToBigdecimal(monthDataList.get(r).getPrecp1DayMax())); // 降水量 >> 最大 >> 一日間
				cliSumMap.put(header[18], String.valueOf(monthDataList.get(r).getPrecp1DayMaxTime().getDayOfMonth())); // 降水量 >> 最大 >> 月日
				// precipitation hours
				cliSumMap.put(header[19], StringToBigdecimal(hourSumList.get(r).getPrecpHourSum())); // 降水時數
				// precipitation days
				cliSumMap.put(header[20], precpDays_01mmList.get(r).getPrecpDays_01mm()); // 降水日數 >> 總計≧0.1mm
				cliSumMap.put(header[21], precpDays_1mmList.get(r).getPrecpDays_1mm()); // 降水日數 >> ≧1.0mm
				cliSumMap.put(header[22], precpDays_10mmList.get(r).getPrecpDays_10mm()); // 降水日數 >> ≧10.0mm
				
				ws += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getWs()));
				wd.add(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWd())));
				wsMax.add(StringToBigdecimal(monthDataList.get(r).getWsMax()));
				wdMax.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWsMax())), 
						Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWdMax())));
				wMaxTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWsMax())),
						monthDataList.get(r).getwMaxTime());
				wsGust.add(StringToBigdecimal(monthDataList.get(r).getWsGust()));
				wGustMaxTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getWsGust())), monthDataList.get(r).getwGustTime());
				
				if(statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count).getcAmtTotalAvg()))<0) {
					cAmtTotal5HSum_flag += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count).getcAmtTotalAvg()));
				}else {
					cAmtTotal5HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count).getcAmtTotalAvg()));
				}	
				
				if(statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+1).getcAmtTotalAvg()))<0) {
					cAmtTotal14HSum_flag += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+1).getcAmtTotalAvg()));
				}else {
					cAmtTotal14HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+1).getcAmtTotalAvg()));
				}
				
				if(statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+2).getcAmtTotalAvg()))<0) {
					cAmtTotal21HSum_flag += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+2).getcAmtTotalAvg()));
				}else {
					cAmtTotal21HSum += statisticalData.Sum(StringToBigdecimal(hourAvgList.get(count+2).getcAmtTotalAvg()));
				}
				if(statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getcAmtMean()))<0) {
					cAmtMean_flag += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getcAmtMean()));
				}else {
					cAmtMean += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getcAmtMean()));
				}
				
				if(statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getVisbMean()))<0) {
					visbMean_flag += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getVisbMean()));
				}else {
					visbMean += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getVisbMean()));
				}
				
				precpSum += statisticalData.Sum(StringToBigdecimal(monthDataList.get(r).getPrecp()));
				precp1DayMax.add(monthDataList.get(r).getPrecp1DayMax());
				precp1DayMaxTime.put(Float.parseFloat(StringToBigdecimal(monthDataList.get(r).getPrecp1DayMax())),
						monthDataList.get(r).getPrecp1DayMaxTime());
				//sum
				if(statisticalData.Sum(StringToBigdecimal(hourSumList.get(r).getPrecpHourSum()))<0) {
					precpHourSum_flag += statisticalData.Sum(StringToBigdecimal(hourSumList.get(r).getPrecpHourSum()));
				}else {
					precpHourSum += statisticalData.Sum(StringToBigdecimal(hourSumList.get(r).getPrecpHourSum()));
				}
				
				precpDays_01mm += statisticalData.Sum(StringToBigdecimal2(precpDays_01mmList.get(r).getPrecpDays_01mm()));
				precpDays_1mm += statisticalData.Sum(StringToBigdecimal2(precpDays_1mmList.get(r).getPrecpDays_1mm()));
				precpDays_10mm += statisticalData.Sum(StringToBigdecimal2(precpDays_10mmList.get(r).getPrecpDays_10mm()));
			}
			count+=3;
			annualCliSum.setWs(StringToBigdecimal(String.valueOf(ws/rowRange)));
			annualCliSum.setWd(StringToBigdecimal(String.valueOf(statisticalData.MaxCount(wd))));
			
			annualCliSum.setWsMax(StringToBigdecimal(String.valueOf(statisticalData.Max(wsMax))));
			annualCliSum.setWdMax(StringToBigdecimal(statisticalData.MaxWd(wdMax)));
			annualCliSum.setWsGust(StringToBigdecimal(String.valueOf(statisticalData.Max(wsGust))));
			
			annualCliSum.setcAmtTotal5H((cAmtTotal5HSum_flag==-(rowRange))?"    ":
				(StringToBigdecimal(String.valueOf(cAmtTotal5HSum/(rowRange+cAmtTotal5HSum_flag)))));
			annualCliSum.setcAmtTotal14H((cAmtTotal14HSum_flag==-(rowRange))?"    ":
				(StringToBigdecimal(String.valueOf(cAmtTotal14HSum/(rowRange+cAmtTotal14HSum_flag)))));
			annualCliSum.setcAmtTotal21H((cAmtTotal21HSum_flag==-(rowRange))?"    ":
				(StringToBigdecimal(String.valueOf(cAmtTotal21HSum/(rowRange+cAmtTotal21HSum_flag)))));
			annualCliSum.setcAmtMean((cAmtMean_flag==-(rowRange))?"    ":
				(StringToBigdecimal(String.valueOf(cAmtMean/(rowRange+cAmtMean_flag)))));
			annualCliSum.setVisbMean((visbMean_flag==-(rowRange))?"    ":
				(StringToBigdecimal(String.valueOf(visbMean/(rowRange+visbMean_flag)))));
			annualCliSum.setPrecp(StringToBigdecimal(String.valueOf(precpSum)));
			annualCliSum.setPrecp1DayMax(StringToBigdecimal(String.valueOf(statisticalData.Max(precp1DayMax))));
			annualCliSum.setPrecpHourSum((precpHourSum_flag==-(rowRange))?"    ":
				(StringToBigdecimal(String.valueOf(precpHourSum))));
			annualCliSum.setPrecpDays_01mm(StringToBigdecimal2(String.valueOf(precpDays_01mm)));
			annualCliSum.setPrecpDays_1mm(StringToBigdecimal2(String.valueOf(precpDays_1mm)));
			annualCliSum.setPrecpDays_10mm(StringToBigdecimal2(String.valueOf(precpDays_10mm)));
			// write the customer maps
		    mapWriter.write(cliSumMap, header, processors);
		}
		annualCliSumMap.put(header[0], StnCName);
		annualCliSumMap.put(header[1], StnEName);
		annualCliSumMap.put(header[2], year);
		annualCliSumMap.put(header[3], "");
		// wind
		annualCliSumMap.put(header[4], annualCliSum.getWs());
		annualCliSumMap.put(header[5], annualCliSum.getWd());	
		annualCliSumMap.put(header[6], annualCliSum.getWsMax());	
		annualCliSumMap.put(header[7], annualCliSum.getWdMax());
		annualCliSumMap.put(header[8], statisticalData.MaxTime(wMaxTime));	
		annualCliSumMap.put(header[9], annualCliSum.getWsGust());
		annualCliSumMap.put(header[10], statisticalData.MaxTime(wGustMaxTime));
		// cloud amout
		annualCliSumMap.put(header[11], (annualCliSum.getcAmtTotal5H().equals("0.0"))?"   ":annualCliSum.getcAmtTotal5H());	
		annualCliSumMap.put(header[12], (annualCliSum.getcAmtTotal14H().equals("0.0"))?"   ":annualCliSum.getcAmtTotal14H());	
		annualCliSumMap.put(header[13], (annualCliSum.getcAmtTotal21H().equals("0.0"))?"   ":annualCliSum.getcAmtTotal21H());	
		annualCliSumMap.put(header[14], (annualCliSum.getcAmtMean().equals("0.0"))?"   ":annualCliSum.getcAmtMean());
		// visibility
		annualCliSumMap.put(header[15], (annualCliSum.getVisbMean().equals("0.0"))?"   ":annualCliSum.getVisbMean());
		// precipitation
		annualCliSumMap.put(header[16], annualCliSum.getPrecp());	
		annualCliSumMap.put(header[17], annualCliSum.getPrecp1DayMax());	
		annualCliSumMap.put(header[18], statisticalData.MaxTime(precp1DayMaxTime));
		// precipitation hours
		annualCliSumMap.put(header[19], (annualCliSum.getPrecpHourSum().equals("0.0"))?"   ":annualCliSum.getPrecpHourSum());	
		// precipitation days
		annualCliSumMap.put(header[20], annualCliSum.getPrecpDays_01mm());
		annualCliSumMap.put(header[21], annualCliSum.getPrecpDays_1mm());	
		annualCliSumMap.put(header[22], annualCliSum.getPrecpDays_10mm());	
		// write the customer maps
	    mapWriter.write(annualCliSumMap, header, processors);
	}

	
	public void create3rdTable(String StnCName, String StnEName, List<CliSum> monthDataList, List<CliSum> hourAvgList, List<CliSum> dayAvgList,
              List<CliSum> weatherConditionList, List<CliSum> TxMinAbs_LE0List, List<CliSum> TxMinAbs_LE10List,
			  List<CliSum> TxMinAbs_GE20List, List<CliSum> TxMinAbs_GE25List, List<CliSum> TxMinAbs_GE30List,
			  List<CliSum> TxMinAbs_GE35List, List<CliSum> precpDayList, List<CliSum> hazeDayList,
			  List<CliSum> hailDayList, List<CliSum> dewDayList, List<CliSum> snowDateList,
			  List<CliSum> frostDateList, ICsvMapWriter mapWriter, String []header) throws IOException {
		
		final CellProcessor[] processors = getTable3Processors();
		Map<String, Object> cliSumMap = new HashMap<String, Object>();
		Map<String, Object> annualCliSumMap = new HashMap<String, Object>();
		int count = 0;
		int rowRange = (monthDataList.size() < month)? monthDataList.size() : month;// cancel station=6; general=7
        int countRange = (monthDataList.size() < month)? monthDataList.size() : month;
        CliSum annualCliSum  = new CliSum();
        Float evapA = 0.0f;
        Float txMinAbs_LE0 = 0.0f;
        Float txMinAbs_LE10 = 0.0f;
        Float txMinAbs_GE20 = 0.0f;
        Float txMaxAbs_GE25 = 0.0f;
        Float txMaxAbs_GE30 = 0.0f;
        Float txMaxAbs_GE35 = 0.0f;
        Float precpDay = 0.0f;
        Float snowDay = 0.0f;
        Float snow_flag = 0.0f;
        Float fogDay = 0.0f;
        Float hazeDay = 0.0f;
        Float thunderstormDay = 0.0f;
        Float hailDay = 0.0f;
        Float wsMaxGE10Day = 0.0f;
        Float dewDay = 0.0f;
        Float frostDay = 0.0f;
        Float frost_flag = 0.0f;
        Float clearSkyDay = 0.0f;
        Float scatteredSkyDay = 0.0f;
        Float brokenSkyDay = 0.0f;
        Float overcastSkyDay = 0.0f;
		cliSumMap.put(header[0], StnCName);
		cliSumMap.put(header[1], StnEName);
		cliSumMap.put(header[2], year);
		for(int r=0; r<rowRange; r++) {  
			//control sum process
			if(count<countRange) {
				cliSumMap.put(header[3], weatherConditionList.get(r).getObsTime().getMonthValue());
				// evaporation
				cliSumMap.put(header[4], weatherConditionList.get(r).getEvapA());  // 蒸發量
				// weather condition
				// - temperature min
				cliSumMap.put(header[5], TxMinAbs_LE0List.get(r).getTxMinAbs_LE0());  // 天氣現象 >> 最低氣溫 : <=0
				cliSumMap.put(header[6], TxMinAbs_LE10List.get(r).getTxMinAbs_LE10());  // 天氣現象 >> 最低氣溫 : <=10
				cliSumMap.put(header[7], TxMinAbs_GE20List.get(r).getTxMinAbs_GE20());  // 天氣現象 >> 最低氣溫 : >=20
				// - temperature max
				cliSumMap.put(header[8], TxMinAbs_GE25List.get(r).getTxMaxAbs_GE25());  // 天氣現象 >> 最高氣溫 : >=25
				cliSumMap.put(header[9], TxMinAbs_GE30List.get(r).getTxMaxAbs_GE30());  // 天氣現象 >> 最高氣溫 : >= 30
				cliSumMap.put(header[10], TxMinAbs_GE35List.get(r).getTxMaxAbs_GE35());  // 天氣現象 >> 最高氣溫 : >=35
				cliSumMap.put(header[11], precpDayList.get(r).getPrecpDay());  // 天氣現象 >> 雨
				cliSumMap.put(header[12], weatherConditionList.get(r).getSnowDay());  // 天氣現象 >> 雪
				cliSumMap.put(header[13], weatherConditionList.get(r).getFogDay()); // 天氣現象 >> 霧
				cliSumMap.put(header[14], hazeDayList.get(r).getStatO2()); // 天氣現象 >> 霾
				cliSumMap.put(header[15], nullToEmptyValue(weatherConditionList.get(r).getThunderstormDay())); // 天氣現象 >> 雷暴
				cliSumMap.put(header[16], hailDayList.get(r).getStatF3()); // 天氣現象 >> 雹
				cliSumMap.put(header[17], weatherConditionList.get(r).getWsMaxGE10Day()); // 天氣現象 >> 強風
				cliSumMap.put(header[18], dewDayList.get(r).getStatP2()); // 天氣現象 >> 露
				cliSumMap.put(header[19], weatherConditionList.get(r).getFrostDay()); // 天氣現象 >> 霜
				// sky condition
				cliSumMap.put(header[20], weatherConditionList.get(r).getClearSkyDay()); // 天空狀況 >> 碧
				cliSumMap.put(header[21], weatherConditionList.get(r).getScatteredSkyDay()); // 天空狀況 >> 疏
				cliSumMap.put(header[22], weatherConditionList.get(r).getBrokenSkyDay()); // 天空狀況 >> 裂
				cliSumMap.put(header[23], weatherConditionList.get(r).getOvercastSkyDay()); // 天空狀況 >> 密
				// snow date
				cliSumMap.put(header[24], getDayOfMonth(snowDateList.get(r).getStatF1_MinTime()) +
						"   " + getDayOfMonth(snowDateList.get(r).getStatF1_MaxTime())); // 初終雪日 >> from-to
				// frost date
				cliSumMap.put(header[25], getDayOfMonth(frostDateList.get(r).getStatF2_MinTime()) +
						"   " + getDayOfMonth(frostDateList.get(r).getStatF2_MaxTime())); // 初終雪日 >> from-to
				
				evapA += statisticalData.CountDaySum(weatherConditionList.get(r).getEvapA());
				txMinAbs_LE0 += statisticalData.Sum(TxMinAbs_LE0List.get(r).getTxMinAbs_LE0());
				txMinAbs_LE10 += statisticalData.Sum(TxMinAbs_LE10List.get(r).getTxMinAbs_LE10());
				txMinAbs_GE20 += statisticalData.Sum(TxMinAbs_GE20List.get(r).getTxMinAbs_GE20());
				txMaxAbs_GE25 += statisticalData.Sum(TxMinAbs_GE25List.get(r).getTxMaxAbs_GE25());
				txMaxAbs_GE30 += statisticalData.Sum(TxMinAbs_GE30List.get(r).getTxMaxAbs_GE30());
				txMaxAbs_GE35 += statisticalData.Sum(TxMinAbs_GE35List.get(r).getTxMaxAbs_GE35());
				precpDay += statisticalData.Sum(precpDayList.get(r).getPrecpDay());
				//day count with empty value
				if(statisticalData.CountDaySum(weatherConditionList.get(r).getSnowDay())<0) {
					snow_flag += statisticalData.CountDaySum(weatherConditionList.get(r).getSnowDay());
				}else {
					snowDay += statisticalData.CountDaySum(weatherConditionList.get(r).getSnowDay());
				}
				
				fogDay += statisticalData.CountDaySum(weatherConditionList.get(r).getFogDay());
				hazeDay += statisticalData.CountDaySum(hazeDayList.get(r).getStatO2());
				thunderstormDay += statisticalData.CountDaySum(nullToEmptyValue(weatherConditionList.get(r).getThunderstormDay()));
				hailDay += statisticalData.CountDaySum(hailDayList.get(r).getStatF3());
				wsMaxGE10Day += statisticalData.Sum(weatherConditionList.get(r).getWsMaxGE10Day());
				dewDay += statisticalData.CountDaySum(dewDayList.get(r).getStatP2());
				if(statisticalData.CountDaySum(weatherConditionList.get(r).getFrostDay())<0) {
					frost_flag += statisticalData.CountDaySum(weatherConditionList.get(r).getFrostDay());
				}else {
					frostDay += statisticalData.CountDaySum(weatherConditionList.get(r).getFrostDay());
				}
				clearSkyDay += statisticalData.CountDaySum(weatherConditionList.get(r).getClearSkyDay());
				scatteredSkyDay += statisticalData.CountDaySum(weatherConditionList.get(r).getScatteredSkyDay());
				brokenSkyDay += statisticalData.CountDaySum(weatherConditionList.get(r).getBrokenSkyDay());
				overcastSkyDay += statisticalData.CountDaySum(weatherConditionList.get(r).getOvercastSkyDay());
			}
			count++;
			annualCliSum.setEvapA(StringToBigdecimal(String.valueOf(evapA)));
			annualCliSum.setTxMinAbs_LE0(StringToBigdecimal2(String.valueOf(txMinAbs_LE0)));
			annualCliSum.setTxMinAbs_LE10(StringToBigdecimal2(String.valueOf(txMinAbs_LE10)));
			annualCliSum.setTxMinAbs_GE20(StringToBigdecimal2(String.valueOf(txMinAbs_GE20)));
			annualCliSum.setTxMaxAbs_GE25(StringToBigdecimal2(String.valueOf(txMaxAbs_GE25)));
			annualCliSum.setTxMaxAbs_GE30(StringToBigdecimal2(String.valueOf(txMaxAbs_GE30)));
			annualCliSum.setTxMaxAbs_GE35(StringToBigdecimal2(String.valueOf(txMaxAbs_GE35)));
			annualCliSum.setPrecpDay(StringToBigdecimal2(String.valueOf(precpDay)));
			annualCliSum.setSnowDay(StringToBigdecimal2(String.valueOf(snowDay)));
			annualCliSum.setFogDay(StringToBigdecimal2(String.valueOf(fogDay)));
			annualCliSum.setStatO2(StringToBigdecimal2(String.valueOf(hazeDay)));
			annualCliSum.setThunderstormDay(StringToBigdecimal2(String.valueOf(thunderstormDay)));
			annualCliSum.setStatF3(StringToBigdecimal2(String.valueOf(hailDay)));
			annualCliSum.setWsMaxGE10Day(StringToBigdecimal2(String.valueOf(wsMaxGE10Day)));
			annualCliSum.setStatP2(StringToBigdecimal2(String.valueOf(dewDay)));
			annualCliSum.setFrostDay(StringToBigdecimal2(String.valueOf(frostDay)));
			annualCliSum.setClearSkyDay(StringToBigdecimal2(String.valueOf(clearSkyDay)));
			annualCliSum.setScatteredSkyDay(StringToBigdecimal2(String.valueOf(scatteredSkyDay)));
			annualCliSum.setBrokenSkyDay(StringToBigdecimal2(String.valueOf(brokenSkyDay)));
			annualCliSum.setOvercastSkyDay(StringToBigdecimal2(String.valueOf(overcastSkyDay)));
			
			// write the customer maps
		    mapWriter.write(cliSumMap, header, processors);
		}
		annualCliSumMap.put(header[0], StnCName);
		annualCliSumMap.put(header[1], StnEName);
		annualCliSumMap.put(header[2], year);
		annualCliSumMap.put(header[3], "");
		// evaporation
		annualCliSumMap.put(header[4], (annualCliSum.getEvapA().equals("0"))?
				(((monthDataList.get(0).getManObsNum().equals("0"))?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getEvapA())<0)?"   ":annualCliSum.getEvapA());
		// weather condition
		// - temperature min
		annualCliSumMap.put(header[5], annualCliSum.getTxMinAbs_LE0());	
		annualCliSumMap.put(header[6], annualCliSum.getTxMinAbs_LE10());	
		annualCliSumMap.put(header[7], annualCliSum.getTxMinAbs_GE20());	
		// - temperature max
		annualCliSumMap.put(header[8], annualCliSum.getTxMaxAbs_GE25());	
		annualCliSumMap.put(header[9], annualCliSum.getTxMaxAbs_GE30());	
		annualCliSumMap.put(header[10], annualCliSum.getTxMaxAbs_GE35());
		annualCliSumMap.put(header[11], annualCliSum.getPrecpDay());	
		annualCliSumMap.put(header[12], (snow_flag==-(rowRange))?"   ":annualCliSum.getSnowDay());
		annualCliSumMap.put(header[13], (annualCliSum.getFogDay().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getFogDay())<0?"   ":annualCliSum.getFogDay()));	
		annualCliSumMap.put(header[14], (annualCliSum.getStatO2().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getStatO2())<0?"   ":annualCliSum.getStatO2()));
		annualCliSumMap.put(header[15],  (annualCliSum.getThunderstormDay().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:annualCliSum.getThunderstormDay());
		annualCliSumMap.put(header[16],  (annualCliSum.getStatF3().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getStatF3())<0)?"   ":annualCliSum.getStatF3());
		annualCliSumMap.put(header[17], annualCliSum.getWsMaxGE10Day());	
		annualCliSumMap.put(header[18],  (annualCliSum.getStatP2().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getStatP2())<0)?"   ":annualCliSum.getStatP2());
		annualCliSumMap.put(header[19], (frost_flag==-(rowRange))?"   ":annualCliSum.getFrostDay());
		// sky condition
		annualCliSumMap.put(header[20], (annualCliSum.getClearSkyDay().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getClearSkyDay())<0)?"   ":annualCliSum.getClearSkyDay());
		annualCliSumMap.put(header[21], (annualCliSum.getScatteredSkyDay().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getScatteredSkyDay())<0)?"   ":annualCliSum.getScatteredSkyDay());
		annualCliSumMap.put(header[22], (annualCliSum.getBrokenSkyDay().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getBrokenSkyDay())<0)?"   ":annualCliSum.getBrokenSkyDay());
		annualCliSumMap.put(header[23], (annualCliSum.getOvercastSkyDay().equals("0"))?
				((monthDataList.get(0).getManObsNum().equals("0")?"   ":"0"))
				:(Float.parseFloat(annualCliSum.getOvercastSkyDay())<0)?"   ":annualCliSum.getOvercastSkyDay());
		// snow date (from, to)
		annualCliSumMap.put(header[24], "   ");
		// frost date (from, to)
		annualCliSumMap.put(header[25], "   ");	
		// write the customer maps
	    mapWriter.write(annualCliSumMap, header, processors);
	}
	
	
    private String StringToBigdecimal(String value) {
    	String data = (value==null) ? 
    			"    ": new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    	return data;
    }
    
    private String StringToBigdecimal2(String value) {
    	String data = (value==null)? 
    			"    ":new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
    	return data;
    }
    
    private String getDayOfMonth(String value) {
    	String data = (value==null)? 
    			"" : value.substring(8, 10);
    	return data;
    }
    private String nullToEmptyValue(String value) {
    	String data = (value==null)? 
    			"" : value;
    	return data;
    }
}
