package com.example.testredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @PostMapping("/redisTest")
    public ResponseEntity<?> addRedisKey() {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set("yellow", "banana");
        vop.set("red", "apple");
        vop.set("green", "watermelon");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/redisTest/{key}")
    public ResponseEntity<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String value = vop.get(key);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }

    @GetMapping("/redisTest/flushall")
    public ResponseEntity<?> flushAll() {

        redisConnectionFactory.getClusterConnection().flushAll();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/redisTest/loop/{loop}/client/{client}/length/{length}")
    public ResponseEntity<?> testLoop(@PathVariable String loop, @PathVariable String client, @PathVariable String length) {
        String value = loop + ", " + client;
        int count = Integer.parseInt(client);
        int offset = Integer.parseInt(loop)/count;

        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        Report reporter = Report.getInstance();
        reporter.Init();

        TestConfig conf = new TestConfig();
        conf.MakeValue(Integer.parseInt(length));
        conf.setReporter(reporter);
        conf.setVop(vop);

        for(int i=0; i<count; i++) {
            conf.setStart(i*offset);
            conf.setEnd((i*offset)+offset);
            Thread thread = new Thread(new TestRedis(i, conf));
            thread.start();
        }

        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}
