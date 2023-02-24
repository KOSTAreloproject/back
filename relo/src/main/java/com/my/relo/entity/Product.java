package com.my.relo.entity;

import java.time.LocalDate;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@NoArgsConstructor
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
    

    @JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd")
    @Column(name = "p_start_date")
    @ColumnDefault(value = "SYSDATE")
    private LocalDate pStartDate;
    

    @JsonFormat(timezone = "Asia/Seoul", pattern = "yyyy-MM-dd")
    @Column(name = "p_end_date")
    private LocalDate pEndDate;
    
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

	@Builder
	public Product(LocalDate pEndDate, Integer pStatus, Stock stock, Long mNum) {
		this.pEndDate = pEndDate;
		this.pStatus = pStatus;
		this.stock = stock;
		this.mNum = mNum;
	}
    
	
    
}