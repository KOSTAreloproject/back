package com.my.relo.entity;

import java.time.LocalDate;



import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
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
	
	@ColumnDefault(value = "0")
	@Column(name = "std_status", nullable = false)
	private Integer stdStatus;

	@Column(name = "std_tracking_info", nullable = false)
	private String stdTrackingInfo;
	

	@ColumnDefault(value = "SYSDATE")
	@Column(name = "std_start_date", nullable = false)
	private LocalDate stdStartDate;

	@Builder
	public StockReturn(Long sNum, Stock s, Address addr, String stdTrackingInfo) {
		this.sNum = sNum;
		this.s = s;
		this.addr = addr;
		this.stdTrackingInfo = stdTrackingInfo;
	}
	
	
}