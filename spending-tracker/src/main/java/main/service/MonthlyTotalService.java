package main.service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import main.model.MonthlyTotal;
import main.model.User;
import main.repository.MonthlyTotalRepository;

@Service
@Data
public class MonthlyTotalService {
	@Autowired
	MonthlyTotalRepository monthlyTotalRepository;
	
	private List<MonthlyTotal> monthlyTotal;
	
	public List<MonthlyTotal> getAllMonthlyTotal(User user){
		monthlyTotal = (List<MonthlyTotal>) monthlyTotalRepository.findAll();
		monthlyTotal = monthlyTotal.stream().filter(month->month.getUser()!=null)
										   .filter(month->month.getUser().getUid()==user.getUid())
										   .filter(month->month.getDate().getYear()+1900==Calendar.getInstance().get(Calendar.YEAR))
										   .collect(Collectors.toList());
		for(MonthlyTotal m:monthlyTotal) {
			System.out.println(m.getMonth());
		}
		return monthlyTotal;
	}

}
