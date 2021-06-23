package com.geo.model;


import com.geo.enumerators.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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

    @Column(name = "USER_ROLES")
    @Enumerated(EnumType.STRING)
    private UserRoles userRoles;




}
