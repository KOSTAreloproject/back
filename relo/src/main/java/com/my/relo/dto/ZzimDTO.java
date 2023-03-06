package com.my.relo.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class ZzimDTO {
	private Long mNum;
	private Long pNum;
	private String sBrand;
	private String sName;
	private String sType;
	private String sizeCategoryName;
	private String sColor;
	private Integer hopePrice;
	private String sGrade;
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd hh:mm:ss")
	@Temporal(TemporalType.DATE)
	private Date pEndDate;
	private Integer maxPrice;
}