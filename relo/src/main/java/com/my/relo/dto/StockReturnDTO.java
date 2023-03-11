package com.my.relo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my.relo.entity.Address;
import com.my.relo.entity.Stock;
import com.my.relo.entity.StockReturn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockReturnDTO {
   private Long sNum;
   private Address addr;
   private Integer srStatus;
   private String srTrackingInfo;
   @JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd")
   private LocalDate srStartDate;
   private String sName;
   private String sBrand;
   private String sizeCategoryName;
   
   @Builder
   public StockReturnDTO(Long sNum, String sName, String sBrand, String sizeCategoryName, Integer srStatus) {
      this.sNum = sNum;
      this.sName = sName;
      this.sBrand = sBrand;
      this.sizeCategoryName = sizeCategoryName;
      this.srStatus = srStatus;
   }

}