package com.polhul.payment.web;

import com.polhul.payment.domain.Client;
import com.polhul.payment.service.ClientService;
import com.polhul.payment.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by TPolhul on 3/16/2018.
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final SessionService sessionService;

    @PostMapping("/client")
    public Long registerClient(@RequestBody Client client) {
        clientService.checkClient(client);
        return clientService.registerClient(client);
    }

    @PostMapping("/client/signup")
    public Long signUp(@RequestBody Client client) {
        clientService.checkClient(client);
        Client signedUpClient = clientService.signUp(client);
        return sessionService.createSession(signedUpClient);
    }

    @PostMapping("/client/logout")
    public Long logout(@RequestHeader("X-Banking-Client-Token") Long clientToken) {
        clientService.checkClientToken(clientToken);
        sessionService.closeSession(clientToken);
        return clientToken;
    }

}
