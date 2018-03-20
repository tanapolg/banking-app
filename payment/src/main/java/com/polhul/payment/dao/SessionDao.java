package com.polhul.payment.dao;

import com.polhul.payment.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Repository
public interface SessionDao extends JpaRepository<Session, String> {

    Session findOneById(Long clientToken);

    @Modifying
    @Query("UPDATE Session s SET s.endTime = :endTime, s.status = :status WHERE s.id = :id")
    int updateEndTimeAndStatus(@Param("id") long id, @Param("endTime") Long endTime, @Param("status") String status);

    @Modifying
    @Query("UPDATE Session s SET s.status = :status WHERE s.client.id = :clientId AND s.status = :oldStatus")
    int updateStatus(@Param("clientId") long clientId, @Param("status") String status, @Param("oldStatus") String oldStatus);

}
