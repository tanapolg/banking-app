package com.polhul.payment.service;

import com.polhul.payment.repository.SessionRepository;
import com.polhul.payment.domain.Client;
import com.polhul.payment.domain.Session;
import com.polhul.payment.domain.SessionStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * Created by TPolhul on 3/19/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionServiceImplTest {

    @Mock
    private SessionRepository sessionRepository;

    private SessionService sessionService;


    @Before
    public void init() {
        sessionService = new SessionServiceImpl(sessionRepository);
    }

    @Test
    public void getClientBySessionId() {
        Client existedClient = new Client("correct-email@gmail.com", "correct-password");
        existedClient.setId(123);

        long sessionStartTime = System.currentTimeMillis();
        long sessionId = 234;
        Session createdSessionWithId = new Session(sessionStartTime, existedClient, SessionStatus.ACTIVE);
        createdSessionWithId.setId(sessionId);
        when(sessionRepository.findOneById(sessionId)).thenReturn(createdSessionWithId);
        Client client = sessionService.getClientBySessionId(sessionId);

        Assert.assertNotNull(client);
        Assert.assertEquals(client, existedClient);
        Assert.assertEquals(client.getId(), existedClient.getId());
        Assert.assertEquals(client.getEmail(), existedClient.getEmail());
    }



}
