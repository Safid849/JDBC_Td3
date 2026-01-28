package com.jdbc.td3;

import java.time.Instant;

public class StockMovement {
    private int id;
    private StockValue value;
    private Unit unit;
    private MovementTypeEnum movementTypeEnum;
    private Instant creationDatetime;

    public StockMovement(){}

    public StockMovement(int id, Unit unit, MovementTypeEnum movementTypeEnum, Instant creationDatetime) {
        this.id = id;
        this.unit = unit;
        this.movementTypeEnum = movementTypeEnum;
        this.creationDatetime = creationDatetime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public StockValue getValue() { return value; }
    public void setValue(StockValue value) { this.value = value; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }

    public MovementTypeEnum getTypeMovement() { return movementTypeEnum; }
    public void setTypeMovement(MovementTypeEnum movementTypeEnum) { this.movementTypeEnum = movementTypeEnum; }

    public Instant getDate() { return creationDatetime; }
    public void setDate(Instant creationDatetime) { this.creationDatetime = creationDatetime; }

    @Override
    public String toString() {
        return "StockIngredient{" +
                "id=" + id +
                ", unit=" + unit +
                ", tYpeMouvement=" + movementTypeEnum +
                ", creationDatetime=" + creationDatetime +
                '}';
    }
}