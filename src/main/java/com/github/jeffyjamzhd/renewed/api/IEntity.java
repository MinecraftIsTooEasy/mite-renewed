package com.github.jeffyjamzhd.renewed.api;

public interface IEntity {
    /**
     * Returns if this entity is on fire
     * @return Fire state
     */
    default boolean mr$onFire() {
        return false;
    }
}
