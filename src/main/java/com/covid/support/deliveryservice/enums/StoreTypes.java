package com.covid.support.deliveryservice.enums;

import java.util.ArrayList;
import java.util.List;

public enum StoreTypes {
    MEDICAL_STORE("Medical Store"),GROCERIES("Groceries"),MEAT_AND_POULTRY("Meat and Poultry"),LIQUOR("Liquor"),FISH("Fish");

    String type;
    private StoreTypes(String types){
        this.type = types;
    }

    public static List<String>  getTypes(){
        List<String> types = new ArrayList<>();
        for(StoreTypes storeTypes : StoreTypes.values()){
            types.add(storeTypes.type);
        }
        return types;
    }

    public StoreTypes getType(String type){
        for(StoreTypes storeTypes : StoreTypes.values()){
            if(storeTypes.type.equalsIgnoreCase(type)){
                return storeTypes;
            }
        }
        return null;
    }
}
