package com.polhul.payment.service;

import com.polhul.payment.repository.SessionRepository;
import com.polhul.payment.domain.Client;
import com.polhul.payment.domain.Session;
import com.polhul.payment.domain.SessionStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Service
@Transactional
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Long createSession(Client client) {
        int updatedClientSessions = sessionRepository.updateStatus(client.getId(), SessionStatus.EXPIRED.getMessage(), SessionStatus.ACTIVE.getMessage());
        Session session = new Session(System.currentTimeMillis(), client, SessionStatus.ACTIVE);
        Session createdSession = sessionRepository.save(session);
        return createdSession.getId();
    }

    @Override
    public Client getClientBySessionId(Long sessionId) {
        Session currentSession = sessionRepository.findOneById(sessionId);
        return currentSession.getClient();
    }

    @Override
    public void closeSession(Long sessionId) {
        int updatedClientSessions = sessionRepository.updateEndTimeAndStatus(sessionId, System.currentTimeMillis(), SessionStatus.EXPIRED.getMessage());
    }
}
