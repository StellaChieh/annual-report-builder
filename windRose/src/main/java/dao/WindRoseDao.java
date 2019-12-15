package dao;

import java.util.List;
import java.util.Map;

import entity.Station;
import entity.WindRose;


public interface WindRoseDao {
	
	public Station getStation(String stno); 
	
	public int getTotalCount(Map<String,Object> condition);
	
	public List<WindRose> getWindRoseProps(Map<String,Object> condition);
	
	public int getTotalCountTY(Map<String,Object> condition);
	
	public List<WindRose> getWindRosePropsTY(Map<String,Object> condition);
}
