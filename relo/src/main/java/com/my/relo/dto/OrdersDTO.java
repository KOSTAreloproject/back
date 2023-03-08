package com.my.relo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersDTO {
	private Long mNum;
	private Long aNum;
	private Long pNum;
	private Long sNum;
	private String sName;
	private String sBrand;
	private String sColor;
	private String sGrade;
	private String sizeCategoryName;
	private Integer aPrice;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate oDate;
	private String oMemo;
	
	private String impUid;
	private int dStatus;
	private String dTrackingInfo;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private LocalDate dCompleteDay;
	
	private Long addrNum; 
	private String addrPostNum; 
	private String addrTel;
	private String addr;
	private String addrDetail;
	private String addrRecipient;
	
}
