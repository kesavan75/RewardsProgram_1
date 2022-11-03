package api.technicalanalysis.strategies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.technicalanalysis.json.Main;
import api.technicalanalysis.json.Meta;
import api.technicalanalysis.json.Result;
import api.technicalanalysis.json.Strategies;
import api.technicalanalysis.json.rsi.Above20;
import api.technicalanalysis.json.rsi.Above30;
import api.technicalanalysis.json.rsi.Above50;
import api.technicalanalysis.json.rsi.Rsi;
import api.technicalanalysis.json.rsi.Rsi14Prd;
import api.technicalanalysis.json.rsi.Rsi7Prd;

	public class RsiStrategies {		
		
		public Map getRsiStrategy(BarSeries series, int period, int above) {
			Map rsiMap = new HashMap();
			Rsi7Prd rsi7prd = new Rsi7Prd();
			Rsi14Prd rsi14prd = new Rsi14Prd();
			//Below50 below50 = new Below50();
			Above50 above50 = new Above50();
			Above20 above20 = new Above20();
			Above30 above30 = new Above30();
			//Below20 below20 = new Below20();
			//Above80 above80 = new Above80();
			
			try {
				// Building the trading strategy
		        Strategy strategy = buildStrategy(series, period, above);
	
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
		        /*if(below == 50) {
		        	below50.setTimestamp(timestamp);
		        	below50.setClose(close);
		        }
		        if(below == 20) {
		        	below20.setTimestamp(timestamp);
		        	below20.setClose(close);
		        }*/
		        
		        /*for(Order order : sellOrders) {
		        	Bar bar = barSeries.getBar(order.getIndex());
		        	timestamp.add(bar.getEndTime().toString());
		        	close.add(Double.parseDouble(bar.getClosePrice().toString()));
		        	//System.out.println("Stock "+arrStock[i]+" Type Sell "+j+" End time "+bar.getEndTime()+" Close price "+bar.getClosePrice());		        	
		        }*/
		        if(above == 50) {
		        	above50.setTimestamp(timestamp);
		        	above50.setClose(close);
		        }
		        else if(above == 20) {
		        	above20.setTimestamp(timestamp);
		        	above20.setClose(close);
		        }
		        else if(above == 30) {
		        	above30.setTimestamp(timestamp);
		        	above30.setClose(close);
		        }
		        /*else if(above == 80) {
		        	above80.setTimestamp(timestamp);
		        	above80.setClose(close);
		        }*/
		        
		        if(period == 7  && above == 50) {
		        	//rsi7prd.setBelow50(below50);
		        	rsi7prd.setAbove50(above50);
		        	rsiMap.put("Rsi7Ab50",rsi7prd);
		        }
		        else if(period == 7 && above == 20) {
		        	//rsi7prd.setBelow20(below20);
		        	rsi7prd.setAbove20(above20);
		        	rsiMap.put("Rsi7Ab20",rsi7prd);
		        }
		        else if(period == 7 && above == 30) {
		        	//rsi7prd.setBelow20(below20);
		        	rsi7prd.setAbove30(above30);
		        	rsiMap.put("Rsi7Ab30",rsi7prd);
		        }
		        else if(period == 14 && above == 50) {
		        	//rsi14prd.setBelow50(below50);
		        	rsi14prd.setAbove50(above50);
		        	rsiMap.put("Rsi14Ab50",rsi14prd);
		        }
		        else if(period == 14 && above == 20) {
		        	//rsi14prd.setBelow20(below20);
		        	rsi14prd.setAbove20(above20);
		        	rsiMap.put("Rsi14Ab20",rsi14prd);
		        }
		        else if(period == 14 && above == 30) {
		        	//rsi14prd.setBelow20(below20);
		        	rsi14prd.setAbove30(above30);
		        	rsiMap.put("Rsi14Ab30",rsi14prd);
		        }
		    }
	    	catch(Exception e) {	    		
	    		//e.printStackTrace();
	    		throw e;
	    	}
			
			return rsiMap;
		}
		
		/**
	     * @param series a bar series
	     * @return a 2-period RSI strategy
	     */
	    public static Strategy buildStrategy(BarSeries series, int period, int above) {
	        if (series == null) {
	            throw new IllegalArgumentException("Series cannot be null");
	        }

	        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
	        //SMAIndicator shortSma = new SMAIndicator(closePrice, 5);
	        //SMAIndicator longSma = new SMAIndicator(closePrice, 200);

	        // We use a 2-period RSI indicator to identify buying
	        // or selling opportunities within the bigger trend.
	        RSIIndicator rsi = new RSIIndicator(closePrice, period);

	        // Entry rule
	        // The long-term trend is up when a security is above its 200-period SMA.
	        Rule entryRule = new OverIndicatorRule(rsi, above); // Signal 1        		
	                //.and(new OverIndicatorRule(shortSma, closePrice)); // Signal 2

	        // Exit rule
	        // The long-term trend is down when a security is below its 200-period SMA.
	        //Rule exitRule = new CrossedUpIndicatorRule(rsi, above); // Signal 1
	        Rule exitRule = new UnderIndicatorRule(rsi, above);
	                
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
	    
	    public String buildJson(Map rsiMap, Meta metabject, String errMsg) throws Exception {
	    	String jsonStr = null;
	    	Main main = new Main();
	    	Strategies strategies = new Strategies();
	    	Rsi rsi = new Rsi();
	    	Result result = new Result();
	    	result.setMeta(metabject);
	    	
	    	if(rsiMap != null && rsiMap.get("Rsi7Ab50") != null) {
	    		result.setRsi7prd((Rsi7Prd)rsiMap.get("Rsi7Ab50"));
	    	}
	    	else if(rsiMap != null && rsiMap.get("Rsi7Ab20") != null) {
	    		result.setRsi7prd((Rsi7Prd)rsiMap.get("Rsi7Ab20"));
	    	}
	    	else if(rsiMap != null && rsiMap.get("Rsi7Ab30") != null) {
	    		result.setRsi7prd((Rsi7Prd)rsiMap.get("Rsi7Ab30"));
	    	}
	    	else if(rsiMap != null && rsiMap.get("Rsi14Ab50") != null) {
	    		result.setRsi14prd((Rsi14Prd)rsiMap.get("Rsi14Ab50"));
	    	}
	    	else if(rsiMap != null && rsiMap.get("Rsi14Ab20") != null) {
	    		result.setRsi14prd((Rsi14Prd)rsiMap.get("Rsi14Ab20"));
	    	}
	    	else if(rsiMap != null && rsiMap.get("Rsi14Ab30") != null) {
	    		result.setRsi14prd((Rsi14Prd)rsiMap.get("Rsi14Ab30"));
	    	}
	    	
	    	if(errMsg != null) {
	    		result.setErrMsg(errMsg);
	    	}
	    	
	    	rsi.setResult(result);
	    	strategies.setRsi(rsi);
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