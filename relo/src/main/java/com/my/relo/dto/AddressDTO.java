package com.my.relo.dto;

import com.my.relo.entity.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@AllArgsConstructor@NoArgsConstructor@ToString

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
	
	
	@Builder
	public AddressDTO(String addr, String addrDetail, String addrName, String addrPostNum, String addrRecipient,
			String addrTel, Long mNum, Integer addrType) {
		this.addr = addr;
		this.addrDetail = addrDetail;
		this.addrName = addrName;
		this.addrPostNum = addrPostNum;
		this.addrRecipient = addrRecipient;
		this.addrTel = addrTel;
		this.mNum = mNum;
		this.addrType = addrType;
	}
	
	public Address toEntity() {
		return Address.builder()
				.addrNum(addrNum).addr(addr).addrDetail(addrDetail).addrName(addrName)
				.addrPostNum(addrPostNum).addrRecipient(addrRecipient)
				.addrTel(addrTel).addrType(addrType).mNum(mNum).build();
	}
}
