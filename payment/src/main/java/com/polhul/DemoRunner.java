package com.polhul;

import com.polhul.payment.domain.Client;
import com.polhul.payment.dto.PaymentDto;
import com.polhul.payment.service.ClientService;
import com.polhul.payment.service.PaymentService;
import com.polhul.payment.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Component
@AllArgsConstructor
public class DemoRunner implements CommandLineRunner {
    private final PaymentService paymentService;
    private final ClientService clientService;
    private final SessionService sessionService;

    @Override
    public void run(String... strings) throws Exception {
        //TODO: it
        String email = UUID.randomUUID().toString();
        Client clientToRegister = new Client(email, "password");
        Long clientId = clientService.registerClient(clientToRegister);
        System.out.println("register");
        Client clientToLogin = new Client(email, "password");
        Client loggedInClient = clientService.login(clientToLogin);
        System.out.println("login");
        Long sessionId = sessionService.createSession(loggedInClient);
        System.out.println("createSession");

        paymentService.deposit(sessionId, 100.0);
        System.out.println("deposit 100.0");
        paymentService.withdraw(sessionId, 10.1);
        System.out.println("withdraw 10.1");
        List<PaymentDto> statements = paymentService.accountStatement(sessionId);
        System.out.println("accountStatement ");
        for (PaymentDto statement: statements) {
            System.out.println("amount: " + statement.getAmount() + ", date: " + statement.getDate());
        }
        Double accountBalance = paymentService.accountBalance(sessionId);
        System.out.println("accountBalance: " + accountBalance);
        sessionService.closeSession(sessionId);
        System.out.println("closeSession");
    }
}
