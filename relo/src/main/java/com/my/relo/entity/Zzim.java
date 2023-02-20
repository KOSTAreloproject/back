package com.my.relo.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zzim")
public class Zzim {

	@EmbeddedId
	private ZzimEmbedded ze = new ZzimEmbedded(); // JPA버전에 따른 오류가 발생할 수도 있으니 new객체선언

	@MapsId("pNum")
	@ManyToOne
	@JoinColumn(name = "p_num")
	private Product product; // 상품번호

	@MapsId("mNum")
	@ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "m_num", nullable = false)
	private Long mNum;

}
