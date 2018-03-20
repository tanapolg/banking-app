package com.polhul.payment.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.polhul.payment.dto.PaymentDto;
import com.polhul.payment.service.ClientService;
import com.polhul.payment.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by TPolhul on 3/20/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PaymentController.class)
public class PaymentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    @MockBean
    private ClientService clientService;
    @MockBean
    private PaymentService paymentService;

    private JacksonTester<Double> jsonDouble;
    private JacksonTester<List<PaymentDto>> jsonPaymentDto;

    private long sessionId = 123l;
    private long firstTime = System.currentTimeMillis();
    private long secondTime = System.currentTimeMillis();
    private long thirdTime = System.currentTimeMillis();
    private List<PaymentDto> correctResult = new ArrayList<PaymentDto>() {{
        add(new PaymentDto(10.0, DateFormat.getDateTimeInstance().format(firstTime)));
        add(new PaymentDto(-10.0, DateFormat.getDateTimeInstance().format(secondTime)));
        add(new PaymentDto(100.0, DateFormat.getDateTimeInstance().format(thirdTime)));
    }};

    private double amount = 10.0;
    private Long paymentId = 11l;
    private Double accountBalance = 100.0;

    @Before
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());

        when(paymentService.accountBalance(sessionId)).thenReturn(accountBalance);

        doNothing().when(clientService).checkClientToken(sessionId);
        when(paymentService.accountStatement(sessionId)).thenReturn(correctResult);

        doNothing().when(paymentService).checkDepositAmount(amount);
        when(paymentService.deposit(sessionId, amount)).thenReturn(paymentId);

        doNothing().when(paymentService).checkWithdrawAmount(amount);
        when(paymentService.withdraw(sessionId, amount)).thenReturn(paymentId);
    }

    @Test
    public void requestForAccountBalanceIsSuccessfullyProcessedWithAvailableClientToken() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/payment/accountBalance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Banking-Client-Token", sessionId)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(accountBalance.toString());
        verify(clientService).checkClientToken(refEq(sessionId));
        verify(paymentService).accountBalance(refEq(sessionId));
    }

    @Test
    public void requestForAccountStatementIsSuccessfullyProcessedWithAvailableClientToken() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                get("/payment/accountStatement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Banking-Client-Token", sessionId)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonPaymentDto.write(correctResult).getJson());
        verify(clientService).checkClientToken(refEq(sessionId));
        verify(paymentService).accountStatement(refEq(sessionId));
    }

    @Test
    public void requestForDepositIsSuccessfullyProcessedWithAvailableClientTokenAndAmount() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/payment/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Banking-Client-Token", sessionId)
                        .content(jsonDouble.write(amount).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(paymentId.toString());
        verify(clientService).checkClientToken(refEq(sessionId));
        verify(paymentService).checkDepositAmount(refEq(amount));
        verify(paymentService).deposit(refEq(sessionId), refEq(amount));
    }

    @Test
    public void requestForWithdrawIsSuccessfullyProcessedWithAvailableClientTokenAndAmount() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/payment/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Banking-Client-Token", sessionId)
                        .content(jsonDouble.write(amount).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(paymentId.toString());
        verify(clientService).checkClientToken(refEq(sessionId));
        verify(paymentService).checkWithdrawAmount(refEq(amount));
        verify(paymentService).withdraw(refEq(sessionId), refEq(amount));
    }
}
