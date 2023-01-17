package com.example.demo.test.MallTest.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mall_log")
@Data
public class LogDto implements Serializable {

    @Id
    @Column(name = "log_id")
    private Integer LogId;

    @Column(name = "log_info")
    private String logInfo;
}
