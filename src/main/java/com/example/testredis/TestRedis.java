package com.example.testredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestRedis implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(TestRedis.class);

    private final TestConfig conf;

    int id;

    public TestRedis(int id, TestConfig conf) {
        this.id = id;
        this.conf = conf;
    }

    @Override
    public void run() {

        LOGGER.info(id + " thread start");
        try{

            conf.Ref();

            //Thread.sleep(1);

            for(int i=conf.getStart(); i<conf.getEnd(); i++) {
                String key = String.format("%06d", i);
                conf.getVop().set(key, conf.getValue());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        synchronized (this) {
            conf.UnRef();

            String str = String.format("%d thread end %d~%d rest:%d", id, conf.getStart(), conf.getEnd(), conf.getReporter().Count());
            LOGGER.info(str);

            if (conf.getReporter().Count() <= 0) {
                LOGGER.info(String.format("REPORT %s", conf.getReporter().Done()));
            }
        }
    }
}
