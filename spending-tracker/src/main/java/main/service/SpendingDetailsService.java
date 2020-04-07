package main.service;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import main.model.SpendingDetails;
import main.model.User;
import main.repository.SpendingDetailsRepository;

@Service
@Data
public class SpendingDetailsService {
	
	private List<SpendingDetails> spendingDetails;
	
	@Autowired
	private SpendingDetailsRepository spendingDetailsRepository;
	
	public List<SpendingDetails> getAllDetails(User user){
		spendingDetails = (List<SpendingDetails>) spendingDetailsRepository.findAll();
		spendingDetails = spendingDetails.stream().filter(detail->detail.getUser()!=null) 
				.filter(detail->detail.getUser().getUid()==user.getUid())
				 .filter(detail->(detail.getDate().getYear()+1900)==Calendar.getInstance().get(Calendar.YEAR))
				 .filter(detail->(detail.getDate().getMonth()+1)==(Calendar.getInstance().get(Calendar.MONTH)+1))
				.collect(Collectors.toList());
		return spendingDetails;
	}
	public String monthlyTotal(List<SpendingDetails> spendingDetails) {
		double total = spendingDetails.stream().mapToDouble(detail->detail.getAmount()).sum();
		DecimalFormat df = new DecimalFormat("###.##");
		return df.format(total);
	}
}
