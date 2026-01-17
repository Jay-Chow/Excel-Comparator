package com.jc.projects.controller;


import com.jc.projects.service.ExcelCompareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class ExcelCompareController {

    @Autowired
    ExcelCompareService excelCompareService;

    @RequestMapping(method = RequestMethod.POST, path = "/compare")
    public ResponseEntity<?> compareExcel(@RequestParam MultipartFile correctExcel, @RequestParam MultipartFile toVerifyFile){

        List<String> mismatch = excelCompareService.compareExcel(correctExcel, toVerifyFile);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(mismatch);
    }
}
