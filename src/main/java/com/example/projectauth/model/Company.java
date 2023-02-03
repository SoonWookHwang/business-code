package com.example.projectauth.model;

import com.example.projectauth.dto.CompanyResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "company")
@AllArgsConstructor // 테스트용
@Builder  // 테스트용
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long companyId;

    @Column  //회사명
    private String companyName;

    @Column  //업태
    private String typeOfBusiness;

    @Column  //종목
    private String busiSector;

    @Column  //전화번호
    private String phoneNum;

    @Column  //팩스번호
    private String faxNum;

    @Column  //기업규모
    private String companySize;

    @Column  //법인구분
    private String clasfiCorporation;

    @Column  //본사/지사 구분 1= 본사 2= 지사
    private String headOrBranch;

    @Column  //법인형태
    private String corpType;

    @Column  //세무서
    private String taxOffice;

    @Column  //  설립일(신고일)
    private String establishmentDate;

    @Column  //  회사이메일
    private String companyEmail;

    @Column  // 대표자명
    private String ownerName;

    @Column  // 사업자 현재 상태
    private String ownerStatus;

    @Column  // 법인등록번호.
    private String registNum;

    @Column  // 사업자등록번호
    private String companyRegistNum;

    @Column  // 과세유형
    private String taxType;

    @Column  // 주요제품
    private String mainProduct;

    @Column  // 종업원수
    private String cntWorkerNum;

    @Column  // 홈페이지
    private String homepage;

    @Column  // IR홈페이지
    private String websiteOfIr;

    @Column  // 우편번호
    private String postCode;

    @Column  // 회사주소
    private String address;

    @Column  /// 결산월
    private String settlementMonth;


    // 테스트용
    public CompanyResponseDto companyToResponsDtoforTest(Company company){
        return CompanyResponseDto.builder().companyList(Collections.singletonList(company)).build();
    }

}
