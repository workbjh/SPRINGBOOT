package com.mc.bookmanager.rent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;

import com.mc.bookmanager.member.Member;

import lombok.Data;

@Data
@Entity
public class Rent {
	
	@Id
	@GeneratedValue
	private Long rmIdx;
	
	// create문에서 컬럼명을 지정한 뒤 작성하는 쿼리를 columnDefinition에 설정
	// create Rent(regDate timestamp default now()...)
	@Column(columnDefinition = "timestamp default now()")
	private LocalDateTime regDate;

	@ColumnDefault("false")
	private Boolean isReturn;
	
	private String title;
	
	@ColumnDefault("0")
	private Integer rentBookCnt;
	
	@ManyToOne()
	@JoinColumn(name = "userId")
	private Member member;
	
	//CascadeType
	// PERSIST : PERSIST를 수행할 때 연관엔티티도 함께 수행, RENT가 테이블에 저장될때 RENTBOOK도 함께 저장
	// REMOVE  : 엔티티를 삭제할 때 연관엔티티도 함께 삭제
	// MERGE   : 준영속상태인 엔티티를 MERGE해서 영속상태로 만들 때 연관엔티티도 함께 영속상태로 만듦
	// DETACH  : 영속상태인 엔티티를 준영속상태로 만들 때 연관엔티티도 함께 수행
	// ALL	   : PERSIST + REMOVE + MERGE + DETACH
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "rent")
	private List<RentBook> rentBooks = new ArrayList<>();
	
	
	
	
	
	
	
	
	
	
	

}