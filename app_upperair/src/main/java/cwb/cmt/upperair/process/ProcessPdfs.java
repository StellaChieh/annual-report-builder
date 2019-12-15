package cwb.cmt.upperair.process;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import cwb.cmt.upperair.model.Station;
import cwb.cmt.upperair.utils.AddPageNumber;
import cwb.cmt.upperair.utils.PageInfo;
import cwb.cmt.upperair.utils.PdfPageType;

@Service
public class ProcessPdfs {

	@Autowired
	PageInfoManager pageManager;

	@Autowired
	PrepareStations prepareStns;

	private int year;

	private String outputPdfFile;

	private Map<PdfPageType, String> outlineTypeMap = PdfPageType.getTypeNameMap();
	
	private static final Logger logger = LogManager.getLogger();

	public void init (int year, String outputPdfFile) {
		this.year = year;
		this.outputPdfFile = outputPdfFile;
	}
	
	public void run() throws IOException, DocumentException {

		List<PageInfo> pageInfos = pageManager.getPageInfos();
		pageInfos.sort(Comparator.comparing(PageInfo::getPageOrder));
		
		logger.info("Print page number...");
		// print page number
		for (int i = 0; i < pageInfos.size(); i++) {
			String inAbsolutePath = pageInfos.get(i).getFilePath();
			// rename
			int index = pageInfos.get(i).getFilePath().lastIndexOf(".");
			String outAbsolutePath = pageInfos.get(i).getFilePath().substring(0 , index) + "_paged.pdf";
			pageInfos.get(i).setFilePath(outAbsolutePath);
			String pageNumber = pageInfos.get(i).getPrintedPageNumber();
			AddPageNumber.add(inAbsolutePath, outAbsolutePath, pageNumber);
			// delete the source pdf after it was stamped page number
			Files.delete(Paths.get(inAbsolutePath));
		}

		// create parent for output
		if (!Files.exists(Paths.get(outputPdfFile).getParent())) {
			Files.createDirectories(Paths.get(outputPdfFile).getParent());
		}; 
		
		
		logger.info("Merge with outline...");
		// merge with outline
		Document doc = new Document();
		PdfCopy copy = new PdfCopy(doc, new FileOutputStream(outputPdfFile));
		doc.open();
		PdfReader reader = null;
		ArrayList<HashMap<String, Object>> outlines = new ArrayList<HashMap<String, Object>>();
		try {
			for (PageInfo pageInfo : pageInfos) {
				if (outlineTypeMap.get(pageInfo.getPageType()) != null) {
					outlines.add(createOutlineElement(pageInfo.getPageOrder(), getOutlineName(pageInfo)));
				}
				try (FileInputStream is = new FileInputStream(pageInfo.getFilePath());) {
					reader = new PdfReader(is);
					copy.addDocument(reader);
					copy.setOutlines(outlines);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} finally {
			doc.close();
			reader.close();
		}
	}

	private String getOutlineName(PageInfo pageInfo) throws IOException {
		PdfPageType type = pageInfo.getPageType();
		String outlineName = "";
		if (type == PdfPageType.COVER) {
			outlineName = String.format(outlineTypeMap.get(type), year - 1911, year);
			outlineTypeMap.remove(type);
		} else if (type == PdfPageType.INTERLEAF) {
			for (Station s : prepareStns.getStns()) {
				if (pageInfo.getFilename().contains(s.getStnCName())) {
					outlineName = String.format(outlineTypeMap.get(type), s.getStnCName() + " " + s.getStnEName());
				}
			}
		} else {
			outlineName = outlineTypeMap.get(type);
			outlineTypeMap.remove(type);
		}
		return outlineName;
	}

	private HashMap<String, Object> createOutlineElement(int page, String outlineName) {
		HashMap<String, Object> outlineMap = new HashMap<>();
		outlineMap.put("Title", outlineName);
		outlineMap.put("Action", "GoTo");
		outlineMap.put("Page", String.format("%d Fit", page));
		return outlineMap;
	}

}
