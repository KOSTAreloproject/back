package com.my.relo.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name="order_delivery")
public class OrderDelivery implements Serializable {
	@Id
	@Column(name="a_num")
	private Long aNum;
	
	@OneToOne(optional=true, cascade=CascadeType.ALL)
	@MapsId
	@JoinColumn(name="a_num")
	private Orders orders;
	
	@OneToOne
	@JoinColumn(name = "addr_num")
	private Address address;
	
	@Column(name = "d_status")
	private int dStatus;
	
	@Column(name = "d_tracking_info")
	private String dTrackingInfo;
	
	@Column(name = "d_complete_day")
	private LocalDate dCompleteDay;
}
