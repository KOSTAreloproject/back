package com.my.relo.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(FindException.class)
	@ResponseBody
	public ResponseEntity<?> findExceptionHandler(FindException e){
		System.out.println("----- FindException ControllerAdvice--------");
		e.printStackTrace();
		Map<String, String> map = new HashMap<>();
		map.put("msg", e.getMessage());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;charset=UTF-8"); //"text/html;charset=utf-8"

		return new ResponseEntity<>(map, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AddException.class)
	@ResponseBody
	public ResponseEntity<?> addExceptionHandler(AddException e){
		System.out.println("-----AddException ControllerAdvice--------");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;charset=UTF-8"); //"text/html;charset=utf-8"

		return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RemoveException.class)
	@ResponseBody
	public ResponseEntity<?> removeExceptionHandler(RemoveException e){
		System.out.println("-----RemoveException ControllerAdvice--------");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;charset=UTF-8"); //"text/html;charset=utf-8"

		return new ResponseEntity<>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public ResponseEntity<?> exceptMaxUploadSize(MaxUploadSizeExceededException e){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;charset=UTF-8"); //"text/html;charset=utf-8"
		headers.add("Access-Control-Allow-Origin", "http://192.168.0.37:5500");
		headers.add("Access-Control-Allow-Credentials", "true");//쿠키허용
		return new ResponseEntity<>("파일크기가 너무 큽니다", headers, HttpStatus.BAD_REQUEST);
	}
}