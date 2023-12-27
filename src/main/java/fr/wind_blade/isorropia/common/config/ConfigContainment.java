package fr.wind_blade.isorropia.common.config;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;


public class ConfigContainment
{
    public static Enum TYPE = Enum.BLACKLIST;
    public static List<Class<? extends Entity>> ENTRIES = new ArrayList<>();

    public static void init() {}

    public enum Enum
    {
        WHITELIST("whitelist"), BLACKLIST("blacklist");

        private final String name;

        Enum(String name) {
            this.name = name;
        }

        public static Enum getByName(String name) {
            for (Enum type : values()) {
                if (type.name.equals(name))
                    return type;
            }
            return null;
        }

        public String getName() {
            return this.name;
        }
    }
}