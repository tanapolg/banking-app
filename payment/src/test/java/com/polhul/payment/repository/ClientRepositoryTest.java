package com.polhul.payment.repository;

import com.polhul.payment.domain.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by TPolhul on 3/19/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository dao;

    @Test
    public void ifThereIsNoClientWithSuchEmailNullIsReturned() {
        assertThat(dao.findOneClientByEmail("unknownEmail"), is(nullValue()));
    }

    @Test
    public void ifThereIsNoClientWithSuchEmailAndPasswordNullIsReturned() {
        assertThat(dao.findOneByEmailAndPassword("unknownEmail", "unknownPassword"), is(nullValue()));
    }

    @Test
    public void ifClientByEmailAreFoundTheyAreReturned() {
        Client client = new Client("Email", "Password");
        Client savedClient = dao.save(client);
        client.setId(savedClient.getId());
        assertThat(dao.findOneClientByEmail("Email"), is(samePropertyValuesAs(client)));
    }

    @Test
    public void ifClientByEmailAndPasswordAreFoundTheyAreReturned() {
        Client client = new Client("Email", "Password");
        Client savedClient = dao.save(client);
        client.setId(savedClient.getId());
        assertThat(dao.findOneByEmailAndPassword("Email", "Password"), is(samePropertyValuesAs(client)));
    }

}
