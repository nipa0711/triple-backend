package net.nipa0711.triplebackend.model;

import java.util.Date;

import lombok.Data;

@Data
public class Photo {
    private String photoId;
    private String reviewId;
    private Date registerTime;
}
