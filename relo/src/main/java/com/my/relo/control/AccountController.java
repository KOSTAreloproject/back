package com.my.relo.control;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.relo.dto.AccountDTO;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.service.AccountService;

@RestController
@RequestMapping("account/*")
public class AccountController {
	@Autowired
	AccountService accountservice;
	
	@PostMapping("add")
	public ResponseEntity<?> accountAdd(HttpSession session, 
			AccountDTO account) throws AddException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new AddException("로그인하세요");
		}
		
		account.setMember(mNum);
		
		accountservice.addAccount(account);
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
	
	@GetMapping("read")
	public ResponseEntity<?> readAccount(HttpSession session) throws FindException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new FindException("로그인하세요");
		}

		
		AccountDTO aDTO = accountservice.readAccount(mNum);
		
	return new ResponseEntity<>(aDTO,HttpStatus.OK);
	
	}
	
	@DeleteMapping("del")
	public ResponseEntity<?> deleteAccount(HttpSession session) throws RemoveException{
		
		Long mNum = (Long) session.getAttribute("logined");
		if (mNum == null) {
			throw new RemoveException("로그인하세요");
		}
		

		
		accountservice.deleteAccount(mNum);
		
	return new ResponseEntity<>(HttpStatus.OK);
	
	}
}
