package com.polhul.payment.web;

import com.polhul.payment.dto.PaymentDto;
import com.polhul.payment.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.polhul.payment.service.PaymentService;

import java.util.List;

/**
 * Created by TPolhul on 3/16/2018.
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PaymentController {
    private final ClientService clientService;
    private final PaymentService paymentService;

    @GetMapping("/payment/accountBalance")
    public Double accountBalance(@RequestHeader("X-Banking-Client-Token") Long clientToken) {
        clientService.checkClientToken(clientToken);
        return paymentService.accountBalance(clientToken);
    }

    @GetMapping("/payment/accountStatement")
    public List<PaymentDto> accountStatement(@RequestHeader("X-Banking-Client-Token") Long clientToken) {
        clientService.checkClientToken(clientToken);
        return paymentService.accountStatement(clientToken);
    }

    @PostMapping("/payment/deposit")
    public Long deposit(@RequestHeader("X-Banking-Client-Token") Long clientToken, @RequestBody Double amount) {
        clientService.checkClientToken(clientToken);
        paymentService.checkDepositAmount(amount);
        return paymentService.deposit(clientToken, amount);
    }

    @PostMapping("/payment/withdraw")
    public Long withdraw(@RequestHeader("X-Banking-Client-Token") Long clientToken, @RequestBody Double amount) {
        clientService.checkClientToken(clientToken);
        paymentService.checkWithdrawAmount(amount);
        return paymentService.withdraw(clientToken, amount);
    }

}
