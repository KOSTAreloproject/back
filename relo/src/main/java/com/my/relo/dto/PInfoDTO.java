package com.my.relo.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@ToString
@Builder
public class PInfoDTO {
	private String sName;
	
	private String sizeCategoryName;
	
	private Integer pStatus;
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd")
	private Date pEndDate;
	
	private Integer sHopePrice;
	
	private String sGrade;
	
	private String sBrand;
	
	private Long pNum;
	
	private Long maxPrice;

//	@Builder
//	public PInfoDTO(String sName, String sizeCategoryName, Integer pStatus, Date pEndDate, Integer sHopePrice,
//			String sGrade, String sBrand, Long pNum, Long maxPrice) {
//		this.sName = sName;
//		this.sizeCategoryName = sizeCategoryName;
//		this.pStatus = pStatus;
//		this.pEndDate = pEndDate;
//		this.sHopePrice = sHopePrice;
//		this.sGrade = sGrade;
//		this.sBrand = sBrand;
//		this.pNum = pNum;
//		this.maxPrice = maxPrice;
//	}


	
}
