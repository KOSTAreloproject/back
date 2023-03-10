package com.my.relo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
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
@Builder
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "orders")
public class Orders implements Serializable {
	@Id
	@Column(name = "a_num")
	private Long aNum;

	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@MapsId
	@JoinColumn(name = "a_num", referencedColumnName = "a_num")
	private Award award;

	@JoinColumn(name = "m_num", nullable = false, referencedColumnName = "m_num")
	private Long mNum;

	@Column(name = "o_memo")
	private String oMemo;

	@Column(name = "o_date")
	@ColumnDefault(value = "SYSDATE")
	private LocalDate oDate;

	@Column(name = "imp_uid")
	private String impUid;

	@OneToOne(mappedBy = "orders", optional = true, fetch = FetchType.LAZY)
	private OrderDelivery oDelivery;
}