package com.jc.projects.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelCompareService {
    public List<String> compareExcel(MultipartFile correctExcel, MultipartFile toVerifyFile);
}
