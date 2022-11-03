import java.util.Date;

public class CustomerTransactionDetail {
	
	private String custName;
	
	private int month;
	
	private double transAmount;
	
	private double totAmount;
	
	private int monthlyRewardPts;
	
	private int totRewardPts;
	
	private int error;

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public double getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(double transAmount) {
		this.transAmount = transAmount;
	}
	
	public double getTotAmount() {
		return totAmount;
	}

	public void setTotAmount(double totAmount) {
		this.totAmount = totAmount;
	}

	public int getMonthlyRewardPts() {
		return monthlyRewardPts;
	}

	public void setMonthlyRewardPts(int monthlyRewardPts) {
		this.monthlyRewardPts = monthlyRewardPts;
	}

	public int getTotRewardPts() {
		return totRewardPts;
	}

	public void setTotRewardPts(int totRewardPts) {
		this.totRewardPts = totRewardPts;
	}
	
	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}		

}
