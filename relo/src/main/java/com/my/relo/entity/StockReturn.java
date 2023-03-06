package com.my.relo.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "stock_return")
public class StockReturn {

   @Id
   @Column(name = "s_num", nullable = false)
   private Long sNum;

   @MapsId
   @OneToOne(cascade = { CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE })
   @JoinColumn(name = "s_num")
   private Stock s;

   @OneToOne
   @JoinColumn(name = "addr_num")
   private Address addr;

   @ColumnDefault(value = "0")
   @Column(name = "sr_status", nullable = false)
   private Integer srStatus;

   @Column(name = "sr_tracking_info", nullable = false)
   private String srTrackingInfo;

   @ColumnDefault(value = "SYSDATE")
   @Column(name = "sr_start_date", nullable = false)
   private LocalDate srStartDate;

   @Builder
   public StockReturn(Long sNum, Stock s, Address addr, String srTrackingInfo,LocalDate srStartDate) {
      this.sNum = sNum;
      this.s = s;
      this.addr = addr;
      this.srTrackingInfo = srTrackingInfo;
      this.srStartDate = srStartDate;
   }

}