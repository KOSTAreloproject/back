package com.my.relo.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity 
@Table(name = "likes")
public class Likes {
	/**
	 * 좋아요 
	 */
	@EmbeddedId
	private LikesEmbedded le = new LikesEmbedded();

}
