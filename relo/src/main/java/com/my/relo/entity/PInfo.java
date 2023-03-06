package com.my.relo.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Immutable
@Table(name = "p_info")
@Getter
@ToString
@NoArgsConstructor @AllArgsConstructor
public class PInfo {
	
	@Id
	@Column(name = "p_num")
	private Long pNum;
	
	@Column(name = "s_num")
	private Long sNum;
	
	@Column(name = "s_brand")
	private String sBrand;
	
	@Column(name = "s_name")
	private String sName;
	
	@Column(name = "s_hope_price")
	private Integer sHopePrice;
	
	@Column(name = "s_color")
	private String sColor;
	
	@Column(name = "s_type")
	private String sType;
	
	@Column(name = "s_grade")
	private String sGrade;
	
	@Column(name = "s_hope_days")
	private Integer sHopeDays;
	
	@Column(name = "manager_comment")
	private String managerComment;
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd")
	@Column(name = "p_start_date")
	private Date pStartDate;
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd")
	@Column(name = "p_end_date")
	private Date pEndDate;
	
	@Column(name = "p_status")
	private Integer pStatus;
	
	@Column(name = "size_category_num")
	private Long sizeCategoryNum;
	
	@Column(name = "m_num")
	private Long mNum;
	


}
