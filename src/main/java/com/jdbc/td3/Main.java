package com.jdbc.td3;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();

        System.out.println("=== DÉBUT DES TESTS JDBC (PostgreSQL) ===\n");

        // 1. Test de findDishById
        testFindDish(retriever, 1); // Salade fraîche

        // 2. Test de getDishCost (Question 4)
        testGetCost(retriever, 1);

        // 3. Test de getGrossMargin (Question 5 - Cas avec prix)
        testMargin(retriever, 1);

        // 4. Test de getGrossMargin (Question 5 - Cas avec prix NULL)
        testMargin(retriever, 3); // Riz aux légumes (Prix est NULL en base)

        System.out.println("\n=== FIN DES TESTS ===");
    }

    private static void testFindDish(DataRetriever retriever, int id) {
        try {
            Dish dish = retriever.findDishById(id);
            System.out.println("[FIND] Plat trouvé : " + dish.getName() + " (" + dish.getDishType() + ")");
            System.out.println("       Nombre d'ingrédients : " + dish.getIngredients().size());
        } catch (Exception e) {
            System.err.println("[FIND] Erreur : " + e.getMessage());
        }
    }

    private static void testGetCost(DataRetriever retriever, int id) {
        try {
            Double cost = retriever.getDishCost(id);
            System.out.println("[COST] Coût de revient pour le plat " + id + " : " + cost + " Ar");
        } catch (Exception e) {
            System.err.println("[COST] Erreur : " + e.getMessage());
        }
    }

    private static void testMargin(DataRetriever retriever, int id) {
        try {
            Double margin = retriever.getGrossMargin(id);
            System.out.println("[MARGIN] Marge brute pour le plat " + id + " : " + margin + " Ar");
        } catch (Exception e) {
            // C'est ici que l'exception pour le prix NULL sera rattrapée
            System.out.println("[MARGIN] Résultat attendu pour le plat " + id + " : " + e.getMessage());
        }
    }
}