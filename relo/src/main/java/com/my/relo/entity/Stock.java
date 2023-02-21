package com.my.relo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@SequenceGenerator(name = "stock_sequence_generator", // 제너레이터명
sequenceName = "stock_seq", // 시퀀스명
initialValue = 1, // 시작 값
allocationSize = 1 // 할당할 범위 사이즈
)
public class Stock {
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stock_sequence_generator")
    @Column(name = "s_num")
    private Long sNum;
    
    @ManyToOne
    @JoinColumn(name = "m_num")
    private Member member;
    
    @OneToOne
    @JoinColumn(name = "size_category_num")
    private Sizes sizes;
    
    @Column(name = "s_brand", nullable = false)
    private String sBrand;
    
    @Column(name = "s_name", nullable = false)
    private String sName;
    
    @Column(name = "s_origin_price", nullable = false)
    private Integer sOriginPrice;
    
    @Column(name = "s_hope_price")
    private Integer sHopePrice;
    
    @Column(name = "s_color", nullable = false)
    private String sColor;
    
    @Column(name = "s_type", nullable = false)
    private String sType;
    
    @Column(name = "s_grade")
    private String sGrade;
    
    @Column(name = "s_hope_days", nullable = false)
    private Integer sHopeDays;
    
    @Column(name = "seller_comment")
    private String sellerComment;
    
    @Column(name = "manager_comment")
    private String managerComment;
    
    @ColumnDefault(value = "1")
    @Column(name = "s_status")
    private Integer sStatus;
}