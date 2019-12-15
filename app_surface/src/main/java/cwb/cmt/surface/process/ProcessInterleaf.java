package cwb.cmt.surface.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.plaf.synth.SynthSplitPaneUI;

import org.springframework.core.env.SystemEnvironmentPropertySource;
//import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import cwb.cmt.surface.service.CreateTableImage;
import cwb.cmt.surface.service.CreateTableImageForFirstInterleaf;
import cwb.cmt.surface.utils.InterLeaf;
import cwb.cmt.surface.utils.PageEncode;
import cwb.cmt.surface.utils.SpecialValue;

@Service("processInterleaf")
public class ProcessInterleaf extends Process{

	@Resource(name="createTableImageForFirstInterleaf")
	CreateTableImageForFirstInterleaf imgCreator;
	
	public void run(int chapter, String titleChinese, String titleEnglish) {
		System.out.println("start to draw interleaf...");
		for(String chineseTitle:InterLeaf.initializeMapping().keySet()) {
			imgCreator.setOutputTmpPdfPath(outputTmpPdfPath);
			imgCreator.setFilename(chapter + "_0" + "_" + titleChinese + "_" + titleEnglish.replace("\n", " "));
			try {
				if(chineseTitle.replaceAll(" ", "")
						.replaceAll("\\d"+"."+"\n", "").equals(titleChinese)){
					imgCreator.createTableImage(chineseTitle, 
							InterLeaf.initializeMapping().get(chineseTitle).replace("\n", " "));
				}
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			}
			System.out.println("draw interleaf completed...");
		}
		
	}

	@Override
	public void run() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
