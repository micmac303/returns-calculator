package com.bestdeals.returnscalculator.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Deal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long returnAmount;
    @ManyToOne
    private Client client;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(long returnAmount) {
        this.returnAmount = returnAmount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
