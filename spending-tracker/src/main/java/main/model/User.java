package main.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int uid;
	@NotNull
	@Column(unique=true)
	private String email;
	private String userName;
	private String password;
	private int code;
	@Temporal(TemporalType.DATE)
	private Date date = new Date();
	@OneToMany(mappedBy="user")
	private List<SpendingDetails> spendingDetails=new ArrayList<>();
	@OneToMany(mappedBy="user")
	private List<MonthlyTotal> monthlyTotal = new ArrayList<>();

}
