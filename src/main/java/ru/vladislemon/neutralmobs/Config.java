package ru.vladislemon.neutralmobs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Config {
    private static final Field MOB_CATEGORY_FIELD = ObfuscationReflectionHelper.findField(EntityType.class, "f_20536_"); // category
    private static final List<String> DEFAULT_NEUTRAL_MOBS = ForgeRegistries.ENTITY_TYPES
            .getValues()
            .stream()
            .filter(entityType -> getMobCategory(entityType) == MobCategory.MONSTER)
            .map(EntityType::getKey)
            .map(ResourceLocation::toString)
            .toList();

    private final ForgeConfigSpec.ConfigValue<List<? extends String>> configSpec;
    private Set<EntityType<?>> neutralMobs;

    public Config(final ForgeConfigSpec.Builder builder) {
        configSpec = builder
                .comment("List of mobs which must be neutral to players")
                .translation("config.neutralmobs.neutral-mobs")
                .defineList("neutral-mobs", DEFAULT_NEUTRAL_MOBS, Config::validateConfigEntries);
    }

    public Set<EntityType<?>> getNeutralMobs() {
        if (neutralMobs == null) {
            neutralMobs = configSpec
                    .get()
                    .stream()
                    .map(String::trim)
                    .map(EntityType::byString)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toUnmodifiableSet());
        }
        return neutralMobs;
    }

    private static boolean validateConfigEntries(Object element) {
        if (!(element instanceof String s)) {
            return false;
        }
        s = s.trim();
        if (s.isEmpty()) {
            return false;
        }
        return EntityType.byString(s).isPresent();
    }

    private static MobCategory getMobCategory(EntityType<?> entityType) {
        try {
            return (MobCategory) MOB_CATEGORY_FIELD.get(entityType);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
