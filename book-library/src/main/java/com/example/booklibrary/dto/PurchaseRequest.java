package com.example.booklibrary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PurchaseRequest {

    @NotBlank(message = "Buyer name is required")
    private String buyerName;

    @NotBlank(message = "Buyer email is required")
    @Email(message = "Enter a valid email address")
    private String buyerEmail;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity = 1;

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
