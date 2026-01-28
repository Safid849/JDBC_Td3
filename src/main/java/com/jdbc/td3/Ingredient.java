package com.jdbc.td3;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ingredient {
    private int id;
    private String name;
    private CategoryEnum category;
    private Double price;
    private List<StockMovement> stockMovementList = new ArrayList<>();


    public Ingredient() {
    }

    public Ingredient(Integer id) {
        this.id = id;
    }

    public Ingredient(Integer id, String name, CategoryEnum category, Double price, List<StockMovement> stockMovementList) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockMovementList = stockMovementList;
    }


    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<StockMovement> getStockMovementList() {
        return stockMovementList;
    }

    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && category == that.category && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", StockIngredient=" + stockMovementList +
                '}';
    }

    public StockValue getStockValueAt(Instant t) {
        double totalQuantity = stockMovementList.stream()
                .filter(mvt -> !mvt.getDate().isAfter(t))
                .mapToDouble(mvt -> {
                    double qty = mvt.getValue().getQuantity();
                    return (mvt.getTypeMovement() == MovementTypeEnum.IN) ? qty : -qty;
                })
                .sum();

        Unit displayUnit = stockMovementList.stream()
                .filter(mvt -> !mvt.getDate().isAfter(t))
                .map(StockMovement::getUnit)
                .reduce((first, second) -> second)
                .orElse(null);

        return new StockValue(totalQuantity, displayUnit);
    }
}

