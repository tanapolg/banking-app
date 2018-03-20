package com.polhul.payment.repository;

import com.polhul.payment.domain.Client;
import com.polhul.payment.domain.Session;
import com.polhul.payment.domain.SessionStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by TPolhul on 3/19/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SessionRepositoryTest {

    @Autowired
    private SessionRepository dao;
    @Autowired
    private ClientRepository clientDao;

    private long now = System.currentTimeMillis();

    @Test
    public void ifThereIsNoSessionWithSuchIdZeroIsReturned() {
        int updatedSessions = dao.updateEndTimeAndStatus(1l, now, SessionStatus.EXPIRED.toString());
        assertThat(updatedSessions, is(0));
    }

    @Test
    public void ifThereIsNoSessionWithSuchClientIdZeroIsReturned() {
        int updatedSessions = dao.updateStatus(1l, SessionStatus.EXPIRED.toString(), SessionStatus.ACTIVE.toString());
        assertThat(updatedSessions, is(0));
    }

    @Test
    public void ifSessionByIdAreFoundTheyAreReturned() {
        Client client = new Client("Email", "Password");
        Client savedClient = clientDao.save(client);
        client.setId(savedClient.getId());
        Session session = new Session(now, client, SessionStatus.ACTIVE);
        Session savedSession = dao.save(session);
        session.setId(savedSession.getId());
        Session session2 = new Session(now, client, SessionStatus.ACTIVE);
        Session savedSession2 = dao.save(session2);
        session2.setId(savedSession2.getId());
        int updatedSessions = dao.updateEndTimeAndStatus(session.getId(), now, SessionStatus.EXPIRED.toString());
        assertThat(updatedSessions, is(1));
    }
}
