package main.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import main.model.SpendingDetails;

@Repository
public interface SpendingDetailsRepository extends CrudRepository<SpendingDetails,Long>{

}
