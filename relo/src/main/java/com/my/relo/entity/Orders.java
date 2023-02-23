package com.my.relo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString

@DynamicInsert @DynamicUpdate
@Entity
@Table(name="orders")
public class Orders implements Serializable{
	@Id
	@Column(name="a_num")
	private Long aNum;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="a_num")
	private Award award;
	
	@ManyToOne(targetEntity = Member.class)
	@JoinColumn(name = "m_num")
	private Long m_num;
	
	@Column(name = "o_memo")
	private String oMemo;
	
	@Column(name = "o_date")
	@ColumnDefault(value = "SYSDATE")
	private LocalDate oDate;
	
	@OneToOne(mappedBy = "orders", cascade=CascadeType.ALL)
	private OrderDelivery oDelivery;                                               
}
