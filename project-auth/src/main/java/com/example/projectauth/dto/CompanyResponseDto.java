package com.example.projectauth.dto;

import com.example.projectauth.model.Company;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponseDto {
    private List<Company> companyList;

}
