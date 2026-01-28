package com.jdbc.td3;

import java.util.List;
import java.util.Objects;

public class Dish {
    private Integer id;
    private Double price;
    private String name;
    private DishTypeEnum dishType;
    private List<DishIngredient> dishIngredientList;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public double getDishCost() {
        return dishIngredientList.stream()
                .filter(di -> di.getIngredient() != null)
                .mapToDouble(di -> {
                    double unitPrice = di.getIngredient().getPrice();
                    double quantity = di.getQuantityRequired();
                    return unitPrice * quantity;
                })
                .sum();
    }

    public Dish() {
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, List<DishIngredient> dishIngredientList) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishIngredientList = dishIngredientList;
    }


    public Integer getId() {
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

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<DishIngredient> getDishIngredients() {
        return dishIngredientList;
    }

    public void setDishIngredient(List<DishIngredient> dishIngredientsList) {
        if (dishIngredientsList == null) {
            this.dishIngredientList = null;
            return;
        }
        this.dishIngredientList = dishIngredientsList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(dishIngredientList, dish.dishIngredientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, dishIngredientList);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", dishIngredient=" + dishIngredientList+
                '}';
    }

    public Double getGrossMargin() {
        if (price == null) {
            throw new RuntimeException("Price is null");
        }
        return price - getDishCost();
    }
}
