package com.polhul.payment.service;

import com.polhul.payment.AppException;
import com.polhul.payment.StatusCode;
import com.polhul.payment.repository.PaymentRepository;
import com.polhul.payment.domain.Client;
import com.polhul.payment.domain.Payment;
import com.polhul.payment.dto.PaymentDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final SessionService sessionService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, SessionService sessionService) {
        this.paymentRepository = paymentRepository;
        this.sessionService = sessionService;
    }

    @Override
    public double accountBalance(Long sessionId) {
        Client client = sessionService.getClientBySessionId(sessionId);
        Double currentAccountAmount = paymentRepository.getSumBalance(client.getId());
        return (currentAccountAmount != null) ? currentAccountAmount : 0.0;
    }

    @Override
    public List<PaymentDto> accountStatement(Long sessionId) {
        Client client = sessionService.getClientBySessionId(sessionId);
        List<Payment> accountStatement = paymentRepository.findByClientIdOrderByDateDesc(client.getId());
        return accountStatement.stream().map(payment -> convertToDto(payment)).collect(Collectors.toList());
    }

    private PaymentDto convertToDto(Payment payment) {
        return new PaymentDto(payment.getAmount(), DateFormat.getDateTimeInstance().format(payment.getDate()));
    }

    @Override
    public Long deposit(Long sessionId, Double amount) {
        Client client = sessionService.getClientBySessionId(sessionId);
        Payment deposit = new Payment(client, amount, System.currentTimeMillis());
        return paymentRepository.save(deposit).getId();
    }

    @Override
    public Long withdraw(Long sessionId, Double amount) {
        Client client = sessionService.getClientBySessionId(sessionId);
        Double currentAccountAmount = paymentRepository.getSumBalance(client.getId());
        currentAccountAmount = (currentAccountAmount != null) ? currentAccountAmount : 0.0;
        if (currentAccountAmount >= amount) {
            Payment deposit = new Payment(client, -1 * amount, System.currentTimeMillis());
            return paymentRepository.save(deposit).getId();
        } else {
            throw new AppException("not enough money on account", StatusCode.WITHDRAW_NOT_ACCEPTED);
        }
    }

    @Override
    public void checkDepositAmount(Double amount) {
        if (amount == null) {
            throw new AppException("deposit amount of money can't be null", StatusCode.MISSING_PARAMETER);
        }
        if (amount <= 0) {
            throw new AppException("deposit amount of money '$amount' is not acceptable", StatusCode.INVALID_VALUE);
        }
    }

    @Override
    public void checkWithdrawAmount(Double amount) {
        if (amount == null) {
            throw new AppException("withdraw amount of money can't be null", StatusCode.MISSING_PARAMETER);
        }
        if (amount <= 0) {
            throw new AppException("withdraw amount of money '$amount' is not acceptable", StatusCode.INVALID_VALUE);
        }
    }
}
