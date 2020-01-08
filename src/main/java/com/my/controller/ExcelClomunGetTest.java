package com.my.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/excel/poi")
public class ExcelClomunGetTest{
 /**

     * 在读取模板文件的的时候处理

     */



    // 索引跟表头对应信息列表

    private static HashMap<Integer, String> index_cloumn_map = new HashMap<>();



    // 表头信息跟字段属性配置

    private static HashMap<String, Cell> cloumn_cellType = new HashMap<>();



    // 元数据信息字段配置

    private static HashMap<Integer, String> index_meta_cloumn_map = new HashMap<>();

    private static String str18 = "18年考核";

    private static String [] JX = new String[]{"前10%","前20%","前40%","前70%","后30%","后10%","无"};


    private static String [] LDZM = new String[]{"A", "B", "C","D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
    "O","P","Q","R","S","T","U","V","W","X","Y","Z"};




    @PostMapping("/testFile")
    public String uploadFile(HttpServletRequest request, Integer size){

        long start = System.currentTimeMillis();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");

        if (null != multipartFiles && multipartFiles.size() >= 1) {

            for (MultipartFile multipartFile : multipartFiles) {

                long onceTime = System.currentTimeMillis();

                String fileName = multipartFile.getOriginalFilename();

//                String dest_file = "d://tmp//excel//" + "use-("+size+ ") size-" + fileName;
                String dest_file = "/Users/zhangruncheng/Desktop/PA001/excel/tmp/" + "use-("+size+ ") size-" + fileName;

                File dest = new File(dest_file);

                try {

                    Map<String, Cell> tableHeard = getTableHeard(multipartFile.getInputStream(), fileName);

                    List<String> form = getForm(tableHeard);



                    fetchCloumn(multipartFile.getInputStream(),fileName);



                    FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),dest);

                    // 构建测试数据

                    List<Map<String, Object>> data = new ArrayList<>();

                    Random random = new Random();

                    for (int i = 0; i < size; i++) {

                        Map<String, Object> data1 = getData(tableHeard, i);

                        data.add(data1);

                    }



//                    insertDataToExcel(multipartFile.getInputStream(),fileName, data, dest);

                    insertDataToExcel(multipartFile.getInputStream(),fileName, data, dest,form);

                    log.info("[{}] size = [{}] and usetime = [{}]",fileName,size, (System.currentTimeMillis() - onceTime));

                } catch (IOException e) {

                    e.printStackTrace();

                }catch (ParseException e) {

                    e.printStackTrace();

                }

            }

        }

        long userTime = System.currentTimeMillis() - start;

        log.info("all useTime = [{}]", userTime);

        return "success and useTime = " + userTime;

    }


    @PostMapping("/getJson")
    public Map getJson(HttpServletRequest request,String name){

        List list = new ArrayList();
        List formulaList = new ArrayList();
        Map map = new HashMap();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String,Object> formulaMaptype = new LinkedHashMap<>();

        List<MultipartFile> multipartFiles = multipartRequest.getFiles("file");
        String filename = multipartFiles.get(0).getOriginalFilename();

        String path = "/Users/zhangruncheng/Desktop/PA001/excel/tmp/" + name;
        try {
            InputStream inputStream = multipartFiles.get(0).getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheetAt = workbook.getSheetAt(0);
            Row heardRow = sheetAt.getRow(0);
            map.put("fileName", filename);
            short maxColIndex = heardRow.getLastCellNum();
            int lastRowNum = sheetAt.getLastRowNum();

            for (int i = 0; i < maxColIndex; i++) {
                Row row = sheetAt.getRow(1);
                Cell cell = row.getCell(i);
                String excleTable = getExcleTable(i);
                if (null != cell && cell.getCellType() == CellType.FORMULA){
                    formulaMaptype.put(excleTable,cell.getCellFormula());
                }

            }

            for (int i = 0; i <= lastRowNum; i++) {
                List colList = new ArrayList();
                Row row = sheetAt.getRow(i);
                Map<String,Object> formulaMap = new LinkedHashMap<>();

                for (int j = 0; j < maxColIndex; j++) {
                    Map<String,Object> rowMap = new LinkedHashMap<>();
                    if (row == null) {
                        continue;
                    }
                    Cell cell = row.getCell(j);
                    String excleTable = getExcleTable(j);
                    if (null != cell){
                        switch (cell.getCellType()){
                            case STRING:
                                String stringCellValue = cell.getStringCellValue();
                                rowMap.put(excleTable, stringCellValue);
                                break;
                            case FORMULA:
//                                formulaMap.put(excleTable,cell.getCellFormula());
                                String cellFormula = cell.getCellFormula();
                                rowMap.put(excleTable, "=" + cellFormula);
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date dateCellValue = cell.getDateCellValue();
                                    rowMap.put(excleTable, dateCellValue);
                                } else {
                                    double numericCellValue = cell.getNumericCellValue();
                                    rowMap.put(excleTable, numericCellValue);
                                }
                                break;


                        }
                    }
                    colList.add(rowMap);
                }
                list.add(colList);
//                if (formulaMap.size() > 0) {
//                    formulaList.add(formulaMap);
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("data", list);
        map.put("formula", formulaMaptype);

        String jsonString = JSON.toJSONString(map);
        String size = StringUtils.substringBetween(filename,"use-(",")");

        String fileJson = path + "/" + size + ".json";

        try {
            File file1 = new File(path);
            if (!file1.exists()){
                file1.exists();
            }
            File file = new File(fileJson);

            FileUtils.writeStringToFile(file,jsonString,"utf-8");


        } catch (IOException e) {
            e.printStackTrace();
        }


        return map;

    }

    private static String getExcleTable(int i){
        int i1 = i % 26;
        int i2 = i / 26;
        if(i2 > 0) {
            return LDZM[i2-1] + LDZM[i1];
        } else {
            return LDZM[i1];
        }

    }

    /**

     * @describe: 获取会表达式的列

     * @param tableHeard

     * @author : ZHANGRUNCHENG389

     * @date : 2019-9-20  10:55

     * @return java.util.Map<java.lang.String,org.apache.poi.ss.usermodel.CellType>

    **/

    public List<String> getForm(Map<String, Cell> tableHeard){

        List<String> cellHeard = new ArrayList<>();

        for (Map.Entry<String, Cell> stringCellEntry : tableHeard.entrySet()) {

            String key = stringCellEntry.getKey();

            Cell cell = stringCellEntry.getValue();

            if (null != cell && CellType.FORMULA.equals(cell.getCellType())){

                cellHeard.add(key);

            }

        }

        return cellHeard;

    }



    public Map<String,Cell> getTableHeard(InputStream is,String fileName) throws IOException{

        Map<String,Cell> map = new LinkedHashMap<>();

        Workbook workbook = null;

        String extensionName = FilenameUtils.getExtension(fileName);

        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();



        Sheet sheet = workbook.getSheetAt(0);



        Row heradRow = sheet.getRow(0);

        Row typeRow = sheet.getRow(1);

        short lastCellNum = heradRow.getLastCellNum();

        for (int i = 0; i < lastCellNum; i++) {

            if (null != heradRow.getCell(i) && heradRow.getCell(i).getCellType() != CellType.BLANK) {

                map.put(heradRow.getCell(i).getStringCellValue(), typeRow.getCell(i));

            }

        }



        return map;



    }



    public Map<String,Object> getData(Map<String, Cell> map, int i){

        Map<String,Object> paraMap = new LinkedHashMap<>();

        for (Map.Entry<String, Cell> stringCellTypeEntry : map.entrySet()) {

            String key = stringCellTypeEntry.getKey();

            Cell cell = stringCellTypeEntry.getValue();
            if (null == cell) {
                continue;
            }
            CellType cellType = cell.getCellType();

            switch (cellType) {

                case BOOLEAN:

                    break;

                case NUMERIC:

                    // 这里的日期类型会被转换为数字类型，需要判别后区分处理

                    if (DateUtil.isCellDateFormatted(cell)) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        String format = sdf.format(new Date());

                        try {

                            paraMap.put(key, sdf.parse(format));

                        } catch (ParseException e) {

                            e.printStackTrace();

                        }

                    } else {

                        paraMap.put(key, new Random().nextInt(100) + i);

                    }

                    break;

                case STRING:
                    if (key.equals("18年考核")){
                        paraMap.put(key, getRandomStr());
                    } else {
                        paraMap.put(key, RandomStringUtils.randomAlphabetic(6) + i);
                    }

                    break;

                case FORMULA:

                    paraMap.put(key, 0);

                    // 公式的暂时不予以处理

                    break;

                case BLANK:



                    break;

                case ERROR:

                    break;

                default:

                    break;

            }

        }



        return paraMap;

    }





    public static String getRandomStr(){

        Random random = new Random();

        int i = random.nextInt(6);

        return JX[i];

    }



    public static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {

        FileUtils.copyFile(source, dest);

    }



    /**

     * 往excel 特定行插入数据:

     *

     * @param file

     * @param dataList

     * @param dest

     * @throws IOException

     */

    private static void insertDataToExcel(File file, List<Map<String, Object>> dataList, File dest) throws IOException, ParseException {

        String extensionName = FilenameUtils.getExtension(file.getName());

        InputStream is = new FileInputStream(file);

        Workbook workbook = null;



        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();



        // Todo 规范值：一般要填充的数据在第1个sheet

        Sheet sheet = workbook.getSheetAt(0);



        // Todo 规范值：一般要填充的数据在第2行

        Row row = sheet.getRow(1);

        StringBuilder sb = new StringBuilder();



        short minColIx = row.getFirstCellNum();

        short maxColIx = row.getLastCellNum();

        if (minColIx == maxColIx || minColIx < 0) {

            return;

        }



        Map<Integer, Row> rows = new HashMap<Integer, Row>();



        // 循环计算数据并插入到别的文件

        for (int i = 0; i < dataList.size(); i++) {

            Map<String, Object> data_map = dataList.get(i);

            Row newRow = sheet.createRow(i + 10);





            /**

             *  插入元数据信息

             */

            Integer colIx = 0;

            for (colIx = 0; colIx < maxColIx; colIx++) {

                Cell newRowCell = newRow.createCell(colIx);

                Cell cell = row.getCell(new Integer(colIx));



                // 根据列名读取应该插入那个数据

                String cloumn_name = index_cloumn_map.get(colIx);

                Object value = data_map.get(cloumn_name);

                if (value == null) {

                    continue;

                } else {

                    // Todo 风险 统一处理，全部当成富文本了--其他字段不一定是富文本

                    String num = JSON.toJSONString(value);

                    if (cell == null) {

                        cell = row.createCell(colIx);

                    }

                    num.trim();

                    String cloumn_na = index_cloumn_map.get(colIx);

                    Cell source_cell = cloumn_cellType.get(cloumn_na);

                    CellType type = source_cell.getCellType();

                    switch (type) {

                        case BOOLEAN:

                            cell.setCellValue(Boolean.valueOf(num));

                            newRowCell.setCellValue(Boolean.valueOf(num));

                            break;

                        case NUMERIC:

                            // 这里的日期类型会被转换为数字类型，需要判别后区分处理

                            if (DateUtil.isCellDateFormatted(source_cell)) {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                Date d = new Date();

                                if (value instanceof Date) {

                                    d = (Date) value;

                                }

                                String s = sdf.format(d);

                                Date date2 = sdf.parse(s);

                                cell.setCellValue(date2);

//                                Workbook tmp_book = cloumn_cellType.get(cloumn_na).getRow().get

//                                cell.setCellStyle(cloumn_cellType.get(cloumn_na).getCellStyle());

                                /**

                                 *

                                 */

//                                cell.setCellStyle();

                            } else {

                                cell.setCellValue(Double.valueOf(num));

                                newRowCell.setCellValue(Double.valueOf(num));

                            }

                            break;

                        case STRING:

                            cell.setCellValue(value.toString());

                            newRowCell.setCellValue(value.toString());

                            break;

                        case FORMULA:

                            // 公式的暂时不予以处理

                            break;

                        case BLANK:

                            cell.setBlank();

                            break;

                        case ERROR:

                            break;

                        default:

                            break;

                    }

                }

            }





            /**

             * 计算带公式

             */

            for (colIx = 0; colIx < maxColIx; colIx++) {

                Cell cell2 = row.getCell(new Integer(colIx));

                // 根据列名读取应该插入那个数据

                Cell newRowCell = newRow.createCell(colIx);

                String cloumn_name2 = index_cloumn_map.get(colIx);

                Object value2 = data_map.get(cloumn_name2);

                // Todo 如果是计算类型的， 是否直接代入计算即可算出数据。（乱序 C 依赖D， D依赖 E+ lookup）

                // 如果该列是公式列就计算

                CellType type = cell2.getCellType();

                if (type == CellType.FORMULA) {

                    long start = System.currentTimeMillis();

                    CellValue cellValue = evaluator.evaluate(cell2);

                    if (value2 == null) {

                        // Todo 风险 要判断这个字段的属性：不一定是数字类型的

                        cell2.setCellValue(cellValue.getNumberValue());

                        newRowCell.setCellValue(cellValue.getNumberValue());

                        continue;

                    }

                    log.info("row :" + i + "; cloumn:" + colIx + "; FORMULA time out:" + (System.currentTimeMillis() - start));

                }

            }

            // 更新完，然后才能读取文件的数值

            rows.put(i + 10, row);

        }



        copyMultiRowToDestFile(workbook, rows, dest);

        // 关闭流

        workbook.close();

    }



    private static void insertDataToExcel(InputStream is,String fileName, List<Map<String, Object>> dataList, File dest) throws IOException, ParseException {



        Workbook workbook = null;

        String extensionName = FilenameUtils.getExtension(fileName);

        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();



        // Todo 规范值：一般要填充的数据在第1个sheet

        Sheet sheet = workbook.getSheetAt(0);



        // Todo 规范值：一般要填充的数据在第2行

        Row row = sheet.getRow(1);

        StringBuilder sb = new StringBuilder();



        short minColIx = row.getFirstCellNum();

        short maxColIx = row.getLastCellNum();

        if (minColIx == maxColIx || minColIx < 0) {

            return;

        }



        Map<Integer, Row> rows = new HashMap<Integer, Row>();



        // 循环计算数据并插入到别的文件

        for (int i = 0; i < dataList.size(); i++) {

            Map<String, Object> data_map = dataList.get(i);

            Row newRow = sheet.createRow(i + 10);

            /**

             *  插入元数据信息

             */

            Integer colIx = 0;

            for (colIx = 0; colIx < maxColIx; colIx++) {

                Cell cell = row.getCell(new Integer(colIx));

                Cell newRowCell = newRow.createCell(colIx);

                // 根据列名读取应该插入那个数据

                String cloumn_name = index_cloumn_map.get(colIx);

                Object value = null;

//                if (StringUtils.isNotBlank(cloumn_name)){

//                    value = data_map.get(cloumn_name.trim());

//                } else {

//                    value = data_map.get(cloumn_name);

//                }

                value = data_map.get(cloumn_name);

                if (value == null) {

                    continue;

                } else {

                    // Todo 风险 统一处理，全部当成富文本了--其他字段不一定是富文本

                    String num = JSON.toJSONString(value);

                    if (cell == null) {

                        cell = row.createCell(colIx);

                    }

                    num.trim();

                    String cloumn_na = index_cloumn_map.get(colIx);

                    Cell source_cell = cloumn_cellType.get(cloumn_na);

                    CellType type = source_cell.getCellType();

                    switch (type) {

                        case BOOLEAN:

                            cell.setCellValue(Boolean.valueOf(num));

                            newRowCell.setCellValue(Boolean.valueOf(num));

                            break;

                        case NUMERIC:

                            // 这里的日期类型会被转换为数字类型，需要判别后区分处理

                            if (DateUtil.isCellDateFormatted(source_cell)) {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                Date d = new Date();

                                if (value instanceof Date) {

                                    d = (Date) value;

                                }

                                String s = sdf.format(d);

                                Date date2 = sdf.parse(s);

                                cell.setCellValue(date2);

                                newRowCell.setCellValue(date2);

//                                Workbook tmp_book = cloumn_cellType.get(cloumn_na).getRow().get

//                                cell.setCellStyle(cloumn_cellType.get(cloumn_na).getCellStyle());

                                /**

                                 *

                                 */

//                                cell.setCellStyle();

                            } else {

                                cell.setCellValue(Double.valueOf(num));

                                newRowCell.setCellValue(Double.valueOf(num));

                            }

                            break;

                        case STRING:

                            cell.setCellValue(value.toString());

                            newRowCell.setCellValue(value.toString());



                            break;

                        case FORMULA:

                            // 公式的暂时不予以处理

                            break;

                        case BLANK:

                            cell.setBlank();

                            break;

                        case ERROR:

                            break;

                        default:

                            break;

                    }

                }

            }





            /**

             * 计算带公式

             */

            for (colIx = 0; colIx < maxColIx; colIx++) {

                Cell newRowCell = newRow.getCell(colIx);

                Cell cell2 = row.getCell(new Integer(colIx));

                // 根据列名读取应该插入那个数据

                String cloumn_name2 = index_cloumn_map.get(colIx);

                Object value2 = data_map.get(cloumn_name2);

                // Todo 如果是计算类型的， 是否直接代入计算即可算出数据。（乱序 C 依赖D， D依赖 E+ lookup）

                // 如果该列是公式列就计算

                if(cell2!=null) {

                    CellType type = cell2.getCellType();

                    if (type == CellType.FORMULA) {

                        long start = System.currentTimeMillis();

                        newRowCell.setCellFormula(cell2.getCellFormula());

                    }

                }

            }

            // 更新完，然后才能读取文件的数值

            rows.put(i + 10, newRow);

        }

        sheet.setForceFormulaRecalculation(true);

        copyMultiRowToDestFile(workbook, rows, dest);

        // 关闭流

        workbook.close();

    }





    private static void insertDataToExcel(InputStream is,String fileName, List<Map<String, Object>> dataList, File dest,

                                          List<String> form) throws IOException, ParseException {



        Workbook workbook = null;

        String extensionName = FilenameUtils.getExtension(fileName);

        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();



        // Todo 规范值：一般要填充的数据在第1个sheet

        Sheet sheet = workbook.getSheetAt(0);

        int maxNotBlankRow = getMaxNotBlankRow(sheet);

        // Todo 规范值：一般要填充的数据在第2行

        Row row = sheet.getRow(1);

        StringBuilder sb = new StringBuilder();



        short minColIx = row.getFirstCellNum();

        short maxColIx = row.getLastCellNum();

        if (minColIx == maxColIx || minColIx < 0) {

            return;

        }



        Map<Integer, Row> rows = new HashMap<Integer, Row>();



        // 循环计算数据并插入到别的文件

        for (int i = 0; i < dataList.size(); i++) {

            Map<String, Object> data_map = dataList.get(i);

            Row newRow = sheet.createRow(i + 1 + maxNotBlankRow);

            /**

             *  插入元数据信息

             */

            Integer colIx = 0;

            for (colIx = 0; colIx < maxColIx; colIx++) {

                Cell cell = row.getCell(new Integer(colIx));

                Cell newRowCell = newRow.createCell(colIx);

                // 根据列名读取应该插入那个数据

                String cloumn_name = index_cloumn_map.get(colIx);

                Object value = null;

//                if (StringUtils.isNotBlank(cloumn_name)){

//                    value = data_map.get(cloumn_name.trim());

//                } else {

//                    value = data_map.get(cloumn_name);

//                }

                value = data_map.get(cloumn_name);

                if (value == null) {

                    continue;

                } else {

                    // Todo 风险 统一处理，全部当成富文本了--其他字段不一定是富文本

                    String num = JSON.toJSONString(value);

                    if (cell == null) {

                        cell = row.createCell(colIx);

                    }

                    num.trim();

                    String cloumn_na = index_cloumn_map.get(colIx);

                    Cell source_cell = cloumn_cellType.get(cloumn_na);

                    CellType type = source_cell.getCellType();

                    switch (type) {

                        case BOOLEAN:

                            cell.setCellValue(Boolean.valueOf(num));

                            newRowCell.setCellValue(Boolean.valueOf(num));

                            break;

                        case NUMERIC:

                            // 这里的日期类型会被转换为数字类型，需要判别后区分处理

                            if (DateUtil.isCellDateFormatted(source_cell)) {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                Date d = new Date();

                                if (value instanceof Date) {

                                    d = (Date) value;

                                }

                                String s = sdf.format(d);

                                Date date2 = sdf.parse(s);

                                cell.setCellValue(date2);

                                newRowCell.setCellValue(date2);

//                                Workbook tmp_book = cloumn_cellType.get(cloumn_na).getRow().get

//                                cell.setCellStyle(cloumn_cellType.get(cloumn_na).getCellStyle());

                                /**

                                 *

                                 */

//                                cell.setCellStyle();

                            } else {

                                cell.setCellValue(Double.valueOf(num));

                                newRowCell.setCellValue(Double.valueOf(num));

                            }

                            break;

                        case STRING:

                            cell.setCellValue(value.toString());

                            newRowCell.setCellValue(value.toString());



                            break;

                        case FORMULA:

                            // 公式的暂时不予以处理

                            break;

                        case BLANK:

                            cell.setBlank();

                            break;

                        case ERROR:

                            break;

                        default:

                            break;

                    }

                }

            }





            /**

             * 计算带公式

             */

            for (colIx = 0; colIx < maxColIx; colIx++) {

                Cell newRowCell = newRow.getCell(colIx);

                Cell cell2 = row.getCell(new Integer(colIx));

                // 根据列名读取应该插入那个数据

                String cloumn_name2 = index_cloumn_map.get(colIx);

                Object value2 = data_map.get(cloumn_name2);



                // Todo 如果是计算类型的， 是否直接代入计算即可算出数据。（乱序 C 依赖D， D依赖 E+ lookup）

                // 如果该列是公式列就计算

                if(cell2!=null) {

                    CellType type = cell2.getCellType();

                    if (type == CellType.FORMULA) {

                        long start = System.currentTimeMillis();

                        newRowCell.setCellFormula(cell2.getCellFormula());



                        /** 添加列 */

//                        if (null != form && form.size() > 0) {
//
//                            /** 公式列存在 */
//
//                            int indexOf = form.indexOf(cloumn_name2);
//
//                            if (indexOf > -1){
//
//                                Cell formulaCell = newRow.createCell(maxColIx  + indexOf *2);
//
//                                Cell valueCell = newRow.createCell(maxColIx + indexOf*2 + 1);
//
//                                formulaCell.setCellFormula(cell2.getCellFormula());
//
//                            }
//
//
//
//                        }

                    }

                }

            }





            // 更新完，然后才能读取文件的数值

            rows.put(i + 1 + maxNotBlankRow, newRow);

        }

        sheet.setForceFormulaRecalculation(true);

        copyMultiRowToDestFile(workbook, rows, dest,form);

        // 关闭流

        workbook.close();

    }


    public static int getMaxNotBlankRow(Sheet sheet){

        int lastRowNum = sheet.getLastRowNum();
        for (int i = lastRowNum; i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (null != cell && CellType.BLANK != cell.getCellType()) {
                        return i;
                    }
                }
            }
        }
        return lastRowNum;
    }

    /**

     * 拷贝行数据到目标文件的指定sheet

     *

     * @param source_book

     * @param sourceRow

     * @param dest_line

     * @param dest

     * @throws IOException

     */

    private static void copyRowToDestFile(Workbook source_book, Row sourceRow, int dest_line, File dest) throws IOException {

        long start = System.currentTimeMillis();

        String extensionName = FilenameUtils.getExtension(dest.getName());

        InputStream is = new FileInputStream(dest);

        Workbook workbook = null;



        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        // Todo 默认第一个表单

        Sheet sheet = workbook.getSheetAt(0);



        // Todo 规范值：一般要填充的数据在第2行

        Row row = sheet.createRow(dest_line);



        for (Integer colIx = 0; colIx < sourceRow.getLastCellNum(); colIx++) {

            // 插入相应的数据

            Cell cell = row.createCell(colIx);

            cell = copyCell(source_book, sourceRow.getCell(colIx), cell);

        }



        OutputStream out = null;

        out = new FileOutputStream(dest.getPath());

        workbook.write(out);

        workbook.close();

        System.out.println("Row Time out:" + (System.currentTimeMillis() - start));

    }



    private static void copyMultiRowToDestFile(Workbook source_book, Map<Integer, Row> sourceRows, File dest) throws IOException {

        long start = System.currentTimeMillis();

        String extensionName = FilenameUtils.getExtension(dest.getName());

        InputStream is = new FileInputStream(dest);

        Workbook workbook = null;



        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        // Todo 默认第一个表单

        Sheet sheet = workbook.getSheetAt(0);



        sheet.setForceFormulaRecalculation(true);

        for (Map.Entry<Integer, Row> en : sourceRows.entrySet()) {

            Integer dest_line = en.getKey();

            Row sourceRow = en.getValue();

            // Todo 规范值：一般要填充的数据在第2行

            Row row = sheet.createRow(dest_line);



            for (Integer colIx = 0; colIx < sourceRow.getLastCellNum(); colIx++) {

                // 插入相应的数据

                Cell cell = row.createCell(colIx);

                cell = copyCell(source_book, sourceRow.getCell(colIx), cell,dest_line);

            }

        }

        sheet.setForceFormulaRecalculation(true);





        OutputStream out = null;

        out = new FileOutputStream(dest.getPath());

        workbook.write(out);

        workbook.close();

        System.out.println("Row Time out:" + (System.currentTimeMillis() - start));

    }



    /**

     * @param source_book

     * @param sourceRows

     * @throws IOException

     */

    private static void copyMultiRowToDestFile(Workbook source_book, Map<Integer, Row> sourceRows, File dest,List<String> form) throws IOException {

        long start = System.currentTimeMillis();

        String extensionName = FilenameUtils.getExtension(dest.getName());

        InputStream is = new FileInputStream(dest);

        Workbook workbook = null;



        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        // Todo 默认第一个表单

        Sheet sheet = workbook.getSheetAt(0);



        /** 修改表头 */

        Row heardColunm = sheet.getRow(0);

        short maxCellColunm = heardColunm.getLastCellNum();

        Row valueColunm = sheet.getRow(1);

//        for (int i = heardColunm.getFirstCellNum(); i < maxCellColunm; i++) {
//            Cell cell = valueColunm.getCell(i);
//            if (null!= cell && cell.getCellType() == CellType.FORMULA) {
//
//                /** 公式列所在的位置 */
//
//                String cloumn_name_formula = heardColunm.getCell(i).getStringCellValue();
//
//                int indexOf = form.indexOf(cloumn_name_formula);
//
//                if (indexOf > -1) {
//
//                    Cell cellFormula = heardColunm.createCell(heardColunm.getLastCellNum());
//
//                    Cell cellValue = heardColunm.createCell(heardColunm.getLastCellNum());
//
//                    cellFormula.setCellValue(cloumn_name_formula + "(公式)");
//
//                    cellValue.setCellValue(cloumn_name_formula + "(计算值)");
//
//                }
//
//            }
//
//        }



        sheet.setForceFormulaRecalculation(true);

        for (Map.Entry<Integer, Row> en : sourceRows.entrySet()) {

            Integer dest_line = en.getKey();

            Row sourceRow = en.getValue();

            // Todo 规范值：一般要填充的数据在第2行

            Row row = sheet.createRow(dest_line);



            for (Integer colIx = 0; colIx < sourceRow.getLastCellNum(); colIx++) {

                // 插入相应的数据

                Cell cell = row.createCell(colIx);

                cell = copyCell(source_book, sourceRow.getCell(colIx), cell,dest_line);

            }

        }

        sheet.setForceFormulaRecalculation(true);

        Row row = sheet.getRow(0);

        int index = 0;

//        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//
//        short lastCellNum = row.getLastCellNum();
//
//        for (int i = 0; i < lastCellNum; i++) {
//
//        if(row.getCell(i).getStringCellValue().contains(("(公式)"))) {
//
//            index = i;
//
//            break;
//
//        }
//
//    }
//
//    List<String> formulaList = new ArrayList<>();
//
//    int lastRowNum = sheet.getLastRowNum();
//
//        long startTime = System.currentTimeMillis();

//        for (int i = 10; i <= lastRowNum; i++) {
//
//            Row sheetRow = sheet.getRow(i);
//
//            for (int j = index; j < lastCellNum; j += 2) {
//
//                if (row.getCell(j).getStringCellValue().contains("(公式)")){
//
//                    Cell cellFormula = sheetRow.getCell(j);
//
//                    CellValue evaluate = evaluator.evaluate(cellFormula);
//
//                    if (null != cellFormula && CellType.FORMULA == cellFormula.getCellType() && i == 10) {
//
//                        formulaList.add(cellFormula.getCellFormula());
//
//                    }
//
//                    try {
//
//                        if (null != evaluate) {
//
//                            sheetRow.getCell(j + 1).setCellValue(evaluate.getNumberValue());
//
//                        }
//
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//
//                    }
//
//                }
//
//            }
//
//        }

//        log.info("calculate FORMULA size = [{}] and useTime = [{}]",(lastRowNum - 9),(System.currentTimeMillis() - startTime));

//        log.info("this formula size = [{}] and is {}",formulaList.size(),formulaList);

        OutputStream out = null;

        out = new FileOutputStream(dest.getPath());

        workbook.write(out);

        workbook.close();

        System.out.println("Row Time out:" + (System.currentTimeMillis() - start));

    }







    /**

     * @param workbook 要截图workbook 进行运算

     * @param source

     * @param dest

     * @return

     */

    private static Cell copyCell(Workbook workbook, Cell source, Cell dest) {

        CellType type = source.getCellType();



        switch (type) {

            case BOOLEAN:

                dest.setCellValue(source.getBooleanCellValue());

                break;

            case NUMERIC:

                // 这里的日期类型会被转换为数字类型，需要判别后区分处理

                if (DateUtil.isCellDateFormatted(dest)) {

                    dest.setCellValue(source.getDateCellValue());

                } else {

                    dest.setCellValue(source.getNumericCellValue());

                }

                break;

            case STRING:

                dest.setCellValue(source.getRichStringCellValue());

                break;

            case FORMULA:

                FormulaEvaluator evaluator = workbook.getCreationHelper()

                        .createFormulaEvaluator();

                CellValue cellValue = evaluator.evaluate(source);

                // Todo 如何判断数据的值； 未处理

                dest.setCellValue(cellValue.getNumberValue());

                break;

            case BLANK:

                dest.setBlank();

                break;

            case ERROR:

                break;

            default:

                break;

        }

        return dest;

    }



    private static Cell copyCell(Workbook workbook, Cell source, Cell dest,int index) {

        CellType type = source.getCellType();



        switch (type) {

            case BOOLEAN:

                dest.setCellValue(source.getBooleanCellValue());

                break;

            case NUMERIC:

                // 这里的日期类型会被转换为数字类型，需要判别后区分处理

                if (DateUtil.isCellDateFormatted(dest)) {

                    dest.setCellValue(source.getDateCellValue());

                } else {

                    dest.setCellValue(source.getNumericCellValue());

                }

                break;

            case STRING:

                dest.setCellValue(source.getRichStringCellValue());

                break;

            case FORMULA:

                FormulaEvaluator evaluator = workbook.getCreationHelper()

                        .createFormulaEvaluator();

//                CellValue cellValue = evaluator.evaluate(source);

                // Todo 如何判断数据的值； 未处理

//                dest.setCellValue(cellValue.getNumberValue());

                String cellFormula = source.getCellFormula();

//                String s = cellFormula.replaceAll("^[A~Z]\2", Integer.toString(index + 1));

                String s = cellFormula.replaceAll("([A-Z]{1,})(2)", "$1" + Integer.toString(index + 1));

//                String s = rep(cellFormula,index + 1);

                dest.setCellFormula(s);

                break;

            case BLANK:

                dest.setBlank();

                break;

            case ERROR:

                break;

            default:

                break;

        }

        return dest;

    }





    private static String rep(String str,int x){

        if (StringUtils.isNotBlank(str)){

            String[] split = str.split("2");

            for (int i = 1,length = split.length; i < length; i++) {

                split[i] = "2" + split[i];

                if (i == length -1){

                    char lastChar = split[length - 1].charAt(split[length - 1].length() - 1);

                    if (lastChar >= 'A' && lastChar <= 'Z'){

                        split[length -1] = split[length -1] + "2";

                    }

                }

            }

            for (int i = 1; i < split.length; i++) {

                char lastChar = split[i - 1].charAt(split[i - 1].length() -1);

                if (lastChar >= 'A' && lastChar <= 'Z') {

                    split[i] =split[i].replaceAll("2",Integer.toString(x));

                }

            }

            StringBuilder sb = new StringBuilder();

            for (String s : split) {

                sb.append(s);

            }

            return sb.toString();

        }

        return null;

    }



    private String replaceStr(String str){

        List<Integer> indexList = new ArrayList<>();

        if (StringUtils.isNotBlank(str)){

            for (int i = 0,length = str.length(); i < length; i++) {

                String indexChar = str.substring(i, i + 1);

                if ("2".equals(indexChar)){

                    char charAt = str.charAt(i);

                    if (charAt >= 'a' && charAt <= 'z' || charAt >= 'A' && charAt <= 'Z') {

                        indexList.add(i);

                    }

                }

            }

            for (Integer integer : indexList) {



            }

        }

        return str;

    }



    /**

     * @param source

     */

    private static void fetchCloumn(File source) throws IOException {

        String extensionName = FilenameUtils.getExtension(source.getName());

        InputStream is = new FileInputStream(source);

        Workbook workbook = null;



        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();



        Sheet sheet = workbook.getSheetAt(0);





        /**

         * 读取表头信息

         */

        Row row = sheet.getRow(0);

        short minColIx = row.getFirstCellNum();

        short maxColIx = row.getLastCellNum();

        if (minColIx == maxColIx || minColIx < 0) {

            return;

        }

        for (int colIx = minColIx; colIx <= maxColIx; colIx++) {

            Cell cell = row.getCell(new Integer(colIx));

            if (cell != null) {

                // 读取字段名

                String cloumn_name = cell.getStringCellValue();

                index_cloumn_map.put(colIx, cloumn_name);

                index_meta_cloumn_map.put(colIx, cloumn_name);

            }

        }



        /**

         * 读取字段属性信息;第二行提取出来字段的属性（celltype）: 备注：日期格式的不一定能够读取出来， string boolean number famula 一定支持

         * 同时要更新下 元数据信息字段配置

         */

        Row field_row = sheet.getRow(1);

        short minColIx2 = field_row.getFirstCellNum();

        short maxColIx2 = field_row.getLastCellNum();

        if (minColIx2 == maxColIx2 || minColIx2 < 0) {

            return;

        }

        for (short colIx2 = minColIx2; colIx2 <= maxColIx2; colIx2++) {

            Cell cell = field_row.getCell(new Integer(colIx2));

            // 判断cell是否为空

            if (cell != null) {

                String cloumn_name = index_cloumn_map.get(new Integer(colIx2));

                cloumn_cellType.put(cloumn_name, cell);

                if (cell.getCellType() == CellType.FORMULA) {

                    index_meta_cloumn_map.remove(new Integer(colIx2));

                }

            }

        }

        System.out.println("cloumn info:" + JSON.toJSONString(index_cloumn_map));

        System.out.println("index_meta_cloumn_map info:" + JSON.toJSONString(index_meta_cloumn_map));

    }



    private static void fetchCloumn(InputStream is,String fileName) throws IOException {

        Workbook workbook = null;

        String extensionName = FilenameUtils.getExtension(fileName);

        if (extensionName.toLowerCase().equals(ExcelHelper.XLS)) {

            workbook = new HSSFWorkbook(is);

        } else if (extensionName.toLowerCase().equals(ExcelHelper.XLSX)) {

            workbook = new XSSFWorkbook(is);

        }

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();



        Sheet sheet = workbook.getSheetAt(0);





        /**

         * 读取表头信息

         */

        Row row = sheet.getRow(0);

        short minColIx = row.getFirstCellNum();

        short maxColIx = row.getLastCellNum();

        if (minColIx == maxColIx || minColIx < 0) {

            return;

        }

        for (int colIx = minColIx; colIx <= maxColIx; colIx++) {

            Cell cell = row.getCell(new Integer(colIx));

            if (cell != null) {

                // 读取字段名

                String cloumn_name = cell.getStringCellValue();

                index_cloumn_map.put(colIx, cloumn_name);

                index_meta_cloumn_map.put(colIx, cloumn_name);

            }

        }



        /**

         * 读取字段属性信息;第二行提取出来字段的属性（celltype）: 备注：日期格式的不一定能够读取出来， string boolean number famula 一定支持

         * 同时要更新下 元数据信息字段配置

         */

        Row field_row = sheet.getRow(1);

        short minColIx2 = field_row.getFirstCellNum();

        short maxColIx2 = field_row.getLastCellNum();

        if (minColIx2 == maxColIx2 || minColIx2 < 0) {

            return;

        }

        for (short colIx2 = minColIx2; colIx2 <= maxColIx2; colIx2++) {

            Cell cell = field_row.getCell(new Integer(colIx2));

            // 判断cell是否为空

            if (cell != null) {

                String cloumn_name = index_cloumn_map.get(new Integer(colIx2));

                cloumn_cellType.put(cloumn_name, cell);

                if (cell.getCellType() == CellType.FORMULA) {

                    index_meta_cloumn_map.remove(new Integer(colIx2));

                }

            }

        }

        System.out.println("cloumn info:" + JSON.toJSONString(index_cloumn_map));

        System.out.println("index_meta_cloumn_map info:" + JSON.toJSONString(index_meta_cloumn_map));

    }





    /**

     * used for testing

     *

     * @param args

     * @throws IOException

     */

    public static void main(String[] args) throws IOException, ParseException {

        int i =2, j =26,k = 53,l = 82;
        try {
            System.out.println(getExcleTable(i));
            System.out.println(getExcleTable(j));
            System.out.println(getExcleTable(k));
            System.out.println(getExcleTable(l));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}