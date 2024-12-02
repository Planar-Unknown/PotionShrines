package com.dreu.potionshrines.blocks.crate;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Rarities implements StringRepresentable {
    COMMON("common"),
    UNCOMMON("uncommon"),
    RARE("rare"),
    EPIC("epic"),
    LEGENDARY("legendary");

    private final String name;

    public static Rarities fromString(String name){
        return switch (name) {
            case "uncommon" -> UNCOMMON;
            case "rare" -> RARE;
            case "epic" -> EPIC;
            case "legendary" -> LEGENDARY;
            default -> COMMON;
        };
    }

    Rarities(String name) {this.name = name;}

    public String toString() {return this.name;}

    @Override public @NotNull String getSerializedName() {return this.name;}
}
