package com.snailmann.rabbitmq.variousproducer.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Student implements Serializable {

    private static final long serialVersionUID = 6360539502506218671L;

    private String name;
    private int age;
    private Date birstday;

}
