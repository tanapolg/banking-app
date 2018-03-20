package com.polhul.payment.repository;

import com.polhul.payment.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    Client findOneClientByEmail(String email);

    Client findOneByEmailAndPassword(String email, String password);
}
