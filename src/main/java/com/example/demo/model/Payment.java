package com.example.demo.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer orderId;

    private String transactionId;

    private Double amount;

    private String paymentType;  // DEBIT_CARD / UPI / CREDIT_CARD

    private String status;  // SUCCESS / FAILED / PENDING

    private String cardLast4;

    private LocalDateTime paymentDate;

    @Transient
    private String temporaryNote;

    // No-args constructor
    public Payment() {}

    // All-args constructor
    public Payment(Integer id, Integer orderId, String transactionId, Double amount, String paymentType,
                   String status, String cardLast4, LocalDateTime paymentDate, String temporaryNote) {
        this.id = id;
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.status = status;
        this.cardLast4 = cardLast4;
        this.paymentDate = paymentDate;
        this.temporaryNote = temporaryNote;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCardLast4() { return cardLast4; }
    public void setCardLast4(String cardLast4) { this.cardLast4 = cardLast4; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getTemporaryNote() { return temporaryNote; }
    public void setTemporaryNote(String temporaryNote) { this.temporaryNote = temporaryNote; }
}
