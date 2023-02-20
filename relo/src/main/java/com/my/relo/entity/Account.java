package com.my.relo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode
@Entity
@Table(name = "account")
public class Account {

	@Id
	@Column(name = "m_num", nullable = false)
	private Long mNum; // 멤버번호

	@MapsId(value = "mNum")
	@OneToOne(cascade = { CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "m_num")
	private Member member; //식별관계

	@Column(name = "bank_account")
	private String bankAccount; // 계좌번호

	@Column(name = "bank_code")
	private String bankCode; // 은행코드

}
