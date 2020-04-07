package main.model;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class SpendingDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	
	private long sid;
	private double amount;
	private String purpose;
	private String payMethod;
	@Temporal(TemporalType.DATE)
	private Date date = new Date();
	@ManyToOne
	private User user;

}
