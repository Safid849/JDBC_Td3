package com.jdbc.td3;

import java.util.Objects;

public class RestaurantTable {
    private Integer id;
    private Integer tableNumber;

    public RestaurantTable() {}

    public RestaurantTable(Integer id, Integer tableNumber) {
        this.id = id;
        this.tableNumber = tableNumber;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTableNumber() { return tableNumber; }
    public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTable that = (RestaurantTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", tableNumber=" + tableNumber +
                '}';
    }
}
