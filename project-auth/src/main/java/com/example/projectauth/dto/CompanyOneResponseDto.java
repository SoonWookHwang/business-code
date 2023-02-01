package com.example.projectauth.dto;

import com.example.projectauth.model.Company;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyOneResponseDto {
    private Company company;
}
