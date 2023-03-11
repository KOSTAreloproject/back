package com.my.relo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "account")
public class Account {

	@Id
	@Column(name = "m_num")
	private Long mNum;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "m_num", referencedColumnName = "m_num", nullable = false)
	private Member member;

	@Column(name = "bank_account", nullable = false)
	private String bankAccount; // 계좌번호

	@Column(name = "bank_code", nullable = false)
	private String bankCode; // 은행코드

	@Builder
	public Account(Long mNum, String bankAccount, String bankCode) {
		this.mNum = mNum;
		this.bankAccount = bankAccount;
		this.bankCode = bankCode;
	}

}