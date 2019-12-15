package cwb.cmt.view.utils;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class OpenFileExplorer {

	private static final String FILE_CHOOSE_TITLE = "請選擇合適的檔案";
	private static final String FOLDER_CHOOSER_TITLE = "請選擇檔案放置資料夾";
	
	public static File selectPdf(ActionEvent event){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(FILE_CHOOSE_TITLE);
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.pdf"));
		Node node = (Node) event.getSource();
		return fileChooser.showOpenDialog(node.getScene().getWindow());
	}
	
	public static File selectFolder(ActionEvent event){
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle(FOLDER_CHOOSER_TITLE);
		Node node = (Node) event.getSource();
		return chooser.showDialog(node.getScene().getWindow());
	}
}
