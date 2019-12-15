package windRose.process;

import java.io.IOException;

public class WindRoseMain {
	
	public static void summary(String[] Station, String num, String year, String filename) throws IOException{
		//Station:測站ID , num:羅馬數字, year:起終年份, filename:PDF名稱, index:判斷彙編or年報
		boolean index = true; //判斷是彙編or地面年報
		new ProcessTYWeather().run(Station, num, year, filename, index);
	}
	
	public static void summaryStn(String Station, String num_ala, String num, String year, String filename) throws IOException{
		//Station:測站ID ,num_ala:阿拉伯數字, num:羅馬數字, year:起終年份, filename:PDF名稱		
		new ProcessTYStation().run(Station, num_ala, num, year, filename);
	}
	
	public static void surface(String[] Station, String num, String year, String filename) throws IOException{
		//Station:測站ID , num:羅馬數字, year:起終年份, filename:PDF名稱, index:判斷彙編or年報
		boolean index = false; //判斷是彙編or地面年報
		new ProcessYear().run(Station, num, year, filename, index);
		new ProcessTYWeather().run(Station, num, year, filename, index);//年報當年所有測站風花圖
	}
	
}
