package utility;

import java.io.File;
import java.util.ResourceBundle;

import ChartDirector.Chart;
import ChartDirector.MultiChart;
import ChartDirector.PolarChart;
import ChartDirector.PolarLineLayer;
import ChartDirector.TextBox;

//public class StackRoseExample implements DemoModule
public class WindRoseExample_Output
{
    //Name of demo program
    public String toString() { return "Stacked Rose Chart"; }

    //Number of charts produced in this demo
    public int getNoOfCharts() { return 1; }

    //
    //Main code for creating charts
    public void createChart(String num, String filename, boolean index)
    {
    	//註冊
    	Chart.setLicenseCode("DEVP-2LFA-BN3Z-3YPF-D1E4-DE76");
    	
    	double[] data0 = {3.1, 2.6, 11.2, 5.1, 3.5, 3.9,
    					  3.2, 1.9, 1.9, 1.6, 0.9, 2.4,
    					  3.0, 3.6, 1.0, 2.6, 0.8, 2.9,
    					  0.3, 0.0, 0.1, 0.5, 0.1, 0.4, 
    					  0.0, 0.1, 1.1, 0.8, 0.5, 1.9, 
    					  1.7, 1.3, 0.9, 1.3, 0.5, 2.0};
    	
    	double[] data1 = {3.1, 2.6, 18.1, 7.1, 3.5, 3.9, 
    					  3.2, 1.9, 1.9, 1.6, 0.9, 2.4,
    					  3.0, 5.5, 1.0, 2.6, 0.8, 4.9, 
    					  0.3, 0.0, 0.1, 0.5, 0.1, 0.4, 
    					  0.0, 0.1, 1.1, 0.8, 0.5, 1.9, 
    					  1.7, 1.3, 0.9, 1.3, 0.5, 4.0};
    	
    	double[] data2 = {3.1, 2.6, 25.7, 10.1, 3.5, 3.9,
    					  3.2, 1.9, 1.9, 1.6, 0.9, 2.4,
    					  3.0, 5.5, 1.0, 2.6, 0.8, 6.9,
    					  0.3, 0.0, 0.1, 0.5, 0.1, 0.4,
    					  0.0, 0.1, 1.1, 0.8, 0.5, 1.9,
    					  1.7, 1.3, 0.9, 1.3, 0.5, 4.0};
    	
        double[] angles = new double[36];
    	for(int i = 0; i < angles.length; ++i)
    		angles[i] = (i+1)*10.0;

    	double totalWind = 0.0;
    	
    	for(int i = 0; i < data2.length; ++i)
    		totalWind = totalWind + data2[i];
    		
    	double CALM = Math.round(100.0 - totalWind);
    	
    	String subFilename = "WR_A_Example";
    	String stnCName = "";
    	
    	int centerX = 200;
    	int centerY = 275;
        int R = 120;
        int r = 30;

        PolarChart c = new PolarChart(400, 500, 0xffffff, 0x000000, 1);
        
        //if want to output .svg
        c.enableVectorOutput();
        
        //station name
        TextBox textSt = c.addText(10, 10, stnCName, "kaiu.ttf", 8);
        textSt.setAlignment(Chart.TopLeft);
        textSt.setBackground(0xffffffff);
        
        
        TextBox textLegend = c.addText(centerX, 40, "圖   例\nLegend", "kaiu.ttf", 12);
        textLegend.setAlignment(Chart.Center);
        textLegend.setBackground(0xffffffff);
        
        TextBox text36 = c.addText(centerX+60, 30, "(36方位製圖)", "kaiu.ttf", 10);
        text36.setAlignment(Chart.TopLeft);
        text36.setBackground(0xffffffff);

        TextBox textFrequency = c.addText(370, 70, "頻率百分比\nFrequency Percentage", "kaiu.ttf", 10);
        textFrequency.setAlignment(Chart.TopRight);
        textFrequency.setBackground(0xffffffff);
        
        TextBox textBeaufort = c.addText(10, 80, "蒲福風級\nBeaufort Number\n7 or more\n4 thru 6\n1 thru 3", "kaiu.ttf", 10);
        textBeaufort.setAlignment(Chart.TopLeft);
        textBeaufort.setBackground(0xffffffff);
        
        c.addLine(270, 110, 225, 144, 0x000000, 1);//-
        c.addLine(235, 145, 225, 144, 0x000000, 1);///
        c.addLine(227, 135, 225, 144, 0x000000, 1);//\
        
        c.addLine(90,  120, 150, 120, 0x000000, 1); //-
        c.addLine(150, 120, 233, 185, 0x000000, 1); //\
        c.addLine(223, 188, 233, 185, 0x000000, 1); //
        c.addLine(228, 175, 233, 185, 0x000000, 1);
        
        c.addLine(90,  135, 130, 135, 0x000000, 1);
        c.addLine(130, 135, 233, 205, 0x000000, 1);
        c.addLine(223, 208, 233, 205, 0x000000, 1);
        c.addLine(228, 195, 233, 205, 0x000000, 1);
        
        c.addLine(90,  150, 120, 150, 0x000000, 1);
        c.addLine(120, 150, 213, 230, 0x000000, 1);
        c.addLine(205, 233, 213, 230, 0x000000, 1);
        c.addLine(208, 220, 213, 230, 0x000000, 1);
        
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
        c.angularAxis().setLabelStyle("Arial", 9, 0x000000).setBackground(0xffffffff);
        
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
        c.radialAxis().setLabelStyle("", 8, 0x000000).setBackground(0xffffffff);
        c.radialAxis().setColors(0x709999ff);
        c.radialAxis().setLabelGap(1);
        
        //the CALM
        TextBox textCALM = c.addText(centerX, centerY, "CALM\n"+CALM+"%", "Arial Bold", 12);
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
        	layer0.setDataLabelStyle("Arial Bold", 10).setPos(labelX, labelY);
        }
        
        //Create image
	    ResourceBundle resource = ResourceBundle.getBundle("file_config");
	    File dir = new File(resource.getString("imagePath"));
	    if(!dir.isDirectory()){
	    	//System.out.println("image dir not exit!");
	    	dir.mkdirs();
	    }

		MultiChart m = new MultiChart(794, 1123);
		m.addChart(200, 250, c);
		
		m.setOutputOptions("width=595; height = 842");
		
		if(index){
			TextBox title_1 = m.addText(400, 35, num + "." + " 風 花 圖" + "\n" + " Wind Rose" , "kaiu.ttf", 12);
			title_1.setAlignment(Chart.Center);
			title_1.setBackground(0xffffffff);
       	
			m.makeChart("tmp/tmp_summary/"+ filename + "_1.pdf");
		}else{
			TextBox title_1 = m.addText(400, 35, " 風 花 圖" + "\n" + " Wind Rose" , "kaiu.ttf", 12);
			title_1.setAlignment(Chart.Center);
			title_1.setBackground(0xffffffff);
        
			m.makeChart("tmp/tmp_surface/"+ filename + "_1.pdf");
		}
	    

    }

}

