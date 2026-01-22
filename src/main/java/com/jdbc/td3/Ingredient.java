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
    private List<StockIngredient> stockIngredientList = new ArrayList<>();


    public Ingredient() {
    }

    public Ingredient(Integer id) {
        this.id = id;
    }

    public Ingredient(Integer id, String name, CategoryEnum category, Double price, List<StockIngredient> stockMouvementList) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockIngredientList = stockIngredientList;
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
    public List<StockIngredient> getStockMouvementList() {
        return stockIngredientList;
    }
    public void setStockIngredientList(List<StockIngredient> stockIngredientList) {
        this.stockIngredientList = stockIngredientList;
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
                ", StockIngredient=" + stockIngredientList +
                '}';
    }
    public double getStockValueAt(Instant t) {
        double totalStock = 0.0;

        for (StockIngredient mvt : stockIngredientList) {
            if (!mvt.getDate().isAfter(t)) {
                if (mvt.getTypeMouvement() == TypeMouvement.IN) {
                    totalStock += mvt.getQuantity();
                } else if (mvt.getTypeMouvement() == TypeMouvement.OUT) {
                    totalStock -= mvt.getQuantity();
                }
            }
        }
        return totalStock;
    }
}

