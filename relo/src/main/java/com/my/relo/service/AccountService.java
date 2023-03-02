package com.my.relo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.AccountDTO;
import com.my.relo.entity.Account;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.AccountRepository;


@Service
public class AccountService {
   @Autowired
   private AccountRepository ar;


   /**
    * 회원의 계좌정보를 추가 또는 수정한다.
    * 
    * @param account : 회원이 입력한 계좌정보
    * @throws AddException : 계좌 추가/수정 오류
    * 
    */
   public void addAccount(AccountDTO account) throws AddException {
      Optional<Account> optA = ar.findById(account.getMNum());
      if (optA.isEmpty()) { // 계좌정보가 없을 때 INSERT
         Account ac = Account.builder()
         .mNum(account.getMNum())
         .bankAccount(account.getBankAccount())
         .bankCode(account.getBankCode())
         .build();
         
         ar.save(ac);
      } else { // 계좌정보가 있을때 UPDATE
    	  Account ac = optA.get();
    	  ac = Account.builder()
    		         .mNum(account.getMNum())
    		         .bankAccount(account.getBankAccount())
    		         .bankCode(account.getBankCode())
    		         .build();
         ar.save(ac);
      }

   }

   /**
    * 입력한 회원번호와 일치하는 계좌정보 찾기
    * 
    * @param mNum 회원번호
    * @return Account : 계좌정보
    */
   public AccountDTO readAccount(Long mNum) throws FindException {
     
      Optional<Account> optA = ar.findById(mNum);
      if (!optA.isPresent()) {// 있으면 그대로 진행, 없으면 throw FindException
         throw new FindException("입력한 회원번호와 일치하는 계좌정보가 없습니다.");
      }

      Account account = optA.get();
      AccountDTO aDTO = AccountDTO.builder()
    		  			.mNum(account.getMNum())
    		  			.bankAccount(account.getBankAccount())
    		  			.bankCode(account.getBankCode())
    		  			.build();
      return aDTO;
   }

   /**
    * 입력한 회원번호와 일치하는 계좌정보 삭제
    * 
    * @param mNum : 회원번호
    * @throws RemoveException : 삭제오류
    */
   public void deleteAccount(Long mNum) throws RemoveException {
      Optional<Account> optA = ar.findById(mNum);
      if (optA.isPresent()) {
         ar.deleteById(mNum);
      }
   }

}