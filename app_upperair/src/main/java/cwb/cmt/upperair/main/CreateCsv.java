package cwb.cmt.upperair.main;

import java.io.IOException;
import java.time.YearMonth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.process.ProcessSignificantCsv;
import cwb.cmt.upperair.process.ProcessStandardCsv;
import cwb.cmt.upperair.utils.Utility;

@Service
public class CreateCsv extends CreateOutput {

	@Autowired
	ProcessSignificantCsv processSignificantCsv;

	@Autowired
	ProcessStandardCsv processStandardCsv;

	@Autowired
	@Qualifier("csvStandardFilename")
	private String standardCsvFile;

	@Autowired
	@Qualifier("csvTropFilename")
	private String tropCsvFile;

	@Autowired
	@Qualifier("csvLastFilename")
	private String lastCsvFile;

	@Autowired
	@Qualifier("csvSignificantFilename")
	private String significantCsvFile;

	private static final Logger logger = LogManager.getLogger();

	@Override
	protected boolean run() {
		for(Station s : stns) {
			for(YearMonth m : Utility.getMonthsUntilNow(year, month)) {
				try{
					processStandardCsv.writeCsv(s, m, "00");
					processStandardCsv.writeCsv(s, m, "12");	
					processSignificantCsv.writeCsv(s, m, "00");
					processSignificantCsv.writeCsv(s, m, "12");	
				} catch (IOException e) {
					String logMsg = String.format("Generate %s %d-%d csv files failed."
							, s.getStnEName(), m.getYear(), m.getMonthValue());
					logger.info(logMsg);
					return false;
				}
			}
		}	
		return true;
	}

	@Override
	protected boolean prepareResourceFiles() {
		if (!super.prepareResourceFiles()) {
			return false;
		}
		standardCsvFile = makeAbsolutelyPath(outputFolder, formatOutputFilename(standardCsvFile));
		tropCsvFile = makeAbsolutelyPath(outputFolder, formatOutputFilename(tropCsvFile));
		lastCsvFile = makeAbsolutelyPath(outputFolder, formatOutputFilename(lastCsvFile));
		significantCsvFile = makeAbsolutelyPath(outputFolder, formatOutputFilename(significantCsvFile));
		
		if(!deleteOldOutputFile(standardCsvFile) 
			|| !deleteOldOutputFile(tropCsvFile)
			|| !deleteOldOutputFile(lastCsvFile)
			|| !deleteOldOutputFile(significantCsvFile)) {
			return false;
		}
		return true;
	}

	@Override
	protected void initProcessParameters() {
		super.initProcessParameters();
		processStandardCsv.init(standardCsvFile, tropCsvFile, lastCsvFile);
		processSignificantCsv.init(significantCsvFile);
	}

}
