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
@Table(name = "session")
public class Session {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Client client;

    @NonNull
    @Column(name = "startTime")
    private long startTime;

    @NonNull
    @Column(name = "status")
    private String status;

    @Column(name = "endTime")
    private Long endTime;

    public Session(long startTime, Client client, SessionStatus status) {
        this.startTime = startTime;
        this.client = client;
        this.status = status.getMessage();
    }

    public Session(long startTime, Client client, SessionStatus status, Long endTime) {
        this.startTime = startTime;
        this.client = client;
        this.status = status.getMessage();
        this.endTime = endTime;
    }

}
