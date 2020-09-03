package com.example.amexhack;

import java.util.HashMap;
import java.util.HashSet;

public class Testing2 {
//    public static void main(String args[]){
//        System.out.println("Hellow owhooo");
//    }
    public HashMap<String, HashSet<String>> AmexDiscountedSmallBusinesses = new HashMap<>();

    public String Hello(){
        return "Hello";
    }
    public void populateMap(){
        HashSet<String> coffeePlaces = new HashSet<>();
        coffeePlaces.add("Department Of Coffee And Social Affair");
        coffeePlaces.add("Sacred Cafe");
        coffeePlaces.add("Cafe Concerto");

        HashSet<String> spanishPlaces = new HashSet<>();
        spanishPlaces.add("Barrafina");
        spanishPlaces.add("Lobos Soho");

        HashSet<String> indianPlaces = new HashSet<>();
        indianPlaces.add("Kanishka Restaurant");
        indianPlaces.add("The Kati Roll Company");

    }

}
