package net.nipa0711.triplebackend.model;

import java.util.Date;

import lombok.Data;

@Data
public class Photo {
    private String userId;
    private String placeId;
    private String photoId;
    private Date registerTime;
}
