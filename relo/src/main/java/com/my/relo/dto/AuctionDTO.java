package com.my.relo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	private String id;
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
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime tenDate;
}