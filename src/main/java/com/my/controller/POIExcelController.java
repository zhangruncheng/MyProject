//package com.my.controller;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.compress.compressors.FileNameUtil;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author : zhangruncheng
// * @Date : 2019-09-23  17:27
// * @Version : 1.0.0
// **/
//
//@RestController
//@RequestMapping("/excel/poi")
//@Slf4j
//public class POIExcelController {
//
//    @PostMapping("/testFile")
//    public String getExcle(HttpServletRequest request, Integer size) {
//        MultipartHttpServletRequest request1 = (MultipartHttpServletRequest) request;
//        List<MultipartFile> multipartFiles = request1.getFiles("file");
//        if (null != multipartFiles && multipartFiles.size() == 1) {
//            MultipartFile multipartFile = multipartFiles.get(0);
//            String filename = multipartFile.getOriginalFilename();
//            try {
//                Map<String, Cell> tableHeard = getTableHeard(multipartFile.getInputStream(), filename);
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public Map<String, Cell> getTableHeard(InputStream is, String fileName) throws IOException{
//        Map<String,Cell> map = new LinkedHashMap<>();
//        Workbook workbook = null;
//        String extensionName = FilenameUtils.getExtension(fileName);
//        if (extensionName.toUpperCase().equals("xls")) {
//            workbook = new HSSFWorkbook();
//        } else if (extensionName.toUpperCase().equals("xlsx")){
//            workbook = new XSSFWorkbook();
//        }
//
//        Sheet sheetAt = workbook.getSheetAt(0);
//        Row heardRow = sheetAt.getRow(0);
//        Row typeRow = sheetAt.getRow(1);
//        short lastCellNum = heardRow.getLastCellNum();
//        for (int i = 0; i < lastCellNum; i++) {
//            if (null != heardRow.getCell(i) && heardRow.getCell(i).getCellType() != CellType.BLANK) {
//                map.put(heardRow.getCell(i).getStringCellValue(), typeRow.getCell(i));
//            }
//        }
//        return map;
//    }
//
//    private static void fetchCloumn(InputStream is, String fileName) throws IOException {
//        Workbook workbook = null;
//        String extensionName = FilenameUtils.getExtension(fileName);
//        if (extensionName.toUpperCase().equals("xls")) {
//            workbook = new HSSFWorkbook();
//        } else if (extensionName.toUpperCase().equals("xlsx")){
//            workbook = new XSSFWorkbook();
//        }
//
//        Sheet sheet = workbook.getSheetAt(0);
//
//        Row row = sheet.getRow(0);
//        short minColIx = row.getFirstCellNum();
//        short maxColIx = row.getLastCellNum();
//        if (minColIx == maxColIx || minColIx < 0) {
//            return;
//        }
//        for (int colIx = minColIx; colIx <= maxColIx; colIx++) {
//            Cell cell = row.getCell(new Integer(colIx));
//
//        }
//    }
//}
