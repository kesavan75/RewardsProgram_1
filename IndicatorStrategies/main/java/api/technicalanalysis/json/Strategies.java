package api.technicalanalysis.json;

import api.technicalanalysis.json.rsi.Rsi;
import api.technicalanalysis.json.rsi.Rsi2;
import api.technicalanalysis.json.stochastics.Stochastics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Strategies {
	
	private Rsi rsi;
	
	private Rsi2 rsi2;	
	
	private Stochastics stochastics;

	public Rsi getRsi() {
		return rsi;
	}

	public void setRsi(Rsi rsi) {
		this.rsi = rsi;
	}	

	public Stochastics getStochastics() {
		return stochastics;
	}

	public void setStochastics(Stochastics stochastics) {
		this.stochastics = stochastics;
	}
	
	public Rsi2 getRsi2() {
		return rsi2;
	}

	public void setRsi2(Rsi2 rsi2) {
		this.rsi2 = rsi2;
	}
	
	/*public static void main(String args[]) {
		// Creating Object of ObjectMapper define in Jakson Api 
        ObjectMapper Obj = new ObjectMapper(); 
        Strategies strategies = new Strategies();
        Rsi rsi = new Rsi();
        Result result = new Result();
        strategies.setRsi(rsi);
        stochastics.setResult(result);
        try { 
  
            // get Oraganisation object as a json string 
            String jsonStr = Obj.writeValueAsString(strategies); 
  
            // Displaying JSON String 
            System.out.println(jsonStr); 
        } 
  
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
	}
*/}
