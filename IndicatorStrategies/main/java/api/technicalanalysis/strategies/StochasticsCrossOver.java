package api.technicalanalysis.strategies;

import java.io.IOException;
import java.time.ZonedDateTime;
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
import org.ta4j.core.indicators.StochasticOscillatorDIndicator;
import org.ta4j.core.indicators.StochasticOscillatorKIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;

import api.technicalanalysis.json.Main;
import api.technicalanalysis.json.Meta;
import api.technicalanalysis.json.Result;
import api.technicalanalysis.json.Strategies;
import api.technicalanalysis.json.stochastics.CrossOver;
import api.technicalanalysis.json.stochastics.Stochastics;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

	public class StochasticsCrossOver {		
		
		public CrossOver getStochasticsCrossOver(BarSeries series, int above) {
			CrossOver crossOver = new CrossOver();
			
			try {
				// Building the trading strategy
		        Strategy strategy = buildStrategy(series, above);		        
		        System.out.println(series.getFirstBar().getEndTime());
		        System.out.println(series.getLastBar().getEndTime());
		        // Running the strategy
		        BarSeriesManager seriesManager = new BarSeriesManager(series);
		        TradingRecord tradingRecord = seriesManager.run(strategy);
		        BarSeries barSeries = seriesManager.getBarSeries();
		        BaseTradingRecord baseTradingRecord = (BaseTradingRecord)tradingRecord;
		        List<Order> buyOrders = baseTradingRecord.getBuyOrders();
		        List<Order> sellOrders = baseTradingRecord.getSellOrders();
		        List timestamp = new ArrayList();
		        List close = new ArrayList();
		        ZonedDateTime zonedDateTime = barSeries.getLastBar().getEndTime();
		        for(Order order : buyOrders) {
		        	Bar bar = barSeries.getBar(order.getIndex());
		        	if(bar.getEndTime().isAfter(zonedDateTime.minusDays(60))) {
		        		timestamp.add(bar.getEndTime().toString());
		        		close.add(Double.parseDouble(bar.getClosePrice().toString()));
		        		//System.out.println("Stock "+arrStock[i]+" Type Buy "+j+" End time "+bar.getEndTime()+" Close price "+bar.getClosePrice());
		        	}
		        }
		        
		        for(Order order : sellOrders) {
		        	Bar bar = barSeries.getBar(order.getIndex());
		        	timestamp.add(bar.getEndTime().toString());
		        	close.add(Double.parseDouble(bar.getClosePrice().toString()));
		        	//System.out.println("Stock "+arrStock[i]+" Type Sell "+j+" End time "+bar.getEndTime()+" Close price "+bar.getClosePrice());        	
		        }		        
		        crossOver.setTimestamp(timestamp);
		        crossOver.setClose(close);
		        
		    }
	    	catch(Exception e) {	    		
	    		//e.printStackTrace();
	    		throw e;
	    	}
			
			return crossOver;
		}
		
		/**
	     * @param series a bar series
	     * @return a 2-period RSI strategy
	     */
	    public static Strategy buildStrategy(BarSeries series, int above) {
	        if (series == null) {
	            throw new IllegalArgumentException("Series cannot be null");
	        }
	        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
	        //StochasticOscillatorKIndicator sof = new StochasticOscillatorKIndicator(series, series.getBarCount());
	        StochasticOscillatorKIndicator sof = new StochasticOscillatorKIndicator(series, 14);
	        
	        StochasticOscillatorDIndicator sos = new StochasticOscillatorDIndicator(sof);

	        // Entry rule
	        //Rule entryRule = new CrossedUpIndicatorRule(sos, sof);

	        // Exit rule
	        //Rule exitRule = new CrossedDownIndicatorRule(sos, sof);
	        
	        // Entry rule
	        // The long-term trend is up when a security is above its 200-period SMA.          
			  Rule entryRule = new CrossedUpIndicatorRule(sos, above); // Trend 
				//	  .and(new CrossedDownIndicatorRule(sos, 30)); // Signal 1		 
	        
	        // Exit rule
	        // The long-term trend is down when a security is below its 200-period SMA.		
			  Rule exitRule = new CrossedDownIndicatorRule(sos, above); // Trend			 
					 // .and(new CrossedUpIndicatorRule(sos, 30)); // Signal 2
	                
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
	    
	    public String buildJson(CrossOver crossOver, Meta metabject) throws Exception {
	    	String jsonStr = null;
	    	Main main = new Main();
	    	Strategies strategies = new Strategies();
	    	Stochastics stochastics = new Stochastics();
	    	Result result = new Result();
	    	result.setMeta(metabject);
	    	result.setCrossOver(crossOver);
	    	stochastics.setResult(result);
	    	strategies.setStochastics(stochastics);
	    	main.setStrategies(strategies);
	    	
	    	// Creating Object of ObjectMapper define in Jakson Api 
	        ObjectMapper Obj = new ObjectMapper(); 
	        
	        try { 	        	  
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


