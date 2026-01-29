package com.jdbc.td3;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private List<DishOrder> dishOrderList = new ArrayList<>();
    private RestaurantTable table;
    private Instant installationDatetime;
    private Instant departureDatetime;

    public Double getTotalAmountWithoutVAT() {
        return dishOrderList.stream()
                .mapToDouble(doItem -> doItem.getDish().getPrice() * doItem.getQuantity())
                .sum();
    }

    public Double getTotalAmountWithVAT() {
        return getTotalAmountWithoutVAT() * 1.20;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }

    public List<DishOrder> getDishOrderList() { return dishOrderList; }
    public void setDishOrderList(List<DishOrder> list) { this.dishOrderList = list; }

    public RestaurantTable getTable() { return table; }
    public void setTable(RestaurantTable table) { this.table = table; }

    public Instant getInstallationDatetime() { return installationDatetime; }
    public void setInstallationDatetime(Instant installationDatetime) { this.installationDatetime = installationDatetime; }

    public Instant getDepartureDatetime() { return departureDatetime; }
    public void setDepartureDatetime(Instant departureDatetime) { this.departureDatetime = departureDatetime; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(reference, order.reference) && Objects.equals(creationDatetime, order.creationDatetime) && Objects.equals(dishOrderList, order.dishOrderList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, creationDatetime, dishOrderList);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", dishOrderList=" + dishOrderList +
                '}';
    }
}
