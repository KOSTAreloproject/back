package com.my.relo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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

	@JoinColumn(name = "m_num", nullable = false, referencedColumnName = "m_num")
	private Long mNum;

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
	 * 0 -> 기본 주소지 1 -> 나머지 주소지
	 */
	@Column(name = "addr_type")
	@ColumnDefault(value = "1")
	private Integer addrType;

	@Builder
	public Address(Long addrNum, String addr, String addrDetail, String addrName, String addrPostNum,
			String addrRecipient, String addrTel, Long mNum, Integer addrType) {
		this.addrNum = addrNum;
		this.addr = addr;
		this.addrDetail = addrDetail;
		this.addrName = addrName;
		this.addrPostNum = addrPostNum;
		this.addrRecipient = addrRecipient;
		this.addrTel = addrTel;
		this.mNum = mNum;
		this.addrType = addrType;
	}

}