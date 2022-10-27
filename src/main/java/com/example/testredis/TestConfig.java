package com.example.testredis;

import lombok.Data;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Random;

@Data
public class TestConfig {
    private int start;
    private int end;
    private Report reporter;
    private ValueOperations<String, String> vop;

    private String value;

    public void MakeValue(int length) {
        Random random = new Random();
        value = random.ints(97, 123)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public void Ref() {
        reporter.Ref(end-start);
    }

    public void UnRef() {
        reporter.UnRef();
    }
}
