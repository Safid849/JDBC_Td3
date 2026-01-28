package com.jdbc.td3;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();

        System.out.println("--- DÉBUT DES TESTS DU SUJET ---");

        // TEST 1 : Récupération d'un plat et de ses ingrédients (TD3)
        System.out.println("\n[Test 1] Chargement du plat ID 1 :");
        Dish salade = retriever.findDishById(1);
        System.out.println("Plat récupéré : " + salade.getName() + " (" + salade.getDishType() + ")");

        System.out.println("Ingrédients nécessaires :");
        salade.getDishIngredients().forEach(di -> {
            System.out.println("- " + di.getIngredient().getName() + " : "
                    + di.getQuantityRequired() + " " + di.getUnit());
        });



        System.out.println("\n[Test 2] Calcul du prix de revient :");
        double coutTotal = salade.getDishCost();
        System.out.println("Coût total de fabrication : " + coutTotal + " Ar");

        if (salade.getPrice() != null) {
            double marge = salade.getPrice() - coutTotal;
            System.out.println("Prix de vente : " + salade.getPrice() + " Ar");
            System.out.println("Marge brute : " + marge + " Ar");
        }

        if (!salade.getDishIngredients().isEmpty()) {
            Ingredient laitue = salade.getDishIngredients().get(0).getIngredient();
            System.out.println("\n[Test 3] État des stocks pour : " + laitue.getName());

            Instant debutJanvier = Instant.parse("2024-01-02T00:00:00Z");
            StockValue stockInitial = laitue.getStockValueAt(debutJanvier);
            System.out.println("Stock au 02/01 : " + stockInitial.getQuantity() + " " + stockInitial.getUnit());

            // Date 2 : Après la sortie de 0.2kg (le 06/01)
            Instant apresSortie = Instant.parse("2024-01-07T00:00:00Z");
            StockValue stockFinal = laitue.getStockValueAt(apresSortie);
            System.out.println("Stock au 07/01 : " + stockFinal.getQuantity() + " " + stockFinal.getUnit());
        }



        System.out.println("\n[Test 4] Test de sauvegarde et non-doublon :");
        Ingredient ingTest = salade.getDishIngredients().get(0).getIngredient();

        StockMovement mvtExistant = new StockMovement();
        mvtExistant.setId(1);
        mvtExistant.setValue(new StockValue(999.0, Unit.KG));
        mvtExistant.setTypeMovement(MovementTypeEnum.IN);
        mvtExistant.setUnit(Unit.KG);
        mvtExistant.setDate(Instant.now());

        ingTest.getStockMovementList().add(mvtExistant);

        System.out.println("Tentative de sauvegarde de l'ingrédient avec un mouvement (ID 1) existant...");
        retriever.saveIngredient(ingTest);
        System.out.println("Sauvegarde effectuée. (Le ON CONFLICT DO NOTHING doit empêcher la modification du mouvement 1)");

        // Vérification finale
        Ingredient verif = retriever.findDishById(1).getDishIngredients().get(0).getIngredient();
        StockValue valVerif = verif.getStockValueAt(Instant.now());
        System.out.println("Vérification stock final : " + valVerif.getQuantity() + " " + valVerif.getUnit());
        System.out.println("(Si le stock n'est pas devenu énorme, le test est réussi !)");

        System.out.println("\n--- FIN DES TESTS ---");
    }
}