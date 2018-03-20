package com.polhul.payment.service;

import com.polhul.payment.AppException;
import com.polhul.payment.StatusCode;
import com.polhul.payment.dao.ClientDao;
import com.polhul.payment.domain.Client;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

/**
 * Created by TPolhul on 3/19/2018.
 */
@RunWith(value = Parameterized.class)
public class ParamCheckClientClientServiceTest {

    private Client client;
    private StatusCode statusCode;
    private String errorMessage;

    public ParamCheckClientClientServiceTest(Client client, StatusCode statusCode, String errorMessage) {
        this.client = client;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    private ClientDao dao;
    private ClientService service;
    private MockMvc mockMvc;

    @Before
    public void init() {
        dao = Mockito.mock(ClientDao.class);
        service = new ClientServiceImpl(dao);
        mockMvc = MockMvcBuilders.standaloneSetup(service).build();
    }

    @Parameterized.Parameters(name = "{index}: cancelTransactionTestExecute({0}, {1}, {2}, {3})")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        null,
                        StatusCode.MISSING_PARAMETER,
                        "no client found"
                },
                {
                        new Client(null, "password"),
                        StatusCode.MISSING_PARAMETER,
                        "no client email found"
                },
                {
                        new Client("", "password"),
                        StatusCode.INVALID_VALUE,
                        "client email '' is not acceptable"
                },
                {
                        new Client("email@gmail.com", null),
                        StatusCode.MISSING_PARAMETER,
                        "no client password found"
                },
                {
                        new Client("email@gmail.com", ""),
                        StatusCode.INVALID_VALUE,
                        "client password '' is not acceptable"
                }
        });
    }

    @Test
    public void testCheckClientExecuteWithExceptionCheckExceptionClassAndCodeAndMessage() {
        Assertions.assertThatThrownBy(() -> {
            service.checkClient(client);
        })
                .isInstanceOf(AppException.class)
                .hasMessageContaining(errorMessage);
    }
}
