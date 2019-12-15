package cwb.cmt.surface.pdfProcess;

public class CheckPdf {

	public static boolean isInterleaf(String filename) {
		int idxDash = filename.indexOf('-');
		int idxUnderline = filename.indexOf('_');
		if (idxDash < idxUnderline) {
			// fileSeq-pageNum_n1-n2-n3#n4_textChinese-textEnglish#pageSeq
			return false;
		} else {
			// fileSeq_n1-n2-n3#n4_textChinese-textEnglish#pageSeq
			return true;
		}
	}
}
