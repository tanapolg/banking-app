package com.polhul.payment.service;

import com.polhul.payment.domain.Client;

/**
 * Created by TPolhul on 3/16/2018.
 */
public interface SessionService {
    Long createSession(Client client);

    Client getClientBySessionId(Long sessionId);

    void closeSession(Long sessionId);
}
