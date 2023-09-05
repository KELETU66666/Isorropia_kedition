// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.config;

public class OrePigEntry
        extends DuoWeightedRandom.DuoItem {
    private final String oreDictionary;

    public OrePigEntry(String oreDictionary, float weight, float auxiliaryWeight) {
        this(oreDictionary, weight, auxiliaryWeight, true);
    }

    public OrePigEntry(String oreDictionary, float weight, float auxiliary_weight, boolean active) {
        super(weight, auxiliary_weight, active);
        this.oreDictionary = oreDictionary;
    }

    public String getOreDictionary() {
        return this.oreDictionary;
    }
}
