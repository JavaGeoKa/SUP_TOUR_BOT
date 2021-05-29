package com.geo.dao;


import com.geo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
@Transactional
public interface PersonRepositoty extends JpaRepository<Person, Long> {

//    @Query("SELECT p.carma FROM Person p WHERE p.id = :id")
//    Integer getCarma(@Param("id") long id);
//

//    @Modifying
//    @Query("UPDATE Person p set p.lastUseTime = :lastUseTime where p.id = :id")
//    void updateLastRequestTime(@Param(value = "id") long id, @Param(value = "lastUseTime") LocalDateTime lastUseTime);
//
//    @Modifying
//    @Query("UPDATE Person p set p.requests = :requests where p.id = :id")
//    void updateRequests(@Param(value = "id") long id, @Param(value = "requests") Integer requests);


}
