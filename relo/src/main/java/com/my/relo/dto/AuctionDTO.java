package com.my.relo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Builder
@Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class AuctionDTO {
	private Long mNum;
	private Long aNum;
	private Long pNum;
	private Long sNum;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate pEndDate;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate aDate;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate aTime;
	private int pStatus;
	private String sGrade;
	private String sBrand;
	private String sColor;
	private String sName;
	private String sizeCategoryName;
	private int aPrice;
	private Long awNum;
}
