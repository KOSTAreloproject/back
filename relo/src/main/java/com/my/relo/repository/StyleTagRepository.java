package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.StyleTag;

public interface StyleTagRepository extends CrudRepository<StyleTag, Long>{
	/**
	 * 선택된 게시판에 있는 태그 출력 
	 * @param styleNum
	 * @return List<StyleTag>
	 */
	@Query(value = "SELECT * FROM style_tag s WHERE style_num = ?1", nativeQuery = true)
	public List<StyleTag> findByStyleNum(Long styleNum);
	
	/**
	 * 선택된 게시판에 있는 태그 전체 삭제 
	 * @param styleNum
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM style_tag s where s.style_num = :styleNum",	nativeQuery = true)
	public void deleteByStyleNum(@Param("styleNum") Long styleNum);
	
	/**
	 * 태그 많은 순으로 출력 
	 * @return List<String>
	 */
	@Query(value = "SELECT hash_name FROM "
			+ "(SELECT hash_name, count(*) FROM style_tag group by hash_name order by count(*) desc) "
			+ "where rownum < 11", nativeQuery = true)
	public List<String> ListByCnt();
}
