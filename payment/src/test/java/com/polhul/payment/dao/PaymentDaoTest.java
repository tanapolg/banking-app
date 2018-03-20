package com.polhul.payment.dao;

import com.polhul.payment.domain.Client;
import com.polhul.payment.domain.Payment;
import com.polhul.payment.domain.SessionStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by TPolhul on 3/19/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentDaoTest {

    @Autowired
    private PaymentDao dao;
    @Autowired
    private ClientDao clientDao;

    private long now = System.currentTimeMillis();

    @Test
    public void ifThereIsNoPaymentsForSumWithSuchClientIdNullIsReturned() {
        Double balanceSum = dao.getSumBalance(1l);
        assertThat(balanceSum, is(nullValue()));
    }

    @Test
    public void ifThereIsNoPaymentsWithSuchClientIdNullIsReturned() {
        List<Payment> payments = dao.findByClientIdOrderByDateDesc(1l);
        assertThat(payments, is(empty()));
    }

    @Test
    public void ifPaymentsForSumByClientIdAreFoundBalanceSumIsReturned() {
        Client client = new Client("Email", "Password");
        Client savedClient = clientDao.save(client);
        client.setId(savedClient.getId());

        Client client2 = new Client("Email2", "Password2");
        Client savedClient2 = clientDao.save(client2);
        client2.setId(savedClient2.getId());

        Payment payment1 = new Payment(client, 10.0, now);
        dao.save(payment1);
        Payment payment2 = new Payment(client, 15.0, now);
        dao.save(payment2);
        Payment payment3 = new Payment(client2, 345.0, now);
        dao.save(payment3);

        Double balanceSum = dao.getSumBalance(client.getId());
        assertThat(balanceSum, is(payment1.getAmount() + payment2.getAmount()));
    }

    @Test
    public void ifPaymentsForSumByClientIdAreFoundTheyAreReturned() {
        Client client = new Client("Email", "Password");
        Client savedClient = clientDao.save(client);
        client.setId(savedClient.getId());

        Client client2 = new Client("Email2", "Password2");
        Client savedClient2 = clientDao.save(client2);
        client2.setId(savedClient2.getId());

        Payment payment1 = new Payment(client, 10.0, now);
        Payment payment1Id = dao.save(payment1);
        payment1.setId(payment1Id.getId());
        Payment payment2 = new Payment(client, 15.0, now);
        Payment payment2Id = dao.save(payment2);
        payment2.setId(payment2Id.getId());
        Payment payment3 = new Payment(client2, 345.0, now);
        dao.save(payment3);

        List<Payment> payments = dao.findByClientIdOrderByDateDesc(client.getId());
        assertThat(payments, hasItem(payment1));
        assertThat(payments, hasItem(payment2));
        assertThat(payments, hasSize(2));
    }

}
