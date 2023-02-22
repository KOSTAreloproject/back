package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.my.relo.entity.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
	//해당 회원 주소록 리스트 출력 
	@Query(value= "SELECT ADDR_NUM, ADDR, ADDR_DETAIL, ADDR_NAME, ADDR_POST_NUM, ADDR_RECIPIENT, ADDR_TEL, ADDR_TYPE, M_NUM \n"
			+ "FROM ADDRESS\n"
			+ "WHERE M_NUM = ?1", nativeQuery = true)
	public List<Address> findBymNum(Long mNum);
	
	//기본 주소 출력
	@Query(value="SELECT * FROM ADDRESS WHERE M_NUM = :mNum AND ADDR_TYPE = 0", nativeQuery = true)
	public Address findByAddrType(@Param("mNum")Long mNum);
}
