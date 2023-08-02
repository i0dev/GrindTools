package com.i0dev.grindtools.entity.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class MultiplierLevelPerTool {
    Tools tool;

    Map<Integer, Double> levels;
}