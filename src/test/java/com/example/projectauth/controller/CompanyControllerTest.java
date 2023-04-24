package com.example.projectauth.controller;

import com.example.projectauth.model.Company;
import com.example.projectauth.repository.CompanyRepository;
import com.example.projectauth.service.CompanyService;
import jakarta.persistence.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


class CompanyControllerTest {

    @Autowired
    private CompanyService companyService;

    private Company company;


    @BeforeEach
    void setUp() {
        this.company = Company.builder()
                .companyId(1L)
                .companyName("삼성 현대 엘지 기아")
                .typeOfBusiness("계속사업자")
                .busiSector("제조업, 소매업, 축산가공업")
                .phoneNum("010-1234-5678")
                .faxNum("02-123-4567")
                .companySize("100억")
                .clasfiCorporation("법인")
                .headOrBranch("본사")
                .corpType("주식회사")
                .taxOffice("없음")
                .establishmentDate("2024-02-02")
                .companyEmail("example@gmail.com")
                .ownerName("김엔젤")
                .ownerStatus("생존")
                .registNum("64738291928")
                .companyRegistNum("128321952")
                .taxType("법인세")
                .mainProduct("소고기 돼지고기")
                .cntWorkerNum("25000")
                .homepage("www.example.co.kr")
                .websiteOfIr("www.exampleIR.com")
                .postCode("123-456")
                .address("서울시 영등포구 영등포동 134-12")
                .settlementMonth("3월")
                .build();
    }

    @Test
    void findByCompanyName() {
        Assertions.assertEquals(company.companyToResponsDtoforTest(this.company),companyService.findByCompanyName("삼성"));

    }

    @Test
    void findByCompanyRegistNum() {
    }

    @Test
    void findByMainProduct() {
    }
}

   /*"삼성 현대 엘지 기아" , "계속사업자", "제조업, 소매업, 축산가공업", "010-1234-5678", "02-123-4567", "100억", "법인", "본사", "주식회사", "없음", "2024-02-02", "example@gmail.com", "김엔젤", "생존", "64738291928", "128321952", "법인세", "소고기 돼지고기",  "25000", "www.example.co.kr", "www.exampleIR.com", "123-456", "서울시 영등포구 영등포동 134-12", "3월"

   insert into COMPANY VALUES (
   1,
   '삼성 현대 엘지 기아' ,
   '계속사업자',
   '제조업, 소매업, 축산가공업',
   '010-1234-5678',
   '02-123-4567',
   '100억',
   '법인',
   '본사',
   '주식회사',
   '없음',
   '2024-02-02',
   'example@gmail.com',
   '김엔젤',
   '생존',
   '64738291928',
   '128321952',
   '법인세',
   '소고기 돼지고기',
   '25000',
   'www.example.co.kr',
   'www.exampleIR.com',
   '123-456',
   '서울시 영등포구 영등포동 134-12',
   '3월'
   )

    */