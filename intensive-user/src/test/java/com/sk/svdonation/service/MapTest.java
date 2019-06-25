package com.sk.svdonation.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

public class MapTest {
    @Test
    public void computeTest() {
        Map<String, String> test = new HashMap<>();
        test.put("TEST", "TEST222");
        test.compute("TEST", (key, value) -> Optional.ofNullable(value).map(v -> v + " TEST").orElse("HELLO"));

        System.out.println(test);
    }
}