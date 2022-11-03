package api.technicalanalysis.json;

import api.technicalanalysis.json.rsi.Rsi14Prd;
import api.technicalanalysis.json.rsi.Rsi2Prd;
import api.technicalanalysis.json.rsi.Rsi7Prd;
import api.technicalanalysis.json.stochastics.CrossOver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class Result {
	
	private Meta meta;	
	
	@JsonInclude(Include.NON_NULL)
	private CrossOver crossOver;
	
	@JsonInclude(Include.NON_NULL)
	private Rsi7Prd rsi7prd;
	
	@JsonInclude(Include.NON_NULL)
	private Rsi14Prd rsi14prd;	
	
	@JsonInclude(Include.NON_NULL)
	private Rsi2Prd rsi2prd;	
	
	private String errMsg;
	
	public CrossOver getCrossOver() {
		return crossOver;
	}

	public void setCrossOver(CrossOver crossOver) {
		this.crossOver = crossOver;
	}
	
	public Rsi7Prd getRsi7prd() {
		return rsi7prd;
	}

	public void setRsi7prd(Rsi7Prd rsi7prd) {
		this.rsi7prd = rsi7prd;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public Rsi2Prd getRsi2prd() {
		return rsi2prd;
	}

	public void setRsi2prd(Rsi2Prd rsi2prd) {
		this.rsi2prd = rsi2prd;
	}
	
	public Rsi14Prd getRsi14prd() {
		return rsi14prd;
	}

	public void setRsi14prd(Rsi14Prd rsi14prd) {
		this.rsi14prd = rsi14prd;
	}

}
