package main.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import main.model.MonthlyTotal;

@Repository
public interface MonthlyTotalRepository extends CrudRepository<MonthlyTotal,Integer>{

}
