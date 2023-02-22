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
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity 
@Table(name = "style_tag")
public class StyleTag {
	/**
	 * 스타일 태그 
	 */
	@EmbeddedId
	StyleTagEmbedded ste = new StyleTagEmbedded();
	
	@MapsId("styleNum")
	@ManyToOne
	@JoinColumn(name = "style_num", nullable = false)
	private Style style;
}
