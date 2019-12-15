package cwb.cmt.upperair.utils;

public enum StandardColumn {
	
	NLH("Nlh", "NLHMCwwapp"),
	P("StnPres", "P", "p", 9999.9, DigitType.ONE_DECIMAL_PLACE),
	// in fact, the special value of "High" in database is "/////",
	// but we already replace that value to 999 in sql
	H("High", "H", "h", 999, DigitType.INTEGER),
	T("Tx", "T", "t", 999.9, DigitType.ONE_DECIMAL_PLACE),
	U("RH", "U", "u", 999, DigitType.INTEGER),
	Td("Td", "Td", "td", 999.9, DigitType.ONE_DECIMAL_PLACE),
	dd("WD", "dd", "dd", 999, DigitType.INTEGER),
	ff("WS", "ff", "ff", 999.9, DigitType.ONE_DECIMAL_PLACE);
	
	
	String columnNameInDb;
	String columnNameInBook;
	String variableNameInModel;
	double specialNumber;

	// record the printed digits on book: 
	// whether it is integer or one decimal place number  
	DigitType digitType;

	StandardColumn(String columnNameInDb, String columnNameInBook){
		this.columnNameInDb = columnNameInDb; 
		this.columnNameInBook = columnNameInBook;
	}
	
	StandardColumn(String columnNameInDb, String columnNameInBook, String variableNameInModel, double specialNumber, DigitType digitType){
		this.columnNameInDb = columnNameInDb; 
		this.columnNameInBook = columnNameInBook;
		this.variableNameInModel = variableNameInModel;
		this.specialNumber = specialNumber;
		this.digitType = digitType;
	}

	public double specialValue() {
		return this.specialNumber;
	}
	
	public DigitType digitType() {
		return this.digitType;
	}
	
	public String getVariableNameInModelClass() {
		return this.variableNameInModel;
	}
	
	public String getColumnNameInBook() {
		return columnNameInBook;
	}
}
