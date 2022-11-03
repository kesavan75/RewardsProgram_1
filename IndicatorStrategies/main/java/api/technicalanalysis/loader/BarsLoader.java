/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2019 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package api.technicalanalysis.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.opencsv.CSVReader;

/**
 * This class build a Ta4j bar series from a CSV file containing bars.
 */

public class BarsLoader implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6239692278324915341L;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      
    public String getHistoricalData(String symbol, String period1,
    		String period2,	String interval) throws Exception {    	
    	HttpResponse<String> response = null;
    	String historicalData = null;
    	try {
    		
    		response = Unirest.get("https://query1.finance.yahoo.com/v7/finance/chart/"+symbol+"?symbol="+symbol+"&period1="+period1+"&period2="+period2+"&interval="+interval+"&indicators=quote&includeTimestamps=true").asString();
    		historicalData = response.getBody();
    		 		    		 
    	}    	
    	catch (Exception e) {    		
    	    throw e;
    	}
    	
        return historicalData;        
    }
    
    public static BarSeries loadStockSeries(String historicalData) throws Exception {
    	
    	BarSeries series = new BaseBarSeries("series_stock");
    	
    	try {			
    		//HttpResponse<String> response = Unirest.get("https://query1.finance.yahoo.com/v7/finance/chart/"+symbol+"?symbol="+symbol+"&period1=1588712400&period2=1595635016&interval=1d&indicators=quote&includeTimestamps=true").asString();
    		//String barSeriesJson = response.getBody();
    		JSONObject topObj = new JSONObject(historicalData);
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
    	    //Logger.getLogger(BarsLoader.class.getName()).log(Level.SEVERE, "Unable to load bars ", e);
    		throw e;
    	}     	
        
        return series;        
    }

  }