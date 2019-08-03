package com.snailmann.rabbitmq.dle.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitServiceTest {

    @Autowired
    RabbitService rabbitService;

    @Test
    public void dleTest() {
        rabbitService.dleTest();
    }
}