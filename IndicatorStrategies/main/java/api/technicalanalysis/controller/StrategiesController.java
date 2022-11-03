package api.technicalanalysis.controller;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ta4j.core.BarSeries;

import api.technicalanalysis.json.Meta;
import api.technicalanalysis.json.rsi.Rsi2Prd;
import api.technicalanalysis.json.stochastics.CrossOver;
import api.technicalanalysis.loader.BarsLoader;
import api.technicalanalysis.strategies.Rsi2Strategy;
import api.technicalanalysis.strategies.RsiStrategies;
import api.technicalanalysis.strategies.StochasticsCrossOver;
import api.technicalanalysis.strategies.Util;

@RestController
@RequestMapping(path = "/IndicatorStrategies")
public class StrategiesController {
	
	@GetMapping(path="/GetHistoricalData", produces = "application/json")  
    public String GetHistoricalData(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String historicalData = "";
		
		try {
			BarsLoader barsLoader = new BarsLoader();
			historicalData = barsLoader.getHistoricalData(symbol, period1, period2, interval);		
		}
		catch(Exception e) {
			Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Unable to get historical data ", e);
		}
		
		return historicalData;		
	}
	
	@GetMapping(path="/GetStochasticsCrossOverAbove20", produces = "application/json")  
    public String GetStochasticsCrossOverAbove20(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		
		try {
				String historicalData = GetHistoricalData(symbol, period1, period2, interval);				
				BarSeries series = BarsLoader.loadStockSeries(historicalData);				
		        StochasticsCrossOver sCrossOver = new StochasticsCrossOver();	        
		        
		        CrossOver crossOver = sCrossOver.getStochasticsCrossOver(series, 20);
		        
		        Meta meta = Util.getMetaObject(historicalData);
		        
		        returnData = sCrossOver.buildJson(crossOver, meta);      

	       	}
	    	catch(Exception e) {
	    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Unable to get stochastics cross over ", e);
	    		//sb.append(e.getMessage());
	    		e.printStackTrace();
	    	}
		
		return returnData;
	}
	
	@GetMapping(path="/GetStochasticsCrossOverAbove30", produces = "application/json")  
    public String GetStochasticsCrossOverAbove30(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		
		try {
				String historicalData = GetHistoricalData(symbol, period1, period2, interval);				
				BarSeries series = BarsLoader.loadStockSeries(historicalData);				
		        StochasticsCrossOver sCrossOver = new StochasticsCrossOver();	        
		        
		        CrossOver crossOver = sCrossOver.getStochasticsCrossOver(series, 30);
		        
		        Meta meta = Util.getMetaObject(historicalData);
		        
		        returnData = sCrossOver.buildJson(crossOver, meta);      

	       	}
	    	catch(Exception e) {
	    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Unable to get stochastics cross over ", e);
	    		//sb.append(e.getMessage());
	    		e.printStackTrace();
	    	}
		
		return returnData;
	}
	
	@GetMapping(path="/GetStochasticsCrossOverAbove50", produces = "application/json")  
    public String GetStochasticsCrossOverAbove50(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		
		try {
				String historicalData = GetHistoricalData(symbol, period1, period2, interval);				
				BarSeries series = BarsLoader.loadStockSeries(historicalData);				
		        StochasticsCrossOver sCrossOver = new StochasticsCrossOver();	        
		        
		        CrossOver crossOver = sCrossOver.getStochasticsCrossOver(series, 50);
		        
		        Meta meta = Util.getMetaObject(historicalData);
		        
		        returnData = sCrossOver.buildJson(crossOver, meta);      

	       	}
	    	catch(Exception e) {
	    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Unable to get stochastics cross over ", e);
	    		//sb.append(e.getMessage());
	    		e.printStackTrace();
	    	}
		
		return returnData;
	}
	
	@GetMapping(path="/GetRsi7PrdAbove50", produces = "application/json")  
    public String GetRsi7PrdAbove50(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		RsiStrategies rsiStrategies = null;
		Map rsiMap = null;
		Meta meta = null;
		String errMsg = null;
		
		try {
			String historicalData = GetHistoricalData(symbol, period1, period2, interval);
			
			BarSeries series = BarsLoader.loadStockSeries(historicalData);				
	        
			rsiStrategies = new RsiStrategies();	        
	        
	        rsiMap = rsiStrategies.getRsiStrategy(series, 7, 50);
	        
	        meta = Util.getMetaObject(historicalData);		        
       	}
    	catch(Exception e) {
    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Unable to get rsi 7 period below 50 above 50", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		errMsg = "Exception occured while getting rsi 7 period above 50";
    	}
		
		try {
			returnData = rsiStrategies.buildJson(rsiMap, meta, errMsg);
		}
		catch(Exception e) {
			Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 7 period below 50 above 50", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		returnData = "Exception occured while getting rsi 7 period above 50";
		}
		
		return returnData;
	}
	
	@GetMapping(path="/GetRsi7PrdAbove20", produces = "application/json")  
    public String GetRsi7PrdAbove20(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		Map rsiMap = null;
		Meta meta = null;
		RsiStrategies rsiStrategies = null;
		String errMsg = null;
		
		try {
			String historicalData = GetHistoricalData(symbol, period1, period2, interval);
			
			BarSeries series = BarsLoader.loadStockSeries(historicalData);				
	        
			rsiStrategies = new RsiStrategies();	        
	        
	        rsiMap = rsiStrategies.getRsiStrategy(series, 7, 20);
	        
	        meta = Util.getMetaObject(historicalData);
		}
    	catch(Exception e) {
    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 7 period below 20 above 80", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		errMsg = "Exception occured while getting rsi 7 period above 20";
    	}
		
		try {
			returnData = rsiStrategies.buildJson(rsiMap, meta, errMsg);
		}
		catch(Exception e) {
			Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 7 period below 20 above 80", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		returnData = "Exception occured while getting rsi 7 period above 20";
		}
		
		return returnData;
	}
	
	@GetMapping(path="/GetRsi7PrdAbove30", produces = "application/json")  
    public String GetRsi7PrdAbove30(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		Map rsiMap = null;
		Meta meta = null;
		RsiStrategies rsiStrategies = null;
		String errMsg = null;
		
		try {
			String historicalData = GetHistoricalData(symbol, period1, period2, interval);
			
			BarSeries series = BarsLoader.loadStockSeries(historicalData);				
	        
			rsiStrategies = new RsiStrategies();	        
	        
	        rsiMap = rsiStrategies.getRsiStrategy(series, 7, 30);
	        
	        meta = Util.getMetaObject(historicalData);
		}
    	catch(Exception e) {
    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 7 period above 30", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		errMsg = "Exception occured while getting rsi 7 period above 30";
    	}
		
		try {
			returnData = rsiStrategies.buildJson(rsiMap, meta, errMsg);
		}
		catch(Exception e) {
			Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 7 period above 30", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		returnData = "Exception occured while getting rsi 7 period above 30";
		}
		
		return returnData;
	}
	
	@GetMapping(path="/GetRsi14PrdAbove50", produces = "application/json")  
    public String GetRsi14PrdAbove50(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		RsiStrategies rsiStrategies = null;
		Map rsiMap = null;
		Meta meta = null;
		String errMsg = null;
		
		try {
			String historicalData = GetHistoricalData(symbol, period1, period2, interval);
			
			BarSeries series = BarsLoader.loadStockSeries(historicalData);				
	        
			rsiStrategies = new RsiStrategies();	        
	        
	        rsiMap = rsiStrategies.getRsiStrategy(series, 14, 50);
	        
	        meta = Util.getMetaObject(historicalData);		        
       	}
    	catch(Exception e) {
    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Unable to get rsi 14 period below 50 above 50", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		errMsg = "Exception occured while getting rsi 14 period above 50";
    	}
		
		try {
			returnData = rsiStrategies.buildJson(rsiMap, meta, errMsg);
		}
		catch(Exception e) {
			Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 14 period below 50 above 50", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		returnData = "Exception occured while getting rsi 14 period above 50";
		}
		
		return returnData;
	}
	
	@GetMapping(path="/GetRsi14PrdAbove20", produces = "application/json")  
    public String GetRsi14PrdAbove20(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		Map rsiMap = null;
		Meta meta = null;
		RsiStrategies rsiStrategies = null;
		String errMsg = null;
		
		try {
			String historicalData = GetHistoricalData(symbol, period1, period2, interval);
			
			BarSeries series = BarsLoader.loadStockSeries(historicalData);				
	        
			rsiStrategies = new RsiStrategies();	        
	        
	        rsiMap = rsiStrategies.getRsiStrategy(series, 14, 20);
	        
	        meta = Util.getMetaObject(historicalData);
		}
    	catch(Exception e) {
    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 14 period above 20", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		errMsg = "Exception occured while getting rsi 14 period above 20";
    	}
		
		try {
			returnData = rsiStrategies.buildJson(rsiMap, meta, errMsg);
		}
		catch(Exception e) {
			Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 14 period above 20", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		returnData = "Exception occured while getting rsi 14 period above 20";
		}
		
		return returnData;
	}
	
	@GetMapping(path="/GetRsi14PrdAbove30", produces = "application/json")  
    public String GetRsi14PrdAbove30(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;
		Map rsiMap = null;
		Meta meta = null;
		RsiStrategies rsiStrategies = null;
		String errMsg = null;
		
		try {
			String historicalData = GetHistoricalData(symbol, period1, period2, interval);
			
			BarSeries series = BarsLoader.loadStockSeries(historicalData);				
	        
			rsiStrategies = new RsiStrategies();	        
	        
	        rsiMap = rsiStrategies.getRsiStrategy(series, 14, 30);
	        
	        meta = Util.getMetaObject(historicalData);
		}
    	catch(Exception e) {
    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 14 period above 30", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		errMsg = "Exception occured while getting rsi 14 period above 30";
    	}
		
		try {
			returnData = rsiStrategies.buildJson(rsiMap, meta, errMsg);
		}
		catch(Exception e) {
			Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi 14 period above 30", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		returnData = "Exception occured while getting rsi 14 period above 30";
		}
		
		return returnData;
	}	
	
	@GetMapping(path="/GetRsi2Below10Above90", produces = "application/json")  
    public String GetRsi2Below10Above90(@RequestParam(value = "symbol") String symbol,
    		@RequestParam(value = "period1") String period1, @RequestParam(value = "period2") String period2,
    		@RequestParam(value = "interval") String interval) {
		String returnData = null;		
		Meta meta = null;
		Rsi2Strategy rsi2Strategy = null;
		String errMsg = null;
		Rsi2Prd rsi2prd = null;
		
		try {
			String historicalData = GetHistoricalData(symbol, period1, period2, interval);
			
			BarSeries series = BarsLoader.loadStockSeries(historicalData);				
	        
			rsi2Strategy = new Rsi2Strategy();	        
	        
			rsi2prd = rsi2Strategy.getRsi2Below10Above90(series);
	        
	        meta = Util.getMetaObject(historicalData);
		}
    	catch(Exception e) {
    		Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi2 period below 10 above 90", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		errMsg = "Exception occured while getting rsi2 period below 10 above 90";
    	}
		
		try {
			returnData = rsi2Strategy.buildJson(rsi2prd, meta, errMsg);
		}
		catch(Exception e) {
			Logger.getLogger(StrategiesController.class.getName()).log(Level.SEVERE, "Exception occured while getting rsi2 period below 10 above 90", e);
    		//sb.append(e.getMessage());
    		e.printStackTrace();
    		returnData = "Exception occured while getting rsi2 period below 10 above 90";
		}
		
		return returnData;
	}
}
