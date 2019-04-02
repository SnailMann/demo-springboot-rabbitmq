package com.rabbitmq.rabbitmq.variousconsumer.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Student implements Serializable {

    private static final long serialVersionUID = -3740260395462729232L;
    private String name;
    private int age;
    private Date birstday;

}
