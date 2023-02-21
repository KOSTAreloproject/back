package com.my.relo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor@ToString

public class AddressDTO {
	
	private Long addrNum;

	private Long mNum;
	
	private String addrName;
	
	private String addrPostNum;
	
	private String addrTel;
	
	private String addr;
	
	private String addrDetail;
	
	private String addrRecipient;
	
	private Integer addrType;
}
