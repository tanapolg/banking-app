package com.polhul.payment.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Created by TPolhul on 3/16/2018.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@ToString
@Table(name = "payment")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Client client;

    @NonNull
    @Column(name = "amount")
    private double amount;

    @NonNull
    @Column(name = "date")
    private long date;

    public Payment(Client client, double amount, long date) {
        this.client = client;
        this.amount = amount;
        this.date = date;
    }

}
