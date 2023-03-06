package com.my.relo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
@SpringBootTest
class LikesTest {
	
	@Autowired
	private LikesService ls;
	
	@DisplayName("좋아요+1 테스트")
	@Test
	void plusLikeTest() throws AddException {
		Long styleNum = 7L;
		Long mNum = 1L;
		ls.plusLikes(mNum, styleNum);
	}
	
	@DisplayName("좋아요-1 테스트")
	@Test
	void minusLikeTest() throws RemoveException, FindException {
		Long styleNum = 20L;
		Long mNum = 1L;
		ls.minusLikes(mNum, styleNum);
	}
}
