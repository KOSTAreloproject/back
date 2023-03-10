package com.my.relo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.my.relo.entity.Address;
import com.my.relo.entity.StockReturn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockReturnDTO {
   private Long sNum;
   private Address addr;
   private Integer srStatus;
   private String srTrackingInfo;
   @JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd")
   private LocalDate srStartDate;

   public StockReturn toEntity() {
      StockReturn stockreturn = StockReturn.builder().sNum(this.sNum).addr(this.addr)
            .srTrackingInfo(this.srTrackingInfo).build();

      return stockreturn;
   }
}