package cwb.cmt.summary.dao;

import java.util.List;

import cwb.cmt.summary.model.StnMoves;

public interface StnMovesDao {
	
	public List<StnMoves> selectStnMoves();
}