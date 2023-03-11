package com.my.relo.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
@Entity
@Table(name = "likes")
public class Likes {
	/**
	 * 좋아요
	 */
	@EmbeddedId
	private LikesEmbedded le = new LikesEmbedded();

	@Builder
	public Likes(LikesEmbedded le) {
		this.le = le;
	}
}
