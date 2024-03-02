package com.example;
import java.util.LinkedList;

public class Chef {

    private List<Cook> cooks;

    public List<Food> prepareDinner() {
        List<Food> dinner = new LinkedList<>();

        for (Cook cook : cooks) {
            Food food = cook.cook();

            dinner.add(food);
        }

        return dinner;
    }
}
