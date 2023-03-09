package com.my.relo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.relo.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDTO {
   /**
    * 회원 번호
    */
   private Long mnum;

   private String id;

   private String pwd;

   private String tel;

   private String email;

   /**
    * 유형 - 관리자(1) / 판매자 및 구매자(0)
    */
   private Integer type;

   private String birth;

   private String name;

   /**
    * 탈퇴 여부 -> 탈퇴시 -1이 insert됨
    */
   private Integer outCk;

   @Builder
   public MemberDTO(Long mnum, String id, String pwd, String tel, String email, Integer type, String birth,
         String name, Integer outCk) {
      this.mnum = mnum;
      this.id = id;
      this.pwd = pwd;
      this.tel = tel;
      this.email = email;
      this.type = type;
      this.birth = birth;
      this.name = name;
      this.outCk = outCk;
      this.type = type;
   }

   public Member toEntity() {
      Member entity = Member.builder().id(this.id).pwd(this.pwd).tel(this.tel).email(this.email).type(this.type)
            .birth(this.birth).name(this.name).build();

      return entity;
   }
}