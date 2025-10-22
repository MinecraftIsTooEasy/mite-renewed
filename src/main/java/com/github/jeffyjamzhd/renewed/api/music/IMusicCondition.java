package com.github.jeffyjamzhd.renewed.api.music;

import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public interface IMusicCondition {
    /**
     * Main method, returns {@code true} if the condition passes
     */
    boolean check(@Nullable World world, @Nullable EntityPlayer player);

    /**
     * Checker method ran upon resource reload, validates condition metadata
     */
    default void validate() throws Exception {
    }

    /**
     * Returns identifier of this music condition
     */
    String getIdentifier();

    /**
     * If {@code false}, this condition will always pass. Used for grouping tracks to play under
     * certain circumstances, but not limiting them to those scenarios. e.g. Title-screen tracks
     */
    default boolean isMandatory() {
        return true;
    }

    /**
     * Returns the priority of this condition. Higher values will give this condition
     * more weight over others
     */
    default int getPriority() {
        return 1;
    }
}
