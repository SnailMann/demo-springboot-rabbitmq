package com.snailmann.rabbitmq.ack.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Student {

    private String name;
    private int age;
    private Date birstday;

}
