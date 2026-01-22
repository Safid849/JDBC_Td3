package com.jdbc.td3;

import java.util.List;
import java.util.Objects;

public class Dish {
    private Integer id;
    private Double price;
    private String name;
    private DishTypeEnum dishType;
    private List<DishIngredient> dishIngredientsList;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDishCost() {
        double totalPrice = 0;
        if (dishIngredientsList != null) {
            for (DishIngredient dishIngredient : dishIngredientsList) {
                Ingredient newIngredient = dishIngredient.getIngredient();
                totalPrice += newIngredient.getPrice() * dishIngredient.getQuantityRequired();
            }
        }
        return totalPrice;
    }

    public Dish() {
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, List<DishIngredient> dishIngredientsList) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishIngredientsList = dishIngredientsList;
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
        return dishIngredientsList;
    }

    public void setDishIngredient(List<DishIngredient> dishIngredientsList) {
        if (dishIngredientsList == null) {
            this.dishIngredientsList = null;
            return;
        }
        this.dishIngredientsList = dishIngredientsList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(id, dish.id) && Objects.equals(name, dish.name) && dishType == dish.dishType && Objects.equals(dishIngredientsList, dish.dishIngredientsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishType, dishIngredientsList);
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", dishIngredient=" + dishIngredientsList+
                '}';
    }

    public Double getGrossMargin() {
        if (price == null) {
            throw new RuntimeException("Price is null");
        }
        return price - getDishCost();
    }
}
