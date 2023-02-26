package com.my.relo.dto;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StockDTO {
	private Long sNum;
	private Long mNum;
	private Long sizeCategoryNum;
	private String sizeCategoryName;
	private String sBrand;
	private String sName;
	private Integer sOriginPrice;
	private Integer sHopePrice;
	private String sColor;
	private String sType;
	private String sGrade;
	private Integer sHopeDays;
    private String sellerComment;
	private String managerComment;
	private Integer sStatus;
	
	

    
    public void stockSetMember(Long mNum) {
    	this.mNum = mNum;
    }
 

}
