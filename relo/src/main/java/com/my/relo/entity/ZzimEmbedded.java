package com.my.relo.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ZzimEmbedded implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -1842248313818646189L;

	private Long mNum;

	private Long pNum;

	@Builder
	public ZzimEmbedded(Long mNum, Long pNum) {
		this.mNum = mNum;
		this.pNum = pNum;
	}
}