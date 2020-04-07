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

@Entity
@Data
public class MonthlyTotal {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int mid;
	private double total;
	private int month;
	@ManyToOne
	private User user;
	@Temporal(TemporalType.DATE)
	private Date date=new Date();
}
