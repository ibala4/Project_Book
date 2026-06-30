package com.example.dmart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private double price;

    @NotBlank(message = "Unit is required")
    private String unit; // e.g. "1 kg", "500 ml", "1 pack"

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

    // A short emoji or letter used as a lightweight visual placeholder for
    // the product, since this is a demo app without real product photos.
    private String icon = "🛒";

    public Product() {
    }

    public Product(String name, String category, double price, String unit, int stock, String icon) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.stock = stock;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
