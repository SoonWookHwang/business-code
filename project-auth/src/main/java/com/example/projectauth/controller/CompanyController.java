package com.example.projectauth.controller;

import com.example.projectauth.dto.CompanyOneResponseDto;
import com.example.projectauth.dto.CompanyResponseDto;
import com.example.projectauth.response.ApiUtils;
import com.example.projectauth.response.CommonResponse;
import com.example.projectauth.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ResponseBody
public class CompanyController {
    private final CompanyService companyService;

    /*
    회사명으로 검색어가 포함된 데이터 리스트 조회
    http://localhost:8080/api/company?comName=""
     */
    @GetMapping("/api/company")
    public CommonResponse<?> findByCompanyName(@RequestParam(name = "comName") String comName){
        try{
            CompanyResponseDto responseDto = companyService.findByCompanyName(comName);
            return ApiUtils.success(200, responseDto);
        }catch (Exception e){
            return ApiUtils.failed(404, e.getMessage());
        }
    }

    /*
    사업자 등록번호로 단건 조회
    http://localhost:8080/api/company?comId=""
     */
    @GetMapping("/api/company")
    public CommonResponse<?> findByCompanyRegistNum(@RequestParam(name = "comId") String comId) {
        try{
            CompanyOneResponseDto responseDto = companyService.findByCompanyRegistNum(comId);
            return ApiUtils.success(200, responseDto);
        }catch(Exception e){
            return ApiUtils.failed(404, e.getMessage());
        }
    }

    /*
    주요제품으로 검색어가 포함된 데이터 리스트 조회
    http://localhost:8080/api/company?mainProduct=""
     */
    @GetMapping("/api/company")
    public CommonResponse<?> findByMainProduct(@RequestParam(name = "mainProduct") String mainProduct) {
        try{
            CompanyResponseDto responseDto = companyService.findByMainProduct(mainProduct);
            return new CommonResponse<>(200,responseDto);
        }catch(Exception e){
            return new CommonResponse<>(404,e.getMessage());
        }
    }

}