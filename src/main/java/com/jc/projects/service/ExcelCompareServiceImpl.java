package com.jc.projects.service;

import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ExcelCompareServiceImpl implements ExcelCompareService{


    @Override
    public List<String> compareExcel(MultipartFile correctFile, MultipartFile toVerifyFile) {

        List<String> mismatches = new ArrayList<>();

        try (InputStream correctInput = correctFile.getInputStream();
             InputStream toVerifyInput = toVerifyFile.getInputStream();
             Workbook correctWorkbook = new XSSFWorkbook(correctInput);
             Workbook toVerifyWorkbook = new XSSFWorkbook(toVerifyInput)) {

            int sheets = correctWorkbook.getNumberOfSheets();
            log.info("Number of sheets to compare: {}", sheets);

            for (int i = 0; i < sheets; i++) {
                Sheet correctSheet = correctWorkbook.getSheetAt(i);
                Sheet verifySheet = toVerifyWorkbook.getSheetAt(i);
                String sheetName = correctSheet.getSheetName();

                log.info("Comparing sheet: {}", sheetName);

                int maxRows = Math.max(correctSheet.getLastRowNum(), verifySheet.getLastRowNum());

                for (int r = 0; r <= maxRows; r++) {
                    Row correctRow = correctSheet.getRow(r);
                    Row verifyRow = verifySheet.getRow(r);

                    int maxCols = Math.max(
                            correctRow != null ? correctRow.getLastCellNum() : 0,
                            verifyRow != null ? verifyRow.getLastCellNum() : 0
                    );

                    for (int c = 0; c < maxCols; c++) {
                        Cell correctCell = correctRow != null ? correctRow.getCell(c) : null;
                        Cell verifyCell = verifyRow != null ? verifyRow.getCell(c) : null;

                        String correctVal = getCellValue(correctCell);
                        String verifyVal = getCellValue(verifyCell);

                        if (!correctVal.equals(verifyVal)) {
                            String msg = String.format("Mismatch at Sheet: %s, Row: %d, Col: %d | Expected: '%s', Found: '%s'",
                                    sheetName, r + 1, c + 1, correctVal, verifyVal);
                            mismatches.add(msg);
                            log.info(msg);
                        }
                    }
                }
            }

        } catch (Exception e) {
            String errorMsg = "Error during file comparison: " + e.getMessage();
            log.error(errorMsg, e);
            mismatches.add(errorMsg);
        }
        if(mismatches.size() > 0){
            log.info("Comparison finished with {} mismatches found.", mismatches.size());
        }
        else{
            mismatches.add("Files have been verified successfully");
        }

        return mismatches;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getDateCellValue().toString()
                    : String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
