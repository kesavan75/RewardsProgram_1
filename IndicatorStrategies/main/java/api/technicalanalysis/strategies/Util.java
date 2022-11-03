package api.technicalanalysis.strategies;

import org.json.JSONArray;
import org.json.JSONObject;

import api.technicalanalysis.json.Meta;

public class Util {
	
	public static Meta getMetaObject(String historicalData) throws Exception {
    	Meta metaObject = new Meta();
    	
    	try {	    		
	    	JSONObject topObj = new JSONObject(historicalData);
    		Object error = topObj.getJSONObject("chart").get("error");
    		if (!error.toString().equals("null")) {
    		    //System.err.println(error.toString());
    		    return null;
    		}
    		JSONArray results = topObj.getJSONObject("chart").getJSONArray("result");
    		if (results == null || results.length() != 1) {
    		    return null;
    		}
    		JSONObject result = results.getJSONObject(0);
    		
    		JSONObject metaJson = result.getJSONObject("meta");
    		metaObject.setCurrency(metaJson.getString("currency"));
    		metaObject.setSymbol(metaJson.getString("symbol"));
    		metaObject.setExchangeName(metaJson.getString("exchangeName"));
    		metaObject.setInstrumentType(metaJson.getString("instrumentType"));
    		metaObject.setFirstTradeDate(metaJson.getLong("firstTradeDate"));
    		metaObject.setRegularMarketTime(metaJson.getLong("regularMarketTime"));
    		metaObject.setGmtoffset(metaJson.getLong("gmtoffset"));
    		metaObject.setTimezone(metaJson.getString("timezone"));
    		metaObject.setExchangeTimezoneName(metaJson.getString("exchangeTimezoneName"));
    		metaObject.setRegularMarketPrice(metaJson.getDouble("regularMarketPrice"));
    		metaObject.setChartPreviousClose(metaJson.getDouble("chartPreviousClose"));
    		metaObject.setPriceHint(metaJson.getInt("priceHint"));
    		metaObject.setDataGranularity(metaJson.getString("dataGranularity"));
    		metaObject.setRange(metaJson.getString("range"));
    		
	    }
    	catch (Exception e) {
    		//Logger.getLogger(BarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars ", e);
    		throw e;
    	} 

    	return metaObject;

    }

}
