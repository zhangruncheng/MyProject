package com.my.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.DoubleSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.my.bean.Student;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-22  16:48
 * @Version : 1.0.0
 **/
public class JsonFiledTest {


    private static final Logger logger = LoggerFactory.getLogger(JsonFiledTest.class);

    public static void main(String[] args) {
        Effective effective = new Effective(334523452.999,1000.076);
        Student student = new Student();
        Map<String,String> map = new HashMap<>();
        List<Field> supClassFiled = getSupClassFiled(Student.class);
        supClassFiled.forEach(s-> map.put(s.getName(),s.getType().getTypeName()));


        effective.setName("zhangsan");
        effective.setH("h");
        SerializeConfig config = new SerializeConfig();
        config.put(Double.class, new DoubleSerializer(new DecimalFormat("###,##0")));
        logger.info("main --> [{}]", JSON.toJSONString(effective,config));
        String s = JSON.toJSONString(effective, new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                try {
                    String typeName = object.getClass().getDeclaredField(name).getType().getTypeName();
                    if (typeName.equalsIgnoreCase("java.lang.String") && null == value) {
                        return "-";
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return value;
            }
        });
        logger.info("main --> [{}] ",s);


//        Effective effective1 = new Effective(334523452.0,1000.0);
//        SerializeConfig config1 = new SerializeConfig();
//        config.put(Double.class, new NumberSerializer(new NumberFormat("###,###.00")));
//        logger.info("main --> [{}]", JSON.toJSONString(effective1,config1));


    }


    public static List<Field> getSupClassFiled(Class clazz){
//        Class<?> clazz = obj.getClass();
//        Field[] fields = obj.getClass().getFields();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();

        }

        return fieldList;
    }


    @Data
    static class Effective{

        private Double effReal;

        private Double effShare;

        private String name;

        private String sex;

        private String h;

        private String d;

        public Effective() {
        }

        public Effective(Double effReal, Double effShare) {
            this.effReal = effReal;
            this.effShare = effShare;
        }
    }
}
