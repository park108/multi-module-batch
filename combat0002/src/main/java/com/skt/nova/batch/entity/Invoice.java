package com.skt.nova.batch.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "invoice", schema = "billing")
public class Invoice {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    private String status;

    private String currency;

    private Long amount;

    private String description;

    public Invoice() {}
    public Invoice(UUID id,
            String status,
            String currency,
            Long amount,
            String description
    ) {
        this.id = id;
        this.status = status;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
    }

    public UUID getId() {        return id;    }
    public void setId(UUID id) {this.id = id;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status; }
    public String getCurrency() {return currency;}
    public void setCurrency(String currency) {this.currency = currency;}
    public Long getAmount() {return amount;}
    public void setAmount(Long amount) {this.amount = amount;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
