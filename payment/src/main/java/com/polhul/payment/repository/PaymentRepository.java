package com.polhul.payment.repository;

import com.polhul.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT SUM(p.amount) from Payment p WHERE p.client.id= :id")
    Double getSumBalance(@Param("id") long id);

    List<Payment> findByClientIdOrderByDateDesc(long id);
}
