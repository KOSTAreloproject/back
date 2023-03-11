package com.my.relo.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zzim")
public class Zzim {

	@EmbeddedId
	private ZzimEmbedded ze = new ZzimEmbedded();

	@MapsId("mnum")
	@ManyToOne
	@JoinColumn(name = "m_num")
	private Member member; // 멤버번호

	@MapsId("pNum")
	@ManyToOne
	@JoinColumn(name = "p_num")
	private Product product; // 상품번호

}
