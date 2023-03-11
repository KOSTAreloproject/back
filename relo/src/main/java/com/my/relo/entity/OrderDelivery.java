package com.my.relo.entity;

import java.io.Serializable;
import java.time.LocalDate;

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

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor 
@Builder
@DynamicInsert @DynamicUpdate
@Entity
@Table(name="order_delivery")
public class OrderDelivery implements Serializable {
	@Id
	@Column(name="a_num")
	private Long aNum;
	
	@OneToOne(optional=true,  fetch = FetchType.LAZY,  cascade=CascadeType.ALL)
	@MapsId
	@JoinColumn(name="a_num")
	private Orders orders;
	
	@ManyToOne
	@JoinColumn(name = "addr_num")
	private Address address;
	
	@Column(name = "d_status")
	@ColumnDefault(value = "0")
	private int dStatus;
	
	@Column(name = "d_tracking_info")
	private String dTrackingInfo;
	
	@Column(name = "d_complete_day")
	private LocalDate dCompleteDay;
	
	public void updateDStatus(Integer dStatus) {
		this.dStatus = dStatus;
	}
	
	public void updateAddress(Address address) {
		this.address = address;
	}
}
