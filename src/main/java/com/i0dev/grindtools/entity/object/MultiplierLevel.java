package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MultiplierLevel {
    int level;
    int price;
    Double multiplier = null;
    Integer max = null;
    Integer min = null;

    public MultiplierLevel(int level, int price, Double multiplier) {
        this.level = level;
        this.price = price;
        this.multiplier = multiplier;
    }

    public MultiplierLevel(int level, int price, Integer max, Integer min) {
        this.level = level;
        this.price = price;
        this.max = max;
        this.min = min;
    }
}