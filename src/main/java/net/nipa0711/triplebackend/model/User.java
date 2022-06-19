package net.nipa0711.triplebackend.model;

import java.sql.Date;

import lombok.Data;

@Data
public class User {
    private int id;
    private String userId;
    private int totalPoint;
    private Date createdTime;
}
