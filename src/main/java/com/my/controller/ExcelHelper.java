package com.my.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelHelper {

    /**

     * Excel 2003

     */

    public final static String XLS = "xls";

    /**

     * Excel 2007

     */

    public final static String XLSX = "xlsx";

    /**

     * 分隔符

     */

    public final static String SEPARATOR = "|";



    /**

     * 由Excel文件的Sheet导出至List

     *

     * @param file

     * @param sheetNum

     * @return

     */

    public static List<String> exportListFromExcel(File file, int sheetNum)

            throws IOException {

        return exportListFromExcel(new FileInputStream(file),

                FilenameUtils.getExtension(file.getName()), sheetNum);

    }



    /**

     * 由Excel流的Sheet导出至List

     *

     * @param is

     * @param extensionName

     * @param sheetNum

     * @return

     * @throws IOException

     */

    public static List<String> exportListFromExcel(InputStream is,

                                                   String extensionName, int sheetNum) throws IOException {



        Workbook workbook = null;



        if (extensionName.toLowerCase().equals(XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(XLSX)) {

            workbook = new XSSFWorkbook(is);

        }



        return exportListFromExcel(workbook, sheetNum);

    }



    /**

     * 由指定的Sheet导出至List

     *

     * @param workbook

     * @param sheetNum

     * @return

     * @throws IOException

     */





    private static List<String> exportListFromExcel(Workbook workbook,

                                                    int sheetNum) {



        Sheet sheet = workbook.getSheetAt(sheetNum);



        // 解析公式结果

        FormulaEvaluator evaluator = workbook.getCreationHelper()

                .createFormulaEvaluator();

        List<String> list = new ArrayList<String>();



        int minRowIx = sheet.getFirstRowNum();

        int maxRowIx = sheet.getLastRowNum();

        for (int rowIx = minRowIx; rowIx <= maxRowIx; rowIx++) {

            Row row = sheet.getRow(rowIx);

            StringBuilder sb = new StringBuilder();



            short minColIx = row.getFirstCellNum();

            short maxColIx = row.getLastCellNum();

            if(minColIx==maxColIx ||minColIx<0){

                continue;

            }

            for (short colIx = minColIx; colIx <= maxColIx; colIx++) {

                Cell cell = row.getCell(new Integer(colIx));

                if(cell!=null &&cell.getCellType() == CellType.FORMULA){

                    String s = String.valueOf(cell.getCellFormula());

                    System.out.println("formula:"+s);

                }



                CellValue cellValue = evaluator.evaluate(cell);

                if (cellValue == null) {

                    continue;

                }

                // 经过公式解析，最后只存在Boolean、Numeric和String三种数据类型，此外就是Error了

                // 其余数据类型，根据官方文档，完全可以忽略http://poi.apache.org/spreadsheet/eval.html

                switch (cellValue.getCellTypeEnum()) {

                    case BOOLEAN:

                        sb.append(SEPARATOR + cellValue.getBooleanValue());

                        break;

                    case NUMERIC:

                        // 这里的日期类型会被转换为数字类型，需要判别后区分处理

                        if (DateUtil.isCellDateFormatted(cell)) {

                            sb.append(SEPARATOR + cell.getDateCellValue());

                        } else {

                            sb.append(SEPARATOR + cellValue.getNumberValue());

                        }

                        break;

                    case STRING:

                        sb.append(SEPARATOR + cellValue.getStringValue());

                        break;

                    case FORMULA:

                        break;

                    case BLANK:

                        break;

                    case ERROR:

                        break;

                    default:

                        break;

                }

            }

            list.add(sb.toString());

        }

        return list;

    }







    /**

     *

     * @param filename

     */

    public static void writeExcel(String filename,int sheetNum) throws Exception {

        Map<String,String> pillMap = new HashMap<>();

        InputStream inp = new FileInputStream(filename);

        POIFSFileSystem fs = new POIFSFileSystem(inp);

        Workbook wb = new HSSFWorkbook(fs);

        Sheet sheet = wb.getSheetAt(sheetNum);

        for(int i = sheet.getFirstRowNum();i<=sheet.getLastRowNum();i++) {

            Row row = sheet.getRow(i);

            // 取第一个单元格

            Cell cell = row.getCell(0);

            if(null == cell) {

                continue;

            }

            String cellV = cell.getStringCellValue();

            String newV = pillMap.get(cellV);

            //写入第二个单元格

            if(newV != null && !newV.isEmpty()) {

                Cell cell1 = row.getCell(1);

                if(null == cell1) {

                    row.createCell(1);

                }

                cell1.setCellType(CellType.STRING);

                cell1.setCellValue(newV);

            }



        }



        FileOutputStream fileOut = new FileOutputStream(filename);

        wb.write(fileOut);

        fileOut.close();

    }





    public static void main(String[] args) {

        String str = "=MIN((DATE(2019,12,31)-E2+1)/365,1)";

        String str1 = "=D2*K2";



        String s = str.replaceAll("([A-Z]{1,})(2)", "$1" + "15");

        String s1 = str1.replaceAll("([A-Z]{1,})(2)", "$1" + "13");

        System.out.println(s);

        System.out.println(s1);



        for (int i = 0; i < 20; i++) {

//            System.out.println(ExcelCloumnGetTest.getRandomStr());

        }

    }



}
