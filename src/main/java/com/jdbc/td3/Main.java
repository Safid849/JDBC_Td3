package com.jdbc.td3;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();

        try {
            System.out.println("=== TEST DE DISPONIBILITÉ DES TABLES ===");


            Order conflictOrder = new Order();
            conflictOrder.setReference("TEST-CONFLIT");
            conflictOrder.setTable(new RestaurantTable(1, 1));


            Instant heureConflit = Instant.parse("2026-01-29T10:30:00Z");
            conflictOrder.setInstallationDatetime(heureConflit);
            conflictOrder.setCreationDatetime(Instant.now());
            conflictOrder.setDishOrderList(new ArrayList<>()); // Liste vide pour le test

            System.out.println("Tentative sur Table 1 à 10:30 (Occupée jusqu'à 10:46)...");

            try {
                retriever.saveOrder(conflictOrder);
            } catch (RuntimeException e) {
                System.out.println("\nMESSAGE REÇU : " + e.getMessage());
            }

            System.out.println("\nTentative sur Table 4 (Libre)...");
            Order successOrder = new Order();
            successOrder.setReference("ORD-LIBRE-04");
            successOrder.setTable(new RestaurantTable(4, 4));
            successOrder.setInstallationDatetime(heureConflit);
            successOrder.setCreationDatetime(Instant.now());
            successOrder.setDishOrderList(new ArrayList<>());

            retriever.saveOrder(successOrder);
            System.out.println("✅ Succès : La table 4 a été acceptée.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}