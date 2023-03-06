package com.my.relo.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.my.relo.dto.StockReturnDTO;
import com.my.relo.entity.Address;
import com.my.relo.entity.Stock;
import com.my.relo.entity.StockReturn;
import com.my.relo.exception.AddException;
import com.my.relo.exception.FindException;
import com.my.relo.repository.AddressRepository;
import com.my.relo.repository.MemberRepository;
import com.my.relo.repository.StockRepository;
import com.my.relo.repository.StockReturnRepository;

@Service
public class StockReturnService {
   @Autowired
   StockReturnRepository srr;

   @Autowired
   MemberRepository mr;

   @Autowired
   StockRepository sr;

   @Autowired
   AddressRepository ar;

   /**
    * 관리자가 재고반송을 추가한다.
    * 
    * @param mNum
    * @throws AddException
    */
   public void addStockReturn(Long mNum, Long sNum) throws AddException {

      Optional<Stock> optS = sr.findById(sNum);

      Stock s = optS.get();

      Address a = ar.findByAddrType(mNum);
      UUID u = UUID.randomUUID();

      StockReturn stockReturn = StockReturn.builder().sNum(s.getSNum()).s(s).addr(a).srTrackingInfo(String.valueOf(u)).srStartDate(LocalDate.now())
            .build();

      srr.save(stockReturn);
   }

   public Map<String, Object> selectByIdStockReturn(Long mNum, int currentPage) throws FindException {

      Address a = ar.findByAddrType(mNum);

      Pageable pageable = PageRequest.of(currentPage - 1, 5, Sort.by("sr_start_date"));
      Page<Object[]> pageSRList = srr.listById(mNum, pageable);
      List<Object[]> List = pageSRList.getContent();

      List<StockReturnDTO> list = new ArrayList<>();
      for (Object[] obj : List) {
         StockReturnDTO dto = StockReturnDTO.builder().sNum(Long.valueOf(String.valueOf(obj[0])))
               .srStartDate(LocalDate.parse(String.valueOf(obj[1]),
                     DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
               .srStatus(Integer.valueOf(String.valueOf(obj[2]))).srTrackingInfo(String.valueOf(obj[3])).addr(a)
               .build();

         list.add(dto);
      }

      Map<String, Object> resultMap = new HashMap<>();
      resultMap.put("list", list);
      resultMap.put("totalPageNum", pageSRList.getTotalPages());

      return resultMap;
   }
}