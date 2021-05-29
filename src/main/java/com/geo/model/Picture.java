package com.geo.model;


import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Builder
@Data
@Entity
public class Picture {
    @Id
    Integer id;

    String name;

    @Lob
    byte[] picture;
}
