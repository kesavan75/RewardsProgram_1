package api.technicalanalysis.json.rsi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Rsi14Prd {
	
	private Above50 above50;
	
	private Below50 below50;
	
	private Above80 above80;
	
	private Below20 below20;
	
	private Above20 above20;	
	
	private Above30 above30;	

	public Above80 getAbove80() {
		return above80;
	}

	public void setAbove80(Above80 above80) {
		this.above80 = above80;
	}

	public Below20 getBelow20() {
		return below20;
	}

	public void setBelow20(Below20 below20) {
		this.below20 = below20;
	}

	public Above50 getAbove50() {
		return above50;
	}

	public void setAbove50(Above50 above50) {
		this.above50 = above50;
	}

	public Below50 getBelow50() {
		return below50;
	}

	public void setBelow50(Below50 below50) {
		this.below50 = below50;
	}
	
	public Above20 getAbove20() {
		return above20;
	}

	public void setAbove20(Above20 above20) {
		this.above20 = above20;
	}

	public Above30 getAbove30() {
		return above30;
	}

	public void setAbove30(Above30 above30) {
		this.above30 = above30;
	}
}
