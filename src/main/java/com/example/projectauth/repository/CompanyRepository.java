package com.example.projectauth.repository;

import com.example.projectauth.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {

    List<Company> findAllByCompanyNameContains(String param);
    Optional<Company> findByCompanyRegistNum(String param);
    List<Company> findByMainProductContains(String param);
}
