package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.StyleTag;

public interface StyleTagRepository extends CrudRepository<StyleTag, Long>{
	//해당 스타일게시판에 있는 태그 출력 
	@Query(value = "SELECT * FROM style_tag s WHERE style_num = ?1", nativeQuery = true)
	public List<StyleTag> findByStyleNum(Long styleNum);
	
	//해당 스타일게시판에 있는 태그 전체 삭제
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM style_tag s where s.style_num = :styleNum",	nativeQuery = true)
	public void deleteByStyleNum(@Param("styleNum") Long styleNum);
	
	//해시태그 인기순으로 출력 
	@Query(value = "SELECT hash_name FROM "
			+ "(SELECT hash_name, count(*) FROM style_tag group by hash_name order by count(*) desc) "
			+ "where rownum < 11", nativeQuery = true)
	public List<String> ListByCnt();
}
