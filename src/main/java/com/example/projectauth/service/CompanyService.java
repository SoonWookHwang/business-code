package com.example.projectauth.service;

import com.example.projectauth.dto.CompanyOneResponseDto;
import com.example.projectauth.dto.CompanyResponseDto;
import com.example.projectauth.error.exception.BusinessException;
import com.example.projectauth.error.exception.ErrorCode;
import com.example.projectauth.model.Company;
import com.example.projectauth.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public void putDataforTest(Company company){
        companyRepository.save(company);
    }

    public CompanyResponseDto findByCompanyName(String companyName) {
        List<Company> foundCompanies = companyRepository.findAllByCompanyNameContains(companyName);
        if (foundCompanies.isEmpty()) {
            throw new BusinessException(ErrorCode.NOTFOUND_DATA);
        }
        return CompanyResponseDto
                .builder()
                .companyList(foundCompanies)
                .build();
    }

    public CompanyOneResponseDto findByCompanyRegistNum(String companyId) {
        Company foundCompany =
                companyRepository.findByCompanyRegistNum(companyId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOTFOUND_DATA));
        return CompanyOneResponseDto
                .builder()
                .company(foundCompany)
                .build();
    }

    public CompanyResponseDto findByMainProduct(String mainProduct) {
        List<Company> foundCompanies = companyRepository.findByMainProductContains(mainProduct);
        if (foundCompanies.isEmpty()) {
            throw new BusinessException(ErrorCode.NOTFOUND_DATA);
        }
        return CompanyResponseDto
                .builder()
                .companyList(foundCompanies)
                .build();
    }
}
