package com.polhul.payment.service;

import com.polhul.payment.AppException;
import com.polhul.payment.dao.ClientDao;
import com.polhul.payment.domain.Client;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by TPolhul on 3/19/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientServiceImplTest {
    @Mock
    private ClientDao clientDao;

    private ClientService clientService;

    @Before
    public void init() {
        clientService = new ClientServiceImpl(clientDao);
    }

    @Test
    public void ifNoClientExistOnSignUpExceptionIsReturned() {
        Client notExistedClientToSignUp = new Client("wrong-email@gmail.com", "wrong-password");
        Client existedClient = new Client("correct-email@gmail.com", "correct-password");
        Assertions.assertThatThrownBy(() -> {
            when(clientDao.findOneClientByEmail(existedClient.getEmail())).thenReturn(existedClient);
            Client signedUpClient = clientService.signUp(notExistedClientToSignUp);
        })
                .isInstanceOf(AppException.class)
                .hasMessageContaining("client with this email doesn't exist");
    }

    @Test
    public void ifClientExistOnSignUpClientIdIsReturned() {
        Client existedClient = new Client("correct-email@gmail.com", "correct-password");
        existedClient.setId(123);
        Client existedClientWithEncodedPassword = new Client(existedClient.getEmail(), clientService.simpleEncodePassword(existedClient.getPassword()));
        existedClientWithEncodedPassword.setId(123);

        when(clientDao.findOneClientByEmail(existedClient.getEmail())).thenReturn(existedClientWithEncodedPassword);
        Client signedUpClient = clientService.signUp(existedClient);

        assertThat(signedUpClient.getId(), is(existedClient.getId()));
    }

    @Test
    public void ifRegisterNewClientClientIdIsReturned() {
        Client newClientToRegister = new Client("register-correct-email@gmail.com", "correct-password");
        newClientToRegister.setId(123);
        when(clientDao.findOneClientByEmail(newClientToRegister.getEmail())).thenReturn(null);
        when(clientDao.save(newClientToRegister)).thenReturn(newClientToRegister);
        Long registerClientId = clientService.registerClient(newClientToRegister);
        assertThat(registerClientId, is(newClientToRegister.getId()));
    }

    @Test
    public void ifRegisterExistedClientExceptionIsReturned() {
        Assertions.assertThatThrownBy(() -> {
            Client existedClientToRegister = new Client("register-correct-email@gmail.com", "correct-password");
            existedClientToRegister.setId(123);
            when(clientDao.findOneClientByEmail(existedClientToRegister.getEmail())).thenReturn(existedClientToRegister);
            when(clientDao.save(existedClientToRegister)).thenReturn(existedClientToRegister);
            Long registerClientId = clientService.registerClient(existedClientToRegister);
            assertThat(registerClientId, is(existedClientToRegister.getId()));
        })
                .isInstanceOf(AppException.class)
                .hasMessageContaining("client with this email already exist");
    }

}
