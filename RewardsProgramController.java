import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/RewardsProgram")
public class RewardsProgramController {
	
	@GetMapping(path="/calculateRewards", produces = "application/json")
	public List<CustomerTransactionDetail> calculateRewards(@RequestParam(value = "custTransList") 
		List<CustomerTransaction> custTransList) {
		
		List custDetailList = new ArrayList();
		RewardsProgramHandler handler = new RewardsProgramHandler();
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
						custTransDetail.setMonthlyRewardPts(handler.calculateRewards(monthlyTransAmt));
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
						custTransDetail.setMonthlyRewardPts(handler.calculateRewards(monthlyTransAmt));
						custTransDetail.setTotRewardPts(handler.calculateRewards(totalAmt));
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
					custTransDetail.setMonthlyRewardPts(handler.calculateRewards(monthlyTransAmt));
					custTransDetail.setTotRewardPts(handler.calculateRewards(totalAmt));
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
			CustomerTransactionDetail custTransDetail = new CustomerTransactionDetail();
			custTransDetail.setError(400);
			System.out.println(e.getMessage());
		}	
		
		return custDetailList;		
	}
}
