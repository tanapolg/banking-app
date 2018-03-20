package com.polhul.payment.service;

import com.polhul.payment.domain.Client;
import org.springframework.stereotype.Service;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Service
public interface ClientService {

    void checkClient(Client client);

    Long registerClient(Client client);

    String simpleEncodePassword(String password);

    Client login(Client client);

    void checkClientToken(Long clientToken);
}
