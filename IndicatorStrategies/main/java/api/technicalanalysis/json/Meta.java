package api.technicalanalysis.json;

public class Meta {
	
	private String currency;
	
	private String symbol;
	
	private String exchangeName;
	
	private String instrumentType;
	
	private long firstTradeDate;
	
	private long regularMarketTime;
	
	private long gmtoffset;
	
	private String timezone;
	
	private String exchangeTimezoneName;
	
	private double regularMarketPrice;
	
	private double chartPreviousClose;
	
	private int priceHint;
	
	private String dataGranularity;
	
	private String range = "";
	
	private String[] validRanges = {"1d","5d","1mo","3mo","6mo","1y","2y","5y","10y","ytd","max"};

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public long getFirstTradeDate() {
		return firstTradeDate;
	}

	public void setFirstTradeDate(long firstTradeDate) {
		this.firstTradeDate = firstTradeDate;
	}

	public long getRegularMarketTime() {
		return regularMarketTime;
	}

	public void setRegularMarketTime(long regularMarketTime) {
		this.regularMarketTime = regularMarketTime;
	}

	public long getGmtoffset() {
		return gmtoffset;
	}

	public void setGmtoffset(long gmtoffset) {
		this.gmtoffset = gmtoffset;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getExchangeTimezoneName() {
		return exchangeTimezoneName;
	}

	public void setExchangeTimezoneName(String exchangeTimezoneName) {
		this.exchangeTimezoneName = exchangeTimezoneName;
	}

	public double getRegularMarketPrice() {
		return regularMarketPrice;
	}

	public void setRegularMarketPrice(double regularMarketPrice) {
		this.regularMarketPrice = regularMarketPrice;
	}

	public double getChartPreviousClose() {
		return chartPreviousClose;
	}

	public void setChartPreviousClose(double chartPreviousClose) {
		this.chartPreviousClose = chartPreviousClose;
	}

	public int getPriceHint() {
		return priceHint;
	}

	public void setPriceHint(int priceHint) {
		this.priceHint = priceHint;
	}

	public String getDataGranularity() {
		return dataGranularity;
	}

	public void setDataGranularity(String dataGranularity) {
		this.dataGranularity = dataGranularity;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String[] getValidRanges() {
		return validRanges;
	}

	public void setValidRanges(String[] validRanges) {
		this.validRanges = validRanges;
	}

}
