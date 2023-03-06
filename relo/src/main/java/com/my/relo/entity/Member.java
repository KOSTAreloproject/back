package com.my.relo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
@SequenceGenerator(name = "member_sequence_generator", // 제너레이터명
sequenceName = "member_seq", // 시퀀스명
initialValue = 1, // 시작 값
allocationSize = 1 // 할당할 범위 사이즈
)
public class Member {
	
	/**
	 * 회원 번호
	 */
	@Id
	@Column(name = "m_num")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_sequence_generator")
	private Long mNum; 

	@Column
	private String id; 

	@Column
	private String pwd; 

	@Column
	private String tel;

	@Column
	private String email;
	
	/**
	 * 유형 - 관리자(1) / 판매자 및 구매자(1) 
	 */
	@Column
	private int type; 

	@Column
	private String birth;

	@Column
	private String name;
	
	/**
	 * 탈퇴 여부 - 탈퇴시 -1이 insert됨
	 */
	@Column
	private Integer outCk;
}
