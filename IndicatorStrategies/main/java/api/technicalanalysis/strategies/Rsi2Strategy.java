package api.technicalanalysis.strategies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import api.technicalanalysis.json.Main;
import api.technicalanalysis.json.Meta;
import api.technicalanalysis.json.Result;
import api.technicalanalysis.json.Strategies;
import api.technicalanalysis.json.rsi.Above90;
import api.technicalanalysis.json.rsi.Below10;
import api.technicalanalysis.json.rsi.Rsi2;
import api.technicalanalysis.json.rsi.Rsi2Prd;
import api.technicalanalysis.json.rsi.Rsi7Prd;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

	public class Rsi2Strategy {		
		
		public Rsi2Prd getRsi2Below10Above90(BarSeries series) {
			Rsi2Prd rsi2prd = new Rsi2Prd();
			Below10 below10 = new Below10();
			Above90 above90 = new Above90();
			
			try {
				// Building the trading strategy
		        Strategy strategy = buildStrategy(series);
	
		        // Running the strategy
		        BarSeriesManager seriesManager = new BarSeriesManager(series);
		        TradingRecord tradingRecord = seriesManager.run(strategy);
		        BarSeries barSeries = seriesManager.getBarSeries();
		        BaseTradingRecord baseTradingRecord = (BaseTradingRecord)tradingRecord;		        
		        List<Order> buyOrders = baseTradingRecord.getBuyOrders();
		        List<Order> sellOrders = baseTradingRecord.getSellOrders();		       		        		        
		        
		        List timestamp = new ArrayList();
		        List close = new ArrayList();
		        for(Order order : buyOrders) {
		        	Bar bar = barSeries.getBar(order.getIndex());
		        	timestamp.add(bar.getEndTime().toString());
		        	close.add(Double.parseDouble(bar.getClosePrice().toString()));
		        	//System.out.println("Stock "+arrStock[i]+" Type Buy "+j+" End time "+bar.getEndTime()+" Close price "+bar.getClosePrice());	        	
		        }
		        
		        below10.setTimestamp(timestamp);
		        below10.setClose(close);		        
		        		        
		        for(Order order : sellOrders) {
		        	Bar bar = barSeries.getBar(order.getIndex());
		        	timestamp.add(bar.getEndTime().toString());
		        	close.add(Double.parseDouble(bar.getClosePrice().toString()));
		        	//System.out.println("Stock "+arrStock[i]+" Type Sell "+j+" End time "+bar.getEndTime()+" Close price "+bar.getClosePrice());		        	
		        }
		        
		        above90.setTimestamp(timestamp);
		        above90.setClose(close);	        		        
		    }
	    	catch(Exception e) {	    		
	    		//e.printStackTrace();
	    		throw e;
	    	}
			
			rsi2prd.setAbove90(above90);
			rsi2prd.setBelow10(below10);
			
			return rsi2prd;
		}
		
		/**
	     * @param series a bar series
	     * @return a 2-period RSI strategy
	     */
	    public static Strategy buildStrategy(BarSeries series) {
	        if (series == null) {
	            throw new IllegalArgumentException("Series cannot be null");
	        }

	        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
	        SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
	        SMAIndicator longSma = new SMAIndicator(closePrice, 200);

	        // We use a 2-period RSI indicator to identify buying
	        // or selling opportunities within the bigger trend.
	        RSIIndicator rsi = new RSIIndicator(closePrice, 2);

	        // Entry rule
	        // The long-term trend is up when a security is above its 200-period SMA.
	        Rule entryRule = new OverIndicatorRule(shortSma, longSma) // Trend
	                .and(new CrossedDownIndicatorRule(rsi, 10)) // Signal 1        		
	                .and(new OverIndicatorRule(shortSma, closePrice)); // Signal 2

	        // Exit rule
	        // The long-term trend is down when a security is below its 200-period SMA.
	        Rule exitRule = new UnderIndicatorRule(shortSma, longSma) // Trend
	                .and(new CrossedUpIndicatorRule(rsi, 90)) // Signal 1        		
	                .and(new UnderIndicatorRule(shortSma, closePrice)); // Signal 2

	        // TODO: Finalize the strategy

	        return new BaseStrategy(entryRule, exitRule);
	    }
		  
	    /*public static BarSeries loadStockSeries(@RequestParam(value = "symbol") String symbol,
	    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
	    		@RequestParam(value = "interval") String interval) {
	    	
	    	BarSeries series = new BaseBarSeries(symbol+"_stock");
	    	
	    	try {			
	    		HttpResponse<String> response = Unirest.get("https://query1.finance.yahoo.com/v7/finance/chart/"+symbol+"?symbol="+symbol+"&period1="+period1+"&period2="+period2+"&interval="+interval+"&indicators=quote&includeTimestamps=true").asString();
	    		String barSeriesJson = response.getBody();
	    		JSONObject topObj = new JSONObject(barSeriesJson);
	    		Object error = topObj.getJSONObject("chart").get("error");
	    		if (!error.toString().equals("null")) {
	    		    System.err.println(error.toString());
	    		    return null;
	    		}
	    		JSONArray results = topObj.getJSONObject("chart").getJSONArray("result");
	    		if (results == null || results.length() != 1) {
	    		    return null;
	    		}
	    		JSONObject result = results.getJSONObject(0);
	    		JSONArray timestamps = result.getJSONArray("timestamp");
	    		JSONObject indicators = result.getJSONObject("indicators");
	    		JSONArray quotes = indicators.getJSONArray("quote");
	    		if (quotes == null || quotes.length() != 1) {
	    		    return null;
	    		}
	    		JSONObject quote = quotes.getJSONObject(0);
	    		JSONArray adjcloses = indicators.getJSONArray("adjclose");
	    		if (adjcloses == null || adjcloses.length() != 1) {
	    		   return null;
	    		}
	    		JSONArray adjclose = adjcloses.getJSONObject(0).getJSONArray("adjclose");
	    		JSONArray open = quote.getJSONArray("open");
	    		JSONArray close = quote.getJSONArray("close");
	    		JSONArray high = quote.getJSONArray("high");
	    		JSONArray low = quote.getJSONArray("low");
	    		JSONArray volume = quote.getJSONArray("volume");
	    		List<String[]> listStock = null;
	    		for(int i=0; i<timestamps.length(); i++) {
	    			//System.out.println("timestamp "+timestamps.getLong(i)+" open "+open.getDouble(i)+" high "+high.getDouble(i)+" close "+close.getDouble(i)+" low "+low.getDouble(i));
	    			ZonedDateTime date =
	    					  ZonedDateTime
	    					  .ofInstant(Instant.ofEpochMilli(timestamps.getLong(i) * 1000), ZoneId.systemDefault());
	    			series.addBar(date, open.getDouble(i), high.getDouble(i), low.getDouble(i), close.getDouble(i), volume.getLong(i));    			
	    		} 		    		 
	    	}    	
	    	catch (Exception e) {
	    	    Logger.getLogger(BarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars ", e);
	    	}     	
	        
	        return series;        
	    }
	    
		@GetMapping(path="/GetCrossOver", produces = "application/json")
		public String GetCrossOver() {
			return "Test";
		}
		*/
	    
	    public String buildJson(Rsi2Prd rsi2prd, Meta metabject, String errMsg) throws Exception {
	    	String jsonStr = null;
	    	Main main = new Main();
	    	Strategies strategies = new Strategies();
	    	Rsi2 rsi2 = new Rsi2();
	    	Result result = new Result();
	    	result.setMeta(metabject);	    	
	    	result.setRsi2prd(rsi2prd);	    		    	
	    	
	    	if(errMsg != null) {
	    		result.setErrMsg(errMsg);
	    	}
	    	
	    	rsi2.setResult(result);
	    	strategies.setRsi2(rsi2);
	    	main.setStrategies(strategies);
	    	
	    	// Creating Object of ObjectMapper define in Jakson Api 
	        ObjectMapper Obj = new ObjectMapper(); 
	        
	        try { 	        	  
	            // get Oraganisation object as a json string	            
	        	Obj.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);
	            jsonStr = Obj.writeValueAsString(main);            
	        } 
	  
	        catch (IOException e) { 
	            //e.printStackTrace(); 
	        	throw e;
	        }     	
	    	
	        return jsonStr;
	    }		
	}