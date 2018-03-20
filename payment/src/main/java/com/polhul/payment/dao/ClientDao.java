package com.polhul.payment.dao;

import com.polhul.payment.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Repository
public interface ClientDao extends JpaRepository<Client, String> {

    Client findOneClientByEmail(String email);

    Client findOneByEmailAndPassword(String email, String password);
}
