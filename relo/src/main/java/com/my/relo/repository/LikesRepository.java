package com.my.relo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.my.relo.entity.Likes;
import com.my.relo.entity.Style;

public interface LikesRepository extends CrudRepository<Likes, Style> {
	/**
	 * 좋아요 -1
	 * @param styleNum
	 * @param mNum
	 */
	@Modifying
	@Transactional
	@Query(value ="DELETE FROM likes l WHERE l.style_num = :styleNum AND l.m_num = :mNum",nativeQuery = true)
	public void deleteLikes(@Param("styleNum")Long styleNum,@Param("mNum")Long mNum);

	/**
	 * 해당 게시판에 있는 좋아요 모두 삭제
	 * @param styleNum
	 */
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM likes l WHERE style_num = :styleNum",nativeQuery = true)
	public void deleteByStyleNumList(@Param("styleNum")Long styleNum);

	/**
	 * 해당 게시판에 있는 좋아요 리스트 출력
	 * @param styleNum
	 * @return List<Likes>
	 */
	@Query(value ="SELECT * FROM likes l WHERE l.style_num = :styleNum",nativeQuery = true)
	public List<Likes> ListByStyleNum(@Param("styleNum")Long styleNum);

	/**
	 * 해당 되는 행이 있는 지 조회해서 출력
	 * @param mNum
	 * @param styleNum
	 * @return Likes
	 */
	@Query(value = "SELECT  m_num, style_num FROM likes WHERE m_num = :mNum AND style_num = :styleNum",nativeQuery = true)
	public Likes findBymNumAndStyleNum(@Param("mNum")Long mNum,@Param("styleNum")Long styleNum);
}
