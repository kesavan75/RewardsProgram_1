import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class RewardsProgram {
	
	public static void main(String args[]) {
		calculateRewards(getCustTransList());
	}
	
	//calculate transaction and rewards
	public static void calculateRewards(List<CustomerTransaction> custTransList) {
		
		//sort list of objects by name and date
		Collections.sort(custTransList, new Comparator() {

	        public int compare(Object o1, Object o2) {

	            String x1 = ((CustomerTransaction) o1).getCustName();
	            String x2 = ((CustomerTransaction) o2).getCustName();
	            int sComp = x1.compareTo(x2);

	            if (sComp != 0) {
	               return sComp;
	            } 

	            Date x3 = ((CustomerTransaction) o1).getDate();
	            Date x4 = ((CustomerTransaction) o2).getDate();
	            return x3.compareTo(x4);
	    }});
		
		try {
			String tempCustName = null;
			Date tempDate = null;
			double monthlyTransAmt = 0;
			double totalAmt = 0;
			List custDetailList = new ArrayList();
			CustomerTransactionDetail custTransDetail = null;
			for(int i=0; i<custTransList.size(); i++) {
				if(tempCustName != null && tempDate != null && 
						(!tempCustName.equals(custTransList.get(i).getCustName()) || 
						!tempDate.equals(custTransList.get(i).getDate()))) {
					if(tempDate.getMonth() != custTransList.get(i).getDate().getMonth()) {
						custTransDetail = new CustomerTransactionDetail();
						custTransDetail.setCustName(tempCustName);
						custTransDetail.setMonth(tempDate.getMonth());
						custTransDetail.setTransAmount(monthlyTransAmt);
						custTransDetail.setMonthlyRewardPts(calculateRewards(monthlyTransAmt));
						monthlyTransAmt = 0;
						custDetailList.add(custTransDetail);										
					}
					
					if(!tempCustName.equals(custTransList.get(i).getCustName())) {
						custTransDetail = new CustomerTransactionDetail();
						custTransDetail.setCustName(tempCustName);
						custTransDetail.setMonth(tempDate.getMonth());
						custTransDetail.setTransAmount(monthlyTransAmt);
						custTransDetail.setTotAmount(totalAmt);
						//calculate rewards
						custTransDetail.setMonthlyRewardPts(calculateRewards(monthlyTransAmt));
						custTransDetail.setTotRewardPts(calculateRewards(totalAmt));
						custDetailList.add(custTransDetail);
						totalAmt = 0;
						monthlyTransAmt = 0;
					}				
				}			
				
				tempCustName = custTransList.get(i).getCustName();
				tempDate = custTransList.get(i).getDate();	
				monthlyTransAmt = monthlyTransAmt + custTransList.get(i).getTransAmount();
				totalAmt = totalAmt + custTransList.get(i).getTransAmount();
				
				if(i == custTransList.size() - 1) {
					custTransDetail = new CustomerTransactionDetail();
					custTransDetail.setCustName(tempCustName);
					custTransDetail.setMonth(tempDate.getMonth());
					custTransDetail.setTransAmount(monthlyTransAmt);
					custTransDetail.setTotAmount(totalAmt);
					custTransDetail.setMonthlyRewardPts(calculateRewards(monthlyTransAmt));
					custTransDetail.setTotRewardPts(calculateRewards(totalAmt));
					custDetailList.add(custTransDetail);
				}								
			}
			
			//print the transaction results by month
			for(int j=0; j<custDetailList.size(); j++) {
				System.out.println("Customer Name: "+((CustomerTransactionDetail)custDetailList.get(j)).getCustName()+
				" Month: "+((CustomerTransactionDetail)custDetailList.get(j)).getMonth() +
				" Trans Amount: "+((CustomerTransactionDetail)custDetailList.get(j)).getTransAmount() +
				" Total Amount: "+((CustomerTransactionDetail)custDetailList.get(j)).getTotAmount() + 
				" Monthly Rewards Pts: "+((CustomerTransactionDetail)custDetailList.get(j)).getMonthlyRewardPts() + 
				" Total Rewards Pts: "+((CustomerTransactionDetail)custDetailList.get(j)).getTotRewardPts());
			}	
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//calculate rewards
	private static int calculateRewards(double transAmt) {		
		//calculate monthly rewards
		//dollar spent over $100		
		double minus100 = transAmt - 100;
		int over100pts = (int)minus100 * 2;			
		//dollar spent over $50
		double minus50 = transAmt - 50;
		int over50 = ((int)minus50/50) * 50 ;
		int over50pts = over50 * 1;
		int totPts = over100pts + over50pts;		
		
		return totPts;
	}
	
	//list of customer transaction data for testing purpose
	public static List getCustTransList() {
		ArrayList list = new ArrayList();
		CustomerTransaction custTransA = new CustomerTransaction();
		custTransA.setCustName("CustA");
		custTransA.setCity("CityA");
		custTransA.setTransAmount(120);
		custTransA.setDate(new Date(2022,8,20));
		
		CustomerTransaction custTransB = new CustomerTransaction();
		custTransB.setCustName("CustA");
		custTransB.setCity("CityB");
		custTransB.setTransAmount(130);
		custTransB.setDate(new Date(2022,8,21));
		
		CustomerTransaction custTransC = new CustomerTransaction();
		custTransC.setCustName("CustA");
		custTransC.setCity("CityC");
		custTransC.setTransAmount(140);
		custTransC.setDate(new Date(2022,9,22));
		
		CustomerTransaction custTransD = new CustomerTransaction();
		custTransD.setCustName("CustB");
		custTransD.setCity("CityD");
		custTransD.setTransAmount(120);
		custTransD.setDate(new Date(2022,9,20));
		
		CustomerTransaction custTransE = new CustomerTransaction();
		custTransE.setCustName("CustB");
		custTransE.setCity("CityE");
		custTransE.setTransAmount(140);
		custTransE.setDate(new Date(2022,9,21));
		
		CustomerTransaction custTransF = new CustomerTransaction();
		custTransF.setCustName("CustB");
		custTransF.setCity("CityF");
		custTransF.setTransAmount(150);
		custTransF.setDate(new Date(2022,10,22));
		
		CustomerTransaction custTransG = new CustomerTransaction();
		custTransG.setCustName("CustC");
		custTransG.setCity("CityG");
		custTransG.setTransAmount(130);
		custTransG.setDate(new Date(2022,10,20));
		
		CustomerTransaction custTransH = new CustomerTransaction();
		custTransH.setCustName("CustC");
		custTransH.setCity("CityH");
		custTransH.setTransAmount(130);
		custTransH.setDate(new Date(2022,10,21));
		
		list.add(custTransA);
		list.add(custTransB);
		list.add(custTransC);
		list.add(custTransD);
		list.add(custTransE);
		list.add(custTransF);
		list.add(custTransG);
		list.add(custTransH);
		
		return list;		
	}
}
