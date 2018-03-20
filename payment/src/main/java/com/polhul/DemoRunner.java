package com.polhul;

import com.polhul.payment.domain.Client;
import com.polhul.payment.dto.PaymentDto;
import com.polhul.payment.service.ClientService;
import com.polhul.payment.service.PaymentService;
import com.polhul.payment.service.SessionService;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Component
public class DemoRunner implements CommandLineRunner {

    private Logger LOG = LoggerFactory.getLogger(DemoRunner.class);

    private final PaymentService paymentService;
    private final ClientService clientService;
    private final SessionService sessionService;

    public DemoRunner(PaymentService paymentService, ClientService clientService, SessionService sessionService) {
        this.paymentService = paymentService;
        this.clientService = clientService;
        this.sessionService = sessionService;
    }

    @Override
    public void run(String... strings) throws Exception {
        String email = UUID.randomUUID().toString();
        Client clientToRegister = new Client(email, "password");
        Long clientId = clientService.registerClient(clientToRegister);
        LOG.info("register");
        Client clientToLogin = new Client(email, "password");
        Client loggedInClient = clientService.login(clientToLogin);
        LOG.info("login");
        Long sessionId = sessionService.createSession(loggedInClient);
        LOG.info("createSession");

        paymentService.deposit(sessionId, 100.0);
        LOG.info("deposit 100.0");
        paymentService.withdraw(sessionId, 10.1);
        LOG.info("withdraw 10.1");
        List<PaymentDto> statements = paymentService.accountStatement(sessionId);
        LOG.info("accountStatement ");
        for (PaymentDto statement: statements) {
            LOG.info("amount: " + statement.getAmount() + ", date: " + statement.getDate());
        }
        Double accountBalance = paymentService.accountBalance(sessionId);
        LOG.info("accountBalance: " + accountBalance);
        sessionService.closeSession(sessionId);
        LOG.info("closeSession");
    }
}
