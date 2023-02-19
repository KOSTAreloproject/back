package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.my.relo.entity.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {

	@Query(value= "SELECT ADDR_NUM, ADDR, ADDR_DETAIL, ADDR_NAME, ADDR_POST_NUM, ADDR_RECIPIENT, ADDR_TEL, ADDR_TYPE, M_NUM \n"
			+ "FROM ADDRESS\n"
			+ "WHERE M_NUM = ?1", nativeQuery = true)
	List<Address> findBymNum(Long mNum);
}
