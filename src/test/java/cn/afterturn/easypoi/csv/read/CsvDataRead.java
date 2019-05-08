package cn.afterturn.easypoi.csv.read;

import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author by jueyue on 18-10-3.
 */
public class CsvDataRead {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDataRead.class);


    @Test
    public void test() {
        try {
            Date start = new Date();
            LOGGER.debug("start");
            CsvImportParams params = new CsvImportParams(CsvImportParams.GBK);
            params.setTitleRows(1);
//            CsvImportUtil.importCsv(new FileInputStream(
//                            new File(FileUtilTest.getWebRootPath("csv/BigDataExport.csv"))),
//                    MsgClient.class, params, (client) -> {
//                        //LOGGER.info(JSON.toJson(client));
//                    });
            LOGGER.debug("end,time is {}", ((new Date().getTime() - start.getTime()) / 1000));
        } catch (Exception e) {
        }
    }


    @Test
    public void testUtf8Bom() {
        try {
            Date start = new Date();
            LOGGER.debug("start");
            CsvImportParams params = new CsvImportParams(CsvImportParams.UTF8);
//            CsvImportUtil.importCsv(new FileInputStream(
//                            new File(FileUtilTest.getWebRootPath("csv/20181107202743.csv"))),
//                    Map.class, params, (client) -> {
//                        LOGGER.info(JSON.toJson(client));
//                    });
            LOGGER.debug("end,time is {}", ((new Date().getTime() - start.getTime()) / 1000));
        } catch (Exception e) {
        }
    }
}
