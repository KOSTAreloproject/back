package com.my.relo.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ZzimDTO {
	private int mNum;
	private int pNum;
	private String sBrand;
	private String sName;
	private String sType;
	private String sizeCategoryName;
	private String sColor;
	private Integer hopePrice;
	private String sGrade;
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd hh:mm:ss")
	private Date pEndDate;
	private Integer maxPrice;

	@Builder
	public ZzimDTO(int mNum, int pNum, String sBrand, String sName, String sType, String sizeCategoryName,
			String sColor, Integer hopePrice, String sGrade, Date pEndDate, Integer maxPrice) {
		this.mNum = mNum;
		this.pNum = pNum;
		this.sBrand = sBrand;
		this.sName = sName;
		this.sType = sType;
		this.sizeCategoryName = sizeCategoryName;
		this.sColor = sColor;
		this.hopePrice = hopePrice;
		this.sGrade = sGrade;
		this.pEndDate = pEndDate;
		this.maxPrice = maxPrice;
	}

//	@Builder
//	public ZzimDTO(int mNum, int pNum) {
//		this.mNum = mNum;
//		this.pNum = pNum;
//	}

}