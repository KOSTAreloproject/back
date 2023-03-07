package com.my.relo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.my.relo.dto.MemberDTO;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	private String id; 

	@NotNull
	private String pwd; 

	@NotNull
	private String tel;

	@NotNull
	private String email;
	
	/**
	 * 유형 - 관리자(1) / 판매자 및 구매자(0) 
	 */
	@NotNull
	private Integer type; 

	@NotNull
	private String birth;

	@NotNull
	private String name;
	
	/**
	 * 탈퇴 여부 -> 탈퇴시 -1이 insert됨
	 */
	
	@Column(name = "out_ck")
	private Integer outCk;
	
	@OneToOne(mappedBy = "member")
	private Account account;
	
	@Builder
	public Member
	(String id, String pwd, String tel,
			String email, Integer type, 
			String birth, String name, Long mNum) {
		this.id = id;
		this.pwd = pwd;
		this.tel = tel;
		this.email = email;
		this.type = type;
		this.birth = birth;
		this.name = name;
		this.mNum = mNum;
	}
	
	public void updateMember(MemberDTO dto) {
		this.id = dto.getId();
		this.pwd = dto.getPwd();
		this.tel = dto.getTel();
		this.email = dto.getEmail();
		this.outCk = dto.getOutCk();
	}
}