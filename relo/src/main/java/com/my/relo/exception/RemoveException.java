package com.my.relo.exception;

public class RemoveException extends Exception {
	public RemoveException() {
		super(); //부모생성자 호출
		// TODO Auto-generated constructor stub
	}

	public RemoveException(String message) {
		super(message); //예외 상세 내용을 넣고 싶으면 여기(message)에 상세 내용을 넣어주면 된다
		// TODO Auto-generated constructor stub
	}
}
