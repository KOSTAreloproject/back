package com.my.relo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZPResponseDTO {
	private Long mnum;
	private Long pNum;
	private Long sNum;
	private String sBrand;
	private String sName;
	private String sType;
	private String sizeCategoryName;
	private String sColor;
	private Integer sOriginPrice;
	private Integer sHopePrice;
	private Integer maxPrice;
	private String sGrade;
	@JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime pendDate;
	private String managerComment;
	private Long totalcnt;

	@Builder
	public ZPResponseDTO(Long mnum, Long pNum, Long sNum, String sBrand, String sName, String sType,
			String sizeCategoryName, String sColor, Integer sOriginPrice, Integer sHopePrice, Integer maxPrice,
			String sGrade, LocalDateTime pendDate, String managerComment, Long totalcnt) {
		this.mnum = mnum;
		this.pNum = pNum;
		this.sNum = sNum;
		this.sBrand = sBrand;
		this.sName = sName;
		this.sType = sType;
		this.sizeCategoryName = sizeCategoryName;
		this.sColor = sColor;
		this.sOriginPrice = sOriginPrice;
		this.sHopePrice = sHopePrice;
		this.maxPrice = maxPrice;
		this.sGrade = sGrade;
		this.pendDate = pendDate;
		this.managerComment = managerComment;
	}

}