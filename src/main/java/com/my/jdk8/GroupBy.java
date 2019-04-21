package com.my.jdk8;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-21  10:19
 * @Version : 1.0.0
 **/
@Service
public class GroupBy {
    private static final Logger logger = LoggerFactory.getLogger(GroupBy.class);

    /**
     * 分组求和统计
     * @Author : zhangruncheng
     * @Date : 2019-04-21 10:52
     * @param
     * @return void
    **/
    public void testSum(){
        Map<String,Long> map = new HashMap<>();
        map.put("1",100L);
        map.put("2",200L);
        map.put("3",300L);
        map.put("4",400L);
        Long collect = map.values().stream().collect(Collectors.counting());
        logger.info("testGroupByJdk8 --> [{}]",collect);

        List<CompanyEntiry> companyEntiryList = new ArrayList<>();
        companyEntiryList.add(new CompanyEntiry("PA001", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA001", 2000L));
        companyEntiryList.add(new CompanyEntiry("PA002", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA002", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA001", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA003", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA003", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA004", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA005", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA004", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA005", 1000L));
        companyEntiryList.add(new CompanyEntiry("PA005", 1000L));
//        companyEntiryList.add(new CompanyEntiry("PA001", null));
//        companyEntiryList.add(new CompanyEntiry("PA001", null));
//        companyEntiryList.add(new CompanyEntiry("PA002", null));
//        companyEntiryList.add(new CompanyEntiry("PA002", null));
//        companyEntiryList.add(new CompanyEntiry("PA001", null));
//        companyEntiryList.add(new CompanyEntiry("PA003", null));
//        companyEntiryList.add(new CompanyEntiry("PA003", null));
//        companyEntiryList.add(new CompanyEntiry("PA004", null));
//        companyEntiryList.add(new CompanyEntiry("PA005", null));
//        companyEntiryList.add(new CompanyEntiry("PA004", null));
//        companyEntiryList.add(new CompanyEntiry("PA005", null));
//        companyEntiryList.add(new CompanyEntiry("PA005", null));

        /** 分组统计 */
        Map<String, Long> groupCount = companyEntiryList.stream().collect(Collectors.groupingBy(CompanyEntiry::getCompanyNo, Collectors.counting()));
        logger.info("testSum --> groupCount [{}]" ,groupCount);


        /** 分组求和 */
        Map<String, LongSummaryStatistics> collect1 = companyEntiryList.stream().collect(
                Collectors.groupingBy(CompanyEntiry::getCompanyNo, Collectors.summarizingLong(CompanyEntiry::getMoney)));
        logger.info("testSum -->collect1 =  [{}]",collect1);
        Map<String, Long> longMap = companyEntiryList.stream().collect(
                Collectors.groupingBy(CompanyEntiry::getCompanyNo, Collectors.summingLong(CompanyEntiry::getMoney)));
        logger.info("testSum -->longMap = [{}]",longMap);
        Long allSum = longMap.values().stream().collect(Collectors.summingLong(Long::longValue));
        logger.info("testSum -->longMap ->allSum = [{}]",allSum);
        logger.info("testSum --> ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        long startTime = System.currentTimeMillis();
        for (Map.Entry<String, LongSummaryStatistics> stringLongSummaryStatisticsEntry : collect1.entrySet()) {
            logger.info("testSum -->groupBy countSum companyNo = [{}] sum = [{}]",stringLongSummaryStatisticsEntry.getKey(),
                    stringLongSummaryStatisticsEntry.getValue().getSum());
        }

        LongSummaryStatistics longSummaryStatistics = collect1.values().stream().collect(Collectors.summarizingLong(LongSummaryStatistics::getSum));
        logger.info("testSum --> allSum = [{}]" ,longSummaryStatistics.getSum());
        logger.info("testSum--> userTime = {} ms",System.currentTimeMillis() - startTime);

    }

    @Data
    static class CompanyEntiry{
        private String companyNo;

        private Long money;

        public CompanyEntiry(String companyNo,Long money){
            this.companyNo = companyNo;
            this.money = money;
        }
    }


    public static void main(String[] args) {
        //查询昨天一天的所有交易
        List<OrdersDO> list = new ArrayList<>();
        list.add(new OrdersDO(1L,100L,1));
        list.add(new OrdersDO(5L,300L,2));
        list.add(new OrdersDO(1L,100L,3));
        list.add(new OrdersDO(5L,300L,4));
        list.add(new OrdersDO(5L,300L,4));
        //统计每个应用实际支付总额
        Map<Long, Long> tradeAmountMap = list.stream().filter(o->o.getStatus()==2)
                .collect(Collectors.groupingBy(OrdersDO::getAppId,
                        Collectors.summingLong(OrdersDO::getTradeAmount)));
        System.out.println(tradeAmountMap);

        //统计每个应用取消总额
        Map<Long, Long> cancelAmountMap = list.stream()
                .collect(Collectors.groupingBy(OrdersDO::getAppId,
                        Collectors.summingLong(OrdersDO::getTradeAmount)));
        System.out.println(cancelAmountMap);

        //统计每个应用下交易笔数
        Map<Long, Long> appTradeNum = list.stream().collect(Collectors.groupingBy(OrdersDO::getAppId, Collectors.counting()));
        System.out.println(appTradeNum);

        //统计每个应用每种状态下交易笔数
        Map<Long, Map<Integer, Long>> tradeNumMap = list.stream().
                collect(Collectors.groupingBy(OrdersDO::getAppId,
                        Collectors.groupingBy(OrdersDO::getStatus,
                                Collectors.counting())));
        System.out.println(tradeNumMap);

        //每个应用下交易笔数按数量排序
        Map<Long,Long> finalMap = new LinkedHashMap<>();
        appTradeNum.entrySet().stream().sorted(Map.Entry.<Long, Long>comparingByValue().reversed()).forEachOrdered(e->finalMap.put(e.getKey(),e.getValue()));
        System.out.println(finalMap);
    }


    @Data
    static class OrdersDO{
        private Long appId;
        private Long tradeAmount;
        private Integer status;

        OrdersDO(Long appId, Long tradeAmount, Integer status){
            this.appId = appId;
            this.tradeAmount = tradeAmount;
            this.status = status;
        }
    }
}
