package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.StyleTag;

public interface StyleTagRepository extends CrudRepository<StyleTag, Long>{
	
	@Query(value = "SELECT * FROM style_tag s WHERE style_num = ?1", nativeQuery = true)
	List<StyleTag> findByStyleNum(Long styleNum);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM style_tag s where s.style_num = :styleNum",	nativeQuery = true)
	void deleteByStyleNum(@Param("styleNum") Long styleNum);
}
