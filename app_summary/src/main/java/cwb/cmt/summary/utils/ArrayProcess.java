package cwb.cmt.summary.utils;

public class ArrayProcess {
	
	// 前包後不包
	public static int[][] slice(int[][] source, int yFrom, int yTo, int xFrom, int xTo){
		int[][] des = new int [yTo-yFrom][xTo-xFrom];
		for(int i=yFrom; i<yTo; i++){
			for(int j=xFrom; j<xTo; j++){
				des[i-yFrom][j-xFrom] = source[i][j];
			}
		}
		return des;
	}
}
