package net.nipa0711.triplebackend.model;

import java.util.Date;

import lombok.Data;

@Data
public class History {
    private int idx;
    private String userId;
    private int point;
    private String content;
    private Date time;
}
