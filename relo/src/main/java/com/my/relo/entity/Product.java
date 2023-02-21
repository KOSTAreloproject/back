package com.my.relo.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@DynamicInsert
@DynamicUpdate
@SequenceGenerator(name = "product_sequence_generator", // 제너레이터명
sequenceName = "product_seq", // 시퀀스명
initialValue = 1, // 시작 값
allocationSize = 1 // 할당할 범위 사이즈
)
public class Product {
    @Id
    @Column(name = "p_num")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence_generator")
    private Long pNum;
    
    @Column(name = "p_start_date")
    @ColumnDefault(value = "SYSDATE")
    private Date pStartDate;
    
    @Column(name = "p_end_date")
    private Date pEndDate;
    
    @Column(name = "p_status")
    private Integer pStatus;

    @OneToOne
    @JoinColumn(name = "s_num")
    private Stock stock;
    
	@JoinColumn(name = "m_num", nullable = false,referencedColumnName="m_num")
	private Long mNum;
    
	@OneToMany(mappedBy ="aNum" , fetch = FetchType.LAZY)
	private List<Auction> Auction;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private List<Zzim> Zzim;
    
    
}