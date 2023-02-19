package com.my.relo.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock_return")
public class StockReturn {

	@Id
	@Column(name = "s_num", nullable = false)
	private Long sNum;

	@MapsId
	@OneToOne(cascade = { CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "s_num")
	private Stock s;
	
	@OneToOne
	@JoinColumn(name = "addr_num")
	private Address addr;

	@Column(name = "std_status", nullable = false)
	private Integer stdStatus;

	@Column(name = "std_tracking_info", nullable = false)
	private String stdTrackingInfo;

	@Column(name = "std_start_date", nullable = false)
	private Date stdStartDate;
}