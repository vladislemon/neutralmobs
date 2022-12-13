package ru.vladislemon.neutralmobs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod(NeutralMobs.MOD_ID)
public class NeutralMobs {
    public static final String MOD_ID = "neutralmobs";
    private static NeutralMobs INSTANCE;

    private final Config config;

    public NeutralMobs() {
        INSTANCE = this;
        config = createConfig();
        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (incoming, isNetwork) -> true)
        );
        MinecraftForge.EVENT_BUS.register(this);
    }

    private Config createConfig() {
        Pair<Config, ForgeConfigSpec> configSpecPair = new ForgeConfigSpec.Builder().configure(Config::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configSpecPair.getValue());
        return configSpecPair.getKey();
    }

    public Config getConfig() {
        return config;
    }

    public static NeutralMobs getInstance() {
        return INSTANCE;
    }
}
