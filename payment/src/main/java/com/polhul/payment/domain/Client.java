package com.polhul.payment.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@ToString
@Table(name = "client")
public class Client {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NonNull
    @Column(name = "email")
    private String email;

    @NonNull
    @Column(name = "password")
    private String password;

    @OneToMany( cascade = {CascadeType.ALL}, mappedBy = "client" )
    private List<Session> sessions;

    @OneToMany( cascade = {CascadeType.ALL}, mappedBy = "client" )
    private List<Payment> payments;

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
