package com.example.demo.test.MallTest.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "mall_order")
@Data
public class OrderDto implements Serializable {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "order_status")
    private String orderStatus;
}
