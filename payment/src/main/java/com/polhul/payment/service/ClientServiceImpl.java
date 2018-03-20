package com.polhul.payment.service;

import com.polhul.payment.AppException;
import com.polhul.payment.StatusCode;
import com.polhul.payment.repository.ClientRepository;
import com.polhul.payment.domain.Client;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void checkClient(Client client) {
        if (client == null) {
            throw new AppException("no client found", StatusCode.MISSING_PARAMETER);
        }
        if (client.getEmail() == null) {
            throw new AppException("no client email found", StatusCode.MISSING_PARAMETER);
        }
        if (client.getEmail().isEmpty()) {
            throw new AppException("client email '' is not acceptable", StatusCode.INVALID_VALUE);
        }
        //TODO: check email on pattern
        if (client.getPassword() == null) {
            throw new AppException("no client password found", StatusCode.MISSING_PARAMETER);
        }
        if (client.getPassword().isEmpty()) {
            throw new AppException("client password '' is not acceptable", StatusCode.INVALID_VALUE);
        }
    }

    @Override
    public Long registerClient(Client client) {
        if (isClientExist(client)) {
            throw new AppException("client with this email already exist", StatusCode.CLIENT_ALREADY_EXIST);
        }
        String password = simpleEncodePassword(client.getPassword());
        client.setPassword(password);
        Client registeredClient = clientRepository.save(client);
        return registeredClient.getId();
    }

    private boolean isClientExist(Client client) {
        Client foundClient = clientRepository.findOneClientByEmail(client.getEmail());
        return foundClient != null;
    }

    public String simpleEncodePassword(String password) {
        byte[] pwBytes = DigestUtils.sha(password);
        return Base64.encodeBase64String(pwBytes);
    }

    @Override
    public Client login(Client client) {
        if (!isClientExist(client)) {
            throw new AppException("client with this email doesn't exist", StatusCode.NO_CLIENT_EXIST);
        }
        Client foundClient = clientRepository.findOneClientByEmail(client.getEmail());
        String encryptedPassword = simpleEncodePassword(client.getPassword());
        if (!encryptedPassword.equals(foundClient.getPassword())) {
            throw new AppException("client with this email and password doesn't exist", StatusCode.NO_CLIENT_EXIST);
        }
        return foundClient;
    }

    @Override
    public void checkClientToken(Long clientToken) {
        if (clientToken == null)
            throw new AppException("no 'X-Banking-Client-Token' header found", StatusCode.MISSING_PARAMETER);
    }
}
