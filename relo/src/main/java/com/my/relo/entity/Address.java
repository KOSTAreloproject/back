package com.my.relo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "address")
@SequenceGenerator(name = "address_sequence_generator", // 제너레이터명
sequenceName = "address_seq", // 시퀀스명
initialValue = 1, // 시작 값
allocationSize = 1 // 할당할 범위 사이즈
)
public class Address {
	
	/**
	 * 주소록 번호
	 */
	@Id
	@Column(name = "addr_num", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_sequence_generator")
	private Long addrNum; 

	@ManyToOne
	@JoinColumn(name = "m_num", nullable = false)
	private Member member;
	
	@Column(name = "addr_name", nullable = false)
	private String addrName; 

	@Column(name = "addr_post_num", nullable = false)
	private String addrPostNum; 

	@Column(name = "addr_tel", nullable = false)
	private String addrTel;

	/**
	 * 주소
	 */
	@Column(name = "addr", nullable = false)
	private String addr;
	
	@Column(name = "addr_detail", nullable = false)
	private String addrDetail;

	@Column(name = "addr_recipient", nullable = false)
	private String addrRecipient;

	/**
	 * 0 -> 기본 주소지
	 * 1 -> 나머지 주소지
	 */
	@Column(name = "addr_type")
	@ColumnDefault(value = "1")
	private Integer addrType;
	
}