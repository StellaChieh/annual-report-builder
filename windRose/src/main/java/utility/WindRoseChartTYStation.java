package utility;

import ChartDirector.Chart;
import ChartDirector.MultiChart;
import ChartDirector.PolarChart;
import ChartDirector.PolarLineLayer;
import ChartDirector.TextBox;

public class WindRoseChartTYStation
{
	public static String stnCHname = null;
	public static String  stnENname = null;
	public static String  stnID = null;
	
    //Main code for creating charts
    public PolarChart createRose(double CALM, double[] data0, double[] data1, double[] data2, double[] angles,
    		String stno, String stnCName, String stnEName, String startDate, String endDate, Boolean runPerMonth, int month)
    {
    	//註冊
    	Chart.setLicenseCode("DEVP-2LFA-BN3Z-3YPF-D1E4-DE76");
    	
    	int centerX = 110;
    	int centerY = 150;
        int R = 70;
        int r = 20;
        
        stnCHname = stnCName; //測站中文名稱
        stnENname = stnEName; //測站英文名稱
        stnID = stno;         //測站ID

        PolarChart c = new PolarChart(220, 280, 0xffffff, 0x000000, 1);
        
        //if want to output .svg
        c.enableVectorOutput();
        
        //station name, date
        String textTitle = "";
        if(runPerMonth){
        	textTitle = "風花圖  Surface Wind Roses\n" + startDate.substring(0,4) + "年" + startDate.substring(5,7)
                + "月\n" + stnCName;
        }
        else{       	
        	textTitle = startDate.substring(0,4) + "年至 "
            		+ String.valueOf(Integer.valueOf(endDate.substring(0,4))-1) + "年 " + month + "月" + "\n"
            		+ stnCName + " " + stno;
        }
        TextBox textSt = c.addText(20, 20, textTitle, "kaiu.ttf", 7);
        textSt.setAlignment(Chart.TopLeft);
        textSt.setBackground(0xffffffff);
        
        // Set plot area
        c.setPlotArea(centerX, centerY, R, 0xffffff);
        
        // Set the grid style to circular grid
        // and the grid lines will be at the bottom of the polar plot area
        c.setGridStyle(false, false);
        c.setGridColor(0x709999ff, 2, 0x709999ff, 2);
        
        // Set angular axis as 0 - 360, with a spoke every 30 units
        String[] angularLabels = new String[12];
    	for(int i = 0; i < angularLabels.length; ++i){
    		if(i == 0)
    			angularLabels[i] = "360°";
    		else
    			angularLabels[i] = String.valueOf(i*30) + "°";
    	}  
        c.angularAxis().setLinearScale2(0, 360, angularLabels);
        c.angularAxis().setLabelStyle("Arial", 5, 0x000000).setBackground(0xffffffff);
        
        // remove the line in the center area
        for(int i = 0; i<360; i=i+30){
            int cosX = (int)((r-3) * Math.cos(i / 180.0 * Math.PI));
            int sinY = (int)((r-3) * Math.sin(i / 180.0 * Math.PI));
            c.addLine(centerX, centerY, centerX + cosX, centerY + sinY, 0xffffff, 4);
        }
        
        // Set radial axis as 0 - 40%, with a spoke every 10 units
        c.radialAxis().setMargin(0, r);
        String[] radialLabels = {" ", "10", "20", "30%"};
        c.radialAxis().setLinearScale2(0, 30, radialLabels);
        c.radialAxis().setLabelStyle("", 5, 0x000000).setBackground(0xffffffff);
        c.radialAxis().setColors(0x709999ff);
        c.radialAxis().setLabelGap(1);
        
        //the CALM
        TextBox textCALM = c.addText(centerX, centerY, "CALM\n"+CALM+"%", "Arial Bold", 5);
        textCALM.setAlignment(Chart.Center);
        textCALM.setBackground(0x00ffffff);
        
        //draw the wind rose
        double maxData = 0.0;
        int maxDataAngles = 0;
        for (int i = 0; i < angles.length; ++i) {
        	if(data0[i] > 0)
                c.angularAxis().addZone(angles[i] - 1, angles[i] + 1, 0, data0[i], 0x000000, -1);
            if(data1[i] - data0[i] > 0)
                c.angularAxis().addZone(angles[i] - 3, angles[i] + 3, data0[i], data1[i], 0x000000, -1);
            if(data2[i] - data1[i] > 0)
                c.angularAxis().addZone(angles[i] - 5, angles[i] + 5, data1[i], data2[i], 0x000000, -1);
            if (data2[i] > maxData){
            	maxData = data2[i];
            	maxDataAngles = i*10 + 10;
            }
        }
        
        // lable the max % of wind
        if(maxData > 0){
        	double[] test0 = {maxData};
        	double[] test1 = {maxDataAngles};
        	int labelX = 0;
        	int labelY = 0;
        	PolarLineLayer layer0 = c.addLineLayer(test0);
        	layer0.setAngles(test1);
        	layer0.setDataLabelFormat("{value}");
        	layer0.setDataLabelStyle().setBackground(0xffffffff);
        	if(maxDataAngles > 0 && maxDataAngles < 90){
        		labelX = 20;
        		labelY = -10;
        	}
        	else if (maxDataAngles == 90){
        		labelX = 17;
        		labelY = 0;
        	}
        	else if(maxDataAngles > 90 && maxDataAngles < 180){
        		labelX = 20;
        		labelY = 20;
        	}
        	else if (maxDataAngles == 180){
        		labelX = 0;
        		labelY = 15;
        	}
        	else if(maxDataAngles > 180 && maxDataAngles < 270){
        		labelX = -20;
        		labelY = 20;
        	}
        	else if(maxDataAngles == 270){
        		labelX = -20;
        		labelY = 0;
        	}
        	else if(maxDataAngles > 270 && maxDataAngles < 360){
        		labelX = -10;
        		labelY = -20;
        	}
        	else if(maxDataAngles == 360){
        		labelX = -25;
        		labelY = -5;
        	}
        	layer0.setDataLabelStyle("Arial Bold", 5).setPos(labelX, labelY);
        }
        
        return c;
    }
    
	public void mergeChart(PolarChart[] chArray, PolarChart exChart, String num_ala, String num, String filename){
        
		/**
		 * chArray:各月份風花圖  / exChart:範例風花圖 / num_ala:阿拉伯數字/ num:羅馬數字/ filename: 輸出檔案名稱 
		 */
		//merge c1~c4
		MultiChart m = new MultiChart(794, 1123);
		m.addChart(200, 150, exChart);
		m.addChart(65, 730, chArray[0]);
		m.addChart(290, 730, chArray[1]);
		m.addChart(515, 730, chArray[2]);

        TextBox title_1 = m.addText(400, 35, num_ala + " " + stnCHname + " " + stnENname + "\n"
        									+ num + "." + " 風 花 圖" + "\n" + " Wind Rose" , "kaiu.ttf", 10);
        title_1.setAlignment(Chart.Center);
        title_1.setBackground(0xffffffff);
        
		m.setOutputOptions("width=595; height = 842");
		m.makeChart("tmp/tmp_summary/"+filename+"_1.pdf"); //產製第一張PDF
		
		
		MultiChart m2 = new MultiChart(794, 1123);
		m2.addChart(65, 110, chArray[3]);
		m2.addChart(290, 110, chArray[4]);
		m2.addChart(515, 110, chArray[5]);	
		m2.addChart(65, 420, chArray[6]);
		m2.addChart(290, 420, chArray[7]);
		m2.addChart(515, 420, chArray[8]);		
		m2.addChart(65, 730, chArray[9]);
		m2.addChart(290, 730, chArray[10]);
		m2.addChart(515, 730, chArray[11]);
		
        TextBox title_2 = m2.addText(400, 35, num_ala + " " + stnCHname + " " + stnENname + "\n"
											+ num + "." + " 風 花 圖" + "\n" + " Wind Rose" , "kaiu.ttf", 10);
        title_2.setAlignment(Chart.Center);
        title_2.setBackground(0xffffffff);
		
		m2.setOutputOptions("width=595; height = 842");
		m2.makeChart("tmp/tmp_summary/"+filename+"_2.pdf"); //產製第二張PDF
			
	}

}

