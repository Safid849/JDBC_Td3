package com.jdbc.td3;

import java.time.Instant;

public class StockIngredient {
    private int id;
    private double quantity;
    private UnitEnum unit;
    private TypeMouvement typeMouvement;
    private Instant date;

    public StockIngredient(){}

    public StockIngredient(int id, double quantity, UnitEnum unit, TypeMouvement tYpeMouvement, Instant date) {
        this.id = id;
        this.quantity = quantity;
        this.unit = unit;
        this.typeMouvement = tYpeMouvement;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public UnitEnum getUnit() { return unit; }
    public void setUnit(UnitEnum unit) { this.unit = unit; }

    public TypeMouvement getTypeMouvement() { return typeMouvement; }
    public void setTypeMouvement(TypeMouvement tYpeMouvement) { this.typeMouvement = tYpeMouvement; }

    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }

    @Override
    public String toString() {
        return "StockIngredient{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", unit=" + unit +
                ", tYpeMouvement=" + typeMouvement +
                ", date=" + date +
                '}';
    }
}