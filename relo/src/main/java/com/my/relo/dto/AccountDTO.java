package com.my.relo.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@Builder
public class AccountDTO {

   private Long mNum;
   private String bankAccount;
   private String bankCode;
   
   public void setMember(Long mNum) {
	   this.mNum = mNum;
   }
}