package com.polhul.payment.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.polhul.payment.domain.Client;
import com.polhul.payment.service.ClientService;
import com.polhul.payment.service.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by TPolhul on 3/20/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ClientController.class)
public class ClientControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    @MockBean
    private ClientService clientService;
    @MockBean
    private SessionService sessionService;

    private Long notExistedClientToRegisterId = 12l;
    private Long existedClientToLoginId = 22l;
    private Client notExistedClientToRegisterWithoutId = new Client("wrongEemail@gmail.com", "wrongPassword");
    private Client notExistedClientToRegisterWithId = new Client("wrongEmail@gmail.com", "wrongPassword");
    private Client existedClientToLogin = new Client("correctEmail@gmail.com", "correctPassword");
    private long sessionId = 543l;

    private JacksonTester<Client> jsonClient;

    @Before
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());

        notExistedClientToRegisterWithId.setId(notExistedClientToRegisterId);
        existedClientToLogin.setId(existedClientToLoginId);

        doNothing().when(clientService).checkClient(notExistedClientToRegisterWithoutId);
        when(clientService.registerClient(notExistedClientToRegisterWithoutId)).thenReturn(notExistedClientToRegisterId);

        doNothing().when(clientService).checkClient(existedClientToLogin);
        when(clientService.login(existedClientToLogin)).thenReturn(existedClientToLogin);
        when(sessionService.createSession(existedClientToLogin)).thenReturn(existedClientToLoginId);

        doNothing().when(clientService).checkClientToken(sessionId);
        doNothing().when(sessionService).closeSession(sessionId);
    }

    @Test
    public void requestForRegisterIsSuccessfullyProcessedWithAvailableClient() throws Exception {

        MockHttpServletResponse response = this.mockMvc.perform(
                post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient.write(notExistedClientToRegisterWithoutId).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(clientService).checkClient(refEq(notExistedClientToRegisterWithoutId));
        verify(clientService).registerClient(refEq(notExistedClientToRegisterWithoutId));
    }

    @Test
    public void requestForLoginIsSuccessfullyProcessedWithAvailableClient() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/client/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonClient.write(existedClientToLogin).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(clientService).checkClient(refEq(existedClientToLogin));
        verify(clientService).login(refEq(existedClientToLogin));
    }

    @Test
    public void requestForLogOutIsSuccessfullyProcessedWithAvailableClientToken() throws Exception {
        MockHttpServletResponse response = this.mockMvc.perform(
                post("/client/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Banking-Client-Token", sessionId)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(clientService).checkClientToken(refEq(sessionId));
        verify(sessionService).closeSession(refEq(sessionId));
    }
}
