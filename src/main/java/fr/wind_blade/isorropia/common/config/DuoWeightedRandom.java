// Decompiled with: CFR 0.152
// Class Version: 8
package fr.wind_blade.isorropia.common.config;

import io.netty.util.internal.ThreadLocalRandom;
import java.util.List;
import java.util.Random;

public class DuoWeightedRandom {
  public static <T extends DuoItem> double getTotalWeight(List<T> collection) {
    return collection.stream().filter(item -> item.isActive()).mapToDouble(DuoItem::getWeight).sum();
  }

  public static <T extends DuoItem> double getTotalWeightWithMain(List<T> collection, T main_item) {
    return collection.stream().filter(item -> item.isActive() && item != main_item).mapToDouble(DuoItem::getAuxiliaryWeight).sum() + main_item.getWeight();
  }

  public static <T extends DuoItem> T getRandomItem(Random random, List<T> collection, double totalWeight) {
    if (totalWeight <= 0.0) {
      throw new IllegalArgumentException();
    }
    double i = ThreadLocalRandom.current().nextDouble(0.0, totalWeight);
    return DuoWeightedRandom.getRandomItem(collection, i);
  }

  public static <T extends DuoItem> T getRandomItemWithMain(Random random, List<T> collection, T main_item, double totalWeight) {
    if (totalWeight <= 0.0) {
      throw new IllegalArgumentException();
    }
    double i = ThreadLocalRandom.current().nextDouble(0.0, totalWeight);
    return DuoWeightedRandom.getRandomItemWithMain(collection, main_item, i);
  }

  public static <T extends DuoItem> T getRandomItem(List<T> collection, double weight) {
    int j = collection.size();
    for (int i = 0; i < j; ++i) {
      DuoItem t = (DuoItem)collection.get(i);
      if (!t.isActive() || !((weight -= t.weight) < 0.0)) continue;
      return (T)t;
    }
    return null;
  }

  public static <T extends DuoItem> T getRandomItemWithMain(List<T> collection, T main_item, double weight) {
    int j = collection.size();
    for (int i = 0; i < j; ++i) {
      DuoItem t = (DuoItem)collection.get(i);
      if (!t.isActive() || !((weight -= t == main_item ? t.weight : t.getAuxiliaryWeight()) < 0.0)) continue;
      return (T)t;
    }
    return null;
  }

  public static <T extends DuoItem> T getRandomItem(Random random, List<T> collection) {
    return DuoWeightedRandom.getRandomItem(random, collection, DuoWeightedRandom.getTotalWeight(collection));
  }

  public static <T extends DuoItem> T getRandomItemWithMain(Random random, T main_item, List<T> collection) {
    return DuoWeightedRandom.getRandomItemWithMain(random, collection, main_item, DuoWeightedRandom.getTotalWeightWithMain(collection, main_item));
  }

  public static class DuoItem {
    public final double weight;
    public final double auxiliary_weight;
    private boolean active = true;

    public DuoItem(double weight) {
      this(weight, 0.0, true);
    }

    public DuoItem(double weight, boolean active) {
      this(weight, 0.0, active);
    }

    public DuoItem(double weight, double auxiliary_weight) {
      this(weight, auxiliary_weight, true);
    }

    public DuoItem(double weight, double auxiliary_weight, boolean active) {
      this.weight = weight;
      this.auxiliary_weight = auxiliary_weight;
      this.active = active;
    }

    public double getWeight() {
      return this.weight;
    }

    public double getAuxiliaryWeight() {
      return this.auxiliary_weight;
    }

    public boolean isActive() {
      return this.active;
    }

    public void setActive(boolean active) {
      this.active = active;
    }
  }
}
