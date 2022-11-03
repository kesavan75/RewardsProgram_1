package api.technicalanalysis.json.rsi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Rsi2Prd {
	
	private Above90 above90;
	
	private Below10 below10;
	
	public Above90 getAbove90() {
		return above90;
	}

	public void setAbove90(Above90 above90) {
		this.above90 = above90;
	}

	public Below10 getBelow10() {
		return below10;
	}

	public void setBelow10(Below10 below10) {
		this.below10 = below10;
	}	
}
