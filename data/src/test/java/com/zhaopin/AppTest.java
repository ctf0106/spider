package com.zhaopin;

import static org.junit.Assert.assertTrue;

import com.zhaopin.build.DataBaseFactory;
import com.zhaopin.config.DynamicDbConfig;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        DynamicDbConfig dbConfig=new DynamicDbConfig();
        dbConfig.setDbName("haitou20191209");
        try {
            DataBaseFactory dataBaseFactory=new DataBaseFactory(dbConfig);
            dataBaseFactory.createCrawlerDB();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
