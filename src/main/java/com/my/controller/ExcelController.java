package com.my.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.my.bean.excel.AddressEntity;
import com.my.bean.excel.CourseEntity;
import com.my.bean.excel.StudentEntity;
import com.my.bean.excel.TeacherEntity;
import com.my.service.excel.DateHandler;
import com.my.service.excel.ExcelExportStylerByTypeImpl;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);


    @PostMapping(value = "/export")
    public void export(HttpServletResponse response){
        List<StudentEntity> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            StudentEntity studentEntity = new StudentEntity(i+"", "张三" + i,  i%2,  new Date(), new Date(),((i+1)*(i+1)*(i+1))*1000);
            list.add(studentEntity);
        }
        ExportParams exportParams = new ExportParams();
        exportParams.setColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());
        exportParams.setStyle(ExcelExportStylerByTypeImpl.class);
//        /** 设置数值处理器 */
//        DateHandler dateHandler = new DateHandler();
//        dateHandler.setNeedHandlerFields(new String[]{"钱财"});
//        exportParams.setDataHandler(dateHandler);
        /** 设置行高*/
        exportParams.setHeight((short) 8);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, StudentEntity.class, list);
//
//        /** 重新设置单元格样式 */
//        CellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setDataFormat((short)37);
//        /** 使用重写的 excel 导出样式接口  IExcelExportStyler*/
//        ExcelExportStylerColorNumImpl excelExportStylerColorNum = new ExcelExportStylerColorNumImpl(workbook);
//        /** 各行换色 */
//        CellStyle cellStyle1 = excelExportStylerColorNum.stringSeptailStyle(workbook, true);
//        CellStyle cellStyle2 = excelExportStylerColorNum.stringSeptailStyle(workbook, false);
//
//        Sheet sheetAt = workbook.getSheetAt(0);
//        int physicalNumberOfRows = sheetAt.getPhysicalNumberOfRows();
//        for (int i = 2; i < physicalNumberOfRows; i++) {
//            if (i % 2 == 0) {
//                sheetAt.getRow(i).getCell(4).setCellStyle(cellStyle1);
//            } else {
//                sheetAt.getRow(i).getCell(4).setCellStyle(cellStyle2);
//            }
//        }

        download(response,workbook);
    }

    @PostMapping(value = "/exportList")
    public void exportList(HttpServletResponse response){

        List<StudentEntity> studentEntityList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            StudentEntity studentEntity = new StudentEntity(i+"", "张三" + i,  i%2,  new Date(), new Date(),((i+1)*(i+1)*(i+1))*1000);
            studentEntity.setEntity1(new AddressEntity("1","011"));
            studentEntity.setEntity2(new AddressEntity("2","012"));
            studentEntity.setEntity3(new AddressEntity("3","013"));
            studentEntity.setEntity4(new AddressEntity("4","014"));
            studentEntity.setEntity5(new AddressEntity("5","015"));
            studentEntityList.add(studentEntity);
        }

        List<CourseEntity> courseEntityList = new ArrayList<>();
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setName("语文");
        courseEntity.setMathTeacher(new TeacherEntity("王国维"));
        courseEntity.setStudents(studentEntityList);
        courseEntityList.add(courseEntity);

        CourseEntity courseEntity1 = new CourseEntity();
        courseEntity1.setName("历史");
        courseEntity1.setMathTeacher(new TeacherEntity("梁启超"));
        courseEntity1.setStudents(studentEntityList);
        courseEntityList.add(courseEntity1);

        courseEntity.setTeacher(new TeacherEntity("第一次"));

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("三7班","课程，教师表"), CourseEntity.class, courseEntityList);

        download(response,workbook);

    }

    @PostMapping("/exportOther")
    public void exportOther(HttpServletResponse response){

        List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
        //构造对象等同于@Excel
        ExcelExportEntity name = new ExcelExportEntity("姓名", "name");
        name.setNeedMerge(true);
        entity.add(name);
        ExcelExportEntity sex = new ExcelExportEntity("性别", "sex");
        sex.setReplace(new String[]{"男_0","女_1"});
        sex.setSuffix("生");
        entity.add(sex);
        ExcelExportEntity age = new ExcelExportEntity("年龄", "age");
        ExcelExportEntity age1 = new ExcelExportEntity("小学年龄", "age1");
        age1.setType(10);
        ExcelExportEntity age2 = new ExcelExportEntity("初中年龄", "age2");
        age2.setType(10);
        List<ExcelExportEntity> excelExportEntities = Arrays.asList(age1, age2);
        age.setList(excelExportEntities);
        entity.add(age);

        //构造List等同于@ExcelCollection
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 30; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name","张三" + i);
            map.put("sex",i%2);
            List<Map<String, Object>> ageList = new ArrayList<Map<String, Object>>();
            Map<String, Object> mapS = new HashMap<>();
            mapS.put("age1",12);
            mapS.put("age2",15);
            ageList.add(mapS);
            ageList.add(mapS);
            ageList.add(mapS);
            map.put("age",ageList);

            list.add(map);
        }

        //把我们构造好的bean对象放到params就可以了
        ExportParams exportParams = new ExportParams("测试", "测试");
        exportParams.setStyle(ExcelStyleType.COLOR.getClazz());
        exportParams.setDataHandler(new DateHandler());
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, entity,list);
        download(response,workbook);

    }


    @PostMapping(value = "/exportBigData")
    public void exportBigData(HttpServletResponse response){
        List<StudentEntity> list = new ArrayList<>();
        StudentEntity studentEntity = null;
        long stattime = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++) {
            studentEntity = new StudentEntity(i+"", "张三" + i,  i%2,  new Date(), new Date());
            list.add(studentEntity);
        }
        ExportParams exportParams = new ExportParams("大数据测试", "测试");
        /** 设置行高*/
        exportParams.setHeight((short) 8);
        exportParams.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, StudentEntity.class, list);
        logger.info("userTime = [{}]",System.currentTimeMillis() - stattime);
        download(response,workbook);
    }


    private void download(HttpServletResponse response,Workbook workbook){
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + "测试数据" + ".xls");
        //编码
        response.setCharacterEncoding("UTF-8");
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
