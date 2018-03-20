package com.polhul.payment.service;

import com.polhul.payment.dao.PaymentDao;
import com.polhul.payment.domain.Client;
import com.polhul.payment.domain.Payment;
import com.polhul.payment.dto.PaymentDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by TPolhul on 3/19/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentDao paymentDao;
    @Mock
    private SessionService sessionService;

    private PaymentService paymentService;

    @Before
    public void init() {
        paymentService = new PaymentServiceImpl(paymentDao, sessionService);
    }

    @Test
    public void accountStatementReturned() {
        long sessionId = 333l;
        long clientId = 123l;
        Client existedClient = new Client("correct-email@gmail.com", "correct-password");
        existedClient.setId(clientId);
        long firstTime = System.currentTimeMillis();
        long secondTime = System.currentTimeMillis();
        long thirdTime = System.currentTimeMillis();
        List<Payment> payments = new ArrayList<Payment>() {{
            add(new Payment(existedClient, 10.0, firstTime));
            add(new Payment(existedClient, -10.0, secondTime));
            add(new Payment(existedClient, 100.0, thirdTime));
        }};

        when(sessionService.getClientBySessionId(sessionId)).thenReturn(existedClient);
        when(paymentDao.findByClientIdOrderByDateDesc(clientId)).thenReturn(payments);
        List<PaymentDto> result = paymentService.accountStatement(sessionId);

        Assert.assertNotNull(result);
        assertThat(result, hasSize(3));
    }

}
