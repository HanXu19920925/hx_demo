package com.example.demo.test.MallTest.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mall_stock")
@Data
public class StockDto implements Serializable {

    @Id
    @Column(name = "stock_id")
    private Integer stockId;

    @Column(name = "stock_num")
    private Integer stockNum;
}