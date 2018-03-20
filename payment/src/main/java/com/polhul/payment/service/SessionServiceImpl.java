package com.polhul.payment.service;

import com.polhul.payment.dao.SessionDao;
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
    private final SessionDao sessionDao;

    public SessionServiceImpl(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public Long createSession(Client client) {
        int updatedClientSessions = sessionDao.updateStatus(client.getId(), SessionStatus.EXPIRED.getMessage(), SessionStatus.ACTIVE.getMessage());
        Session session = new Session(System.currentTimeMillis(), client, SessionStatus.ACTIVE);
        Session createdSession = sessionDao.save(session);
        return createdSession.getId();
    }

    @Override
    public Client getClientBySessionId(Long sessionId) {
        Session currentSession = sessionDao.findOneById(sessionId);
        return currentSession.getClient();
    }

    @Override
    public void closeSession(Long sessionId) {
        int updatedClientSessions = sessionDao.updateEndTimeAndStatus(sessionId, System.currentTimeMillis(), SessionStatus.EXPIRED.getMessage());
    }
}
