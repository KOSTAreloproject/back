package com.my.relo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.relo.dto.MemberDTO;
import com.my.relo.entity.Member;
import com.my.relo.exception.FindException;
import com.my.relo.exception.RemoveException;
import com.my.relo.repository.MemberRepository;

@Service
public class MemberService {

   @Autowired
   private MemberRepository mr;

   // 회원 가입
   public void join(MemberDTO dto) {
      mr.save(dto.toEntity());
   }

   // 아이디 및 비밀번호 찾기
   public String findIdAndPwd(String tel) {
      String id = mr.findIdAndPwd(tel);
      if(id != null) {
         return id;
      }
      return null;
   }
   
   // 아이디 중복 체크
   public MemberDTO idCheck(String id) {
      Member m = mr.checkId(id);

      if (m != null) {
         MemberDTO dto = MemberDTO.builder().id(m.getId()).pwd(m.getPwd()).mnum(m.getMNum()).type(m.getType()).build();
         return dto;
      } else {
         return null;
      }
   }

   // 회원 상세 프로필 조회
   public MemberDTO detailMember(Long mNum) throws FindException {
      Optional<Member> optM = mr.findById(mNum);
      if (!optM.isPresent())
         throw new FindException();
      else {
         Member m = optM.get();
         MemberDTO dto = MemberDTO.builder().id(m.getId()).pwd(m.getPwd()).email(m.getEmail()).birth(m.getBirth())
               .name(m.getName()).tel(m.getTel()).build();
         return dto;
      }
   }

   // 회원 정보 수정
   public void updateProfile(Long mNum,MemberDTO dto) throws FindException {
      Optional<Member> optM = mr.findById(mNum);
      if (!optM.isPresent()) {
         throw new FindException();
      } else {
         Member m = optM.get();

         if (dto.getPwd() == null) {
            dto = MemberDTO.builder().id(m.getId()).pwd(m.getPwd()).email(dto.getEmail()).tel(dto.getTel()).build();
         }

         if (dto.getEmail() == null) {
            dto = MemberDTO.builder().id(m.getId()).pwd(dto.getPwd()).email(m.getEmail()).tel(dto.getTel()).build();
         }

         if (dto.getTel() == null) {
            dto = MemberDTO.builder().id(m.getId()).pwd(dto.getPwd()).email(dto.getEmail()).tel(m.getTel()).build();
         }

         m.updateMember(dto);

         mr.save(m);
      }

   }

   // 회원 탈퇴시 id null로 변경
   public void updateIdNull(Long mNum) throws FindException {
      Optional<Member> optM = mr.findById(mNum);
      if (!optM.isPresent())
         throw new FindException();
      else {
         Member m = optM.get();

         MemberDTO dto = MemberDTO.builder().id(null).pwd(m.getPwd()).email(m.getEmail()).tel(m.getTel())

               .outCk(-1).build();

         m.updateMember(dto);

         mr.save(m);
      }
   }

   // 회원 탈퇴 여부 확인
   public Integer checkOutTerms(Long mNum) throws FindException {
      Optional<Member> optM = mr.findById(mNum);

      if (!optM.isPresent())
         throw new FindException();
      else {
         return mr.checkOutTerms(mNum);
      }

   }

   // 회원 삭제
   public void deleteMember(Long mNum) throws RemoveException, FindException {
      Optional<Member> optM = mr.findById(mNum);

      if (!optM.isPresent())
         throw new FindException();
      else {
         Member m = optM.get();
         if (m.getId() == null && m.getOutCk() == -1) {
            mr.deleteById(mNum);
         } else {
            throw new RemoveException();
         }
      }
   }
}