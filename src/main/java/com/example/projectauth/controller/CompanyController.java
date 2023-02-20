package com.example.projectauth.controller;

import com.example.projectauth.controller.findDataWithParam.FindDataWithParam;
import com.example.projectauth.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
@RequiredArgsConstructor
@ResponseBody
@Slf4j
public class CompanyController {
    private final FindDataWithParam getMethods;

    @GetMapping("/api/company")
    public CommonResponse<?> findDataWithParam(@RequestParam HashMap<String,String> paramMap){
        String key = paramMap.keySet().toString();
        String value = paramMap.values().toString();

        value = value.replaceAll("\\[", "").replaceAll("]", "");
        key = key.replaceAll("\\[", "").replaceAll("]", "");


        return switch (key) {
            case "comName" -> getMethods.findByCompanyName(value);
            case "comId" -> getMethods.findByCompanyRegistNum(value);
            case "mainProduct" -> getMethods.findByMainProduct(value);
            default -> null;
        };
    }
}
