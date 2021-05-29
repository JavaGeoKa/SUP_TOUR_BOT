package com.geo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;




@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="persons")
@DynamicUpdate
public class Person {

    @Id
    @Column(name = "iduser", unique = true, nullable = false)
    long id;

    @Column(name="FIRSTNAME")
    String firstName;

    @Column(name="LASTNAME")
    String lastName;

    @Column(name = "REQUESTS")
    Integer requests;

    @Column(name="CONNECTTIME")
    LocalDateTime connectTime;

    @Column(name="LAST_USE")
    LocalDateTime lastUseTime;




}
