package com.my.relo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class OrderInfoDTO {
	private String name;
	private String email;
	private String tel;
	private Long sNum;
	private String sGrade;
	private String sBrand;
	private String sColor;
	private String sName;
	private String sizeCategoryName;
	private List<AddressDTO> addrList;
	private Integer aPrice;
	private int applyNum;
	private String impUid;
	private String status;
}
