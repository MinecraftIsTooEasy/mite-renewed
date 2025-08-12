package com.github.jeffyjamzhd.renewed.api;

public interface IMaterial {
    /**
     * Gets the name of the material
     * @return Name of the material
     */
    default String mr$getName() {
        return "";
    }
}
