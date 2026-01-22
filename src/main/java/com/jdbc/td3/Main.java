package com.jdbc.td3;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();

        System.out.println("--- DÉBUT DES TESTS (TD3 & TD4) ---");

        System.out.println("\n[Test 1] Calcul du coût de revient :");
        try {
            Dish salade = retriever.findDishById(1); // Salade fraîche
            System.out.println("Plat récupéré : " + salade.getName());

            double cost = salade.getDishCost();
            System.out.println("Coût de revient : " + cost + " Ar");
            System.out.println("Valeur attendue (Sujet TD3) : 250.00 Ar");
        } catch (Exception e) {
            System.err.println("Erreur Test 1 : " + e.getMessage());
        }

        System.out.println("\n[Test 2] Calcul de la marge :");
        try {
            Dish fruit = retriever.findDishById(5); // Salade de fruits (Prix NULL dans le sujet)
            System.out.println("Plat : " + fruit.getName() + " | Prix : " + fruit.getPrice());
            System.out.println("Marge : " + fruit.getGrossMargin() + " Ar");
        } catch (Exception e) {
            System.out.println("Exception attendue reçue : " + e.getMessage());
        }

        System.out.println("\n[Test 3] Niveau de stock (Laitue) :");
        try {
            Dish salade = retriever.findDishById(1);
            Ingredient laitue = salade.getDishIngredients().get(0).getIngredient();

            Instant avantMouvement = Instant.parse("2024-01-02T10:00:00Z");
            Instant apresMouvement = Instant.parse("2024-01-07T10:00:00Z");

            System.out.println("Stock de Laitue avant le 06/01 : " + laitue.getStockValueAt(avantMouvement) + " KG");
            System.out.println("Stock de Laitue après le 06/01 : " + laitue.getStockValueAt(apresMouvement) + " KG");
            System.out.println("Valeur attendue finale : 4.8 KG (5.0 - 0.2)");
        } catch (Exception e) {
            System.err.println("Erreur Test 3 : " + e.getMessage());
        }

        System.out.println("\n[Test 4] Sauvegarde d'un nouveau plat :");
        try {
            Dish nouveauPlat = new Dish();
            nouveauPlat.setId(10); // Nouvel ID
            nouveauPlat.setName("Café Gourmand");
            nouveauPlat.setDishType(DishTypeEnum.DESSERT);
            nouveauPlat.setPrice(5000.0);

            Dish saved = retriever.saveDish(nouveauPlat);
            System.out.println("Plat sauvegardé avec succès : " + saved.getName());
        } catch (Exception e) {
            System.err.println("Erreur Test 4 : " + e.getMessage());
        }

        System.out.println("\n--- FIN DES TESTS ---");
    }
}