package com.my.relo.dto;



import lombok.Builder;
import lombok.Getter;


import lombok.ToString;

@Getter
@Builder
@ToString
public class StockDTO {
	private Long sNum;
	private String sName;
	private String sizeCategoryName;
	private Integer sStatus;
	private String sGrade;
	private String sBrand;
	private String sColor;
	private String sType;
	private String managerComment;
	private Integer sHopeDays;
	private Integer sOriginPrice;
	private Long mNum;
	
//	@Builder
//	public StockDTO(Long sNum, String sName, String sizeCategoryName, Integer sStatus, String sGrade, String sBrand,
//			String sColor, String sType, String managerComment, Integer sHopeDays, Integer sOriginPrice, Long mNum) {
//		this.sNum = sNum;
//		this.sName = sName;
//		this.sizeCategoryName = sizeCategoryName;
//		this.sStatus = sStatus;
//		this.sGrade = sGrade;
//		this.sBrand = sBrand;
//		this.sColor = sColor;
//		this.sType = sType;
//		this.managerComment = managerComment;
//		this.sHopeDays = sHopeDays;
//		this.sOriginPrice = sOriginPrice;
//		this.mNum = mNum;
//	}
	
	
	



	
	
	
}
