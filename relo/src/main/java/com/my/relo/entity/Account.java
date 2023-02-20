package com.my.relo.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Account implements Serializable{
	@Id
	@OneToOne(targetEntity = Member.class)
	@JoinColumn(name = "m_num", nullable = false)
	private Long mNum;

	@Column(name = "bank_account")
	private String bankAccount; // 계좌번호

	@Column(name = "bank_code")
	private String bankCode; // 은행코드

}