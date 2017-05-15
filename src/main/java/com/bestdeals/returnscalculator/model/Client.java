package com.bestdeals.returnscalculator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public Client() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}