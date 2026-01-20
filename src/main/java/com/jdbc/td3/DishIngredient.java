package com.jdbc.td3;

import java.util.Objects;

public class DishIngredient {
    private Integer id;
    private Integer idDish;
    private Integer idIngredient;
    private Double quantityRequired;
    private UnitEnum unit;
    public DishIngredient() {}

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdDish() { return idDish; }
    public void setIdDish(Integer idDish) { this.idDish = idDish; }

    public Integer getIdIngredient() { return idIngredient; }
    public void setIdIngredient(Integer idIngredient) { this.idIngredient = idIngredient; }

    public Double getQuantityRequired() { return quantityRequired; }
    public void setQuantityRequired(Double quantityRequired) { this.quantityRequired = quantityRequired; }

    public UnitEnum getUnit() { return unit; }
    public void setUnit(UnitEnum unit) { this.unit = unit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishIngredient that = (DishIngredient) o;
        return Objects.equals(id, that.id) && Objects.equals(idDish, that.idDish) && Objects.equals(idIngredient, that.idIngredient) && Objects.equals(quantityRequired, that.quantityRequired) && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idDish, idIngredient, quantityRequired, unit);
    }

    @Override
    public String toString() {
        return "DishIngredient{" +
                "id=" + id +
                ", idDish=" + idDish +
                ", idIngredient=" + idIngredient +
                ", quantityRequired=" + quantityRequired +
                ", unit=" + unit +
                '}';
    }
}