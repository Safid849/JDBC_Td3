package com.jdbc.td3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private double getCurrentStock(Connection conn, int ingredientId) throws SQLException {
        String sqlInitial = "SELECT initial_stock FROM ingredient WHERE id = ?";
        double initialStock = 0;
        try (PreparedStatement ps = conn.prepareStatement(sqlInitial)) {
            ps.setInt(1, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) initialStock = rs.getDouble("initial_stock");
            }
        }
        String sqlMvt = "SELECT SUM(quantity) FROM stock_movement WHERE id_ingredient = ?";
        double movementSum = 0;
        try (PreparedStatement ps = conn.prepareStatement(sqlMvt)) {
            ps.setInt(1, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) movementSum = rs.getDouble(1);
            }
        }
        return initialStock + movementSum;
    }
    public Order saveOrder(Order orderToSave) {
        String sqlOrder = "INSERT INTO \"order\" (reference, creation_datetime) VALUES (?, ?) RETURNING id";
        String sqlDishOrder = "INSERT INTO dish_order (id_order, id_dish, quantity) VALUES (?, ?, ?)";

        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);
            try {
                for (DishOrder item : orderToSave.getDishOrderList()) {
                    Dish dish = item.getDish();
                    for (DishIngredient di : dish.getDishIngredients()) {
                        double totalRequired = di.getQuantityRequired() * item.getQuantity();
                        double stockDisponible = getCurrentStock(conn, di.getIngredient().getId());

                        if (stockDisponible < totalRequired) {
                            throw new RuntimeException("L'ingrédient " + di.getIngredient().getName() + " ne suffit pas");
                        }
                    }
                }

                // --- B) SAUVEGARDE DE LA COMMANDE ---
                int orderId;
                try (PreparedStatement ps = conn.prepareStatement(sqlOrder)) {
                    ps.setString(1, orderToSave.getReference());
                    ps.setTimestamp(2, Timestamp.from(orderToSave.getCreationDatetime()));
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        orderId = rs.getInt(1);
                        orderToSave.setId(orderId);
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(sqlDishOrder)) {
                    for (DishOrder item : orderToSave.getDishOrderList()) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, item.getDish().getId());
                        ps.setInt(3, item.getQuantity());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                conn.commit();
                return orderToSave;
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish findDishById(Integer id) {
        String sql = "SELECT id, name, dish_type, selling_price FROM dish WHERE id = ?";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Dish dish = new Dish();
                    dish.setId(rs.getInt("id"));
                    dish.setName(rs.getString("name"));
                    dish.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
                    dish.setPrice(rs.getObject("selling_price") == null ? null : rs.getDouble("selling_price"));
                    dish.setDishIngredient(findDishIngredients(id));
                    return dish;
                }
            }
            throw new RuntimeException("Plat non trouvé id: " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DishIngredient> findDishIngredients(Integer idDish) {
        List<DishIngredient> list = new ArrayList<>();
        String sql = """
            
                SELECT di.quantity_required, di.unit, i.id, i.name, i.price, i.category
            FROM DishIngredient di
            JOIN ingredient i ON di.id_ingredient = i.id
            WHERE di.id_dish = ?
            """;
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDish);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setPrice(rs.getDouble("price"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ing.setStockMovementList(findStockMovementsByIngredientId(ing.getId()));

                    DishIngredient di = new DishIngredient();
                    di.setIngredient(ing);
                    di.setQuantityRequired(rs.getDouble("quantity_required"));
                    di.setUnit(Unit.valueOf(rs.getString("unit")));
                    list.add(di);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }



    public Dish saveDish(Dish toSave) {
        String sql =
                """
            INSERT INTO dish (id, name, dish_type, selling_price) VALUES (?, ?, ?::
                dish_type, ?)
            ON CONFLICT (id) DO UPDATE SET name=EXCLUDED.name, dish_type=EXCLUDED.dish_type,
                selling_price=EXCLUDED.
                selling_price
            RETURNING id
            """;
        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);
            try {
                int dishId;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    if (toSave.getId() != null) ps.setInt(1, toSave.getId());
                    else ps.setInt(1, getNextSerialValue(conn, "dish", "id"));

                    ps.setString(2, toSave.getName());
                    ps.setString(3, toSave.getDishType().name());
                    if (toSave.getPrice() != null) ps.setDouble(4, toSave.getPrice());
                    else ps.setNull(4, Types.NUMERIC);

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        dishId = rs.getInt(1);
                    }
                }
                updateDishIngredients(conn, dishId, toSave.getDishIngredients());

                conn.commit();
                return findDishById(dishId);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateDishIngredients(Connection conn, int dishId, List<DishIngredient> list) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM DishIngredient WHERE id_dish = ?")) {
            ps.setInt(1, dishId);
            ps.executeUpdate();
        }
        if (list != null && !list.isEmpty()) {
            String sql = "INSERT INTO DishIngredient (id_dish, id_ingredient, quantity_required, unit) VALUES (?, ?, ?, ?::unit_type)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (DishIngredient di : list) {
                    ps.setInt(1, dishId);
                    ps.setInt(2, di.getIngredient().getId());
                    ps.setDouble(3, di.getQuantityRequired());
                    ps.setString(4, di.getUnit().name());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private int getNextSerialValue(Connection conn, String table, String col) throws SQLException {
        String sql = String.format("SELECT nextval(pg_get_serial_sequence('%s', '%s'))", table, col);
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next(); return rs.getInt(1);
        }
    }
    public List<StockMovement> findStockMovementsByIngredientId(int ingredientId) {
        List<StockMovement> movements = new ArrayList<>();
        String sql = "SELECT id, quantity, type, unit, creation_datetime FROM stock_movement WHERE id_ingredient = ?";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    StockMovement mvt = new StockMovement();
                    mvt.setId(rs.getInt("id"));
                    Unit unit = Unit.valueOf(rs.getString("unit"));
                    double qty = rs.getDouble("quantity");
                    mvt.setValue(new StockValue(qty, unit));
                    mvt.setUnit(unit);

                    mvt.setTypeMovement(MovementTypeEnum.valueOf(rs.getString("type")));
                    mvt.setDate(rs.getTimestamp("creation_datetime").toInstant());
                    movements.add(mvt);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération  des stocks", e);
        }
        return movements;
    }
    public Ingredient saveIngredient(Ingredient toSave) {
        String sqlIng = """
        INSERT INTO ingredient (id, name, price, category)
        VALUES (?, ?, ?, ?::ingredient_category)
        ON CONFLICT (id) DO UPDATE SET name=EXCLUDED.name, price=EXCLUDED.price, category=EXCLUDED.category
        """;

        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlIng)) {
                ps.setInt(1, toSave.getId());
                ps.setString(2, toSave.getName());
                ps.setDouble(3, toSave.getPrice());
                ps.setString(4, toSave.getCategory().name());
                ps.executeUpdate();
            }

            String sqlMvt = """
            INSERT INTO stock_movement (id, id_ingredient, quantity, type, unit, creation_datetime)
            VALUES (?, ?, ?, ?::mouvement_type, ?::unit_type, ?)
            ON CONFLICT (id) DO NOTHING
            """;

            try (PreparedStatement psMvt = conn.prepareStatement(sqlMvt)) {
                for (StockMovement mvt : toSave.getStockMovementList()) {
                    psMvt.setInt(1, mvt.getId());
                    psMvt.setInt(2, toSave.getId());
                    psMvt.setDouble(3, mvt.getValue().getQuantity());
                    psMvt.setString(4, mvt.getTypeMovement().name());

                    String unitName = (mvt.getUnit() != null) ? mvt.getUnit().name() : mvt.getValue().getUnit().name();
                    psMvt.setString(5, unitName);

                    psMvt.setTimestamp(6, java.sql.Timestamp.from(mvt.getDate()));
                    psMvt.addBatch();
                }
                psMvt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur sauvegarde cascade : " + e.getMessage());
        }
        return toSave;
    }
    Order findOrderByReference(String reference) {
        DBConnection dbConnection = new DBConnection();
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    select id, reference, creation_datetime from "order" where reference like ?""");
            preparedStatement.setString(1, reference);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order order = new Order();
                Integer idOrder = resultSet.getInt("id");
                order.setId(idOrder);
                order.setReference(resultSet.getString("reference"));
                order.setCreationDatetime(resultSet.getTimestamp("creation_datetime").toInstant());
                order.setDishOrderList(findDishOrderByIdOrder(idOrder));
                return order;
            }
            throw new RuntimeException("Order not found with reference " + reference);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private List<DishOrder> findDishOrderByIdOrder(Integer idOrder) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
        List<DishOrder> dishOrders = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            select id, id_dish, quantity from dish_order where dish_order.id_order = ?
                            """);
            preparedStatement.setInt(1, idOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Dish dish = findDishById(resultSet.getInt("id_dish"));
                DishOrder dishOrder = new DishOrder();
                dishOrder.setId(resultSet.getInt("id"));
                dishOrder.setQuantity(resultSet.getInt("quantity"));
                dishOrder.setDish(dish);
                dishOrders.add(dishOrder);
            }
            dbConnection.closeConnection(connection);
            return dishOrders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}