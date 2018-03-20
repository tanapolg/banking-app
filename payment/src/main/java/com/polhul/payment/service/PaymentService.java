package com.polhul.payment.service;

import com.polhul.payment.dto.PaymentDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Service
public interface PaymentService {

    double accountBalance(Long sessionId);

    List<PaymentDto> accountStatement(Long sessionId);

    Long deposit(Long sessionId, Double amount);

    Long withdraw(Long sessionId, Double amount);

    void checkDepositAmount(Double amount);

    void checkWithdrawAmount(Double amount);
}
