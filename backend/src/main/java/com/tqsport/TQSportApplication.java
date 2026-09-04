package com.tqsport;

public class TQSportApplication {
    public static void main(String[] args) {
        System.out.println("TQSport backend is now a microservice project.");
        System.out.println("Do not run backend/src/main/java/com/tqsport/TQSportApplication.java.");
        System.out.println("Run from D:\\Java\\TQSport\\backend instead:");
        System.out.println("  mvn clean package -DskipTests");
        System.out.println("  powershell -ExecutionPolicy Bypass -File .\\scripts\\start-all.ps1");
    }
}
