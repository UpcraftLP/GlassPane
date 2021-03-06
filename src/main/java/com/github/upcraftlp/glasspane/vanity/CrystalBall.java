package com.github.upcraftlp.glasspane.vanity;

import com.github.upcraftlp.glasspane.GlassPane;
import com.github.upcraftlp.glasspane.api.util.ForgeUtils;
import com.github.upcraftlp.glasspane.util.JsonUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = GlassPane.MODID)
public class CrystalBall {

    private static final String VANITY_URL = "https://gist.githubusercontent.com/UpcraftLP/f2fd5c5e783e3fc23b85c5184fcd4488/raw/vanity_features.json";

    private static final Map<UUID, VanityPlayerInfo> VANITY_PLAYER_INFO = new HashMap<>();

    public static boolean canUseFeature(UUID uuid, String feature) {
        VanityPlayerInfo info = VANITY_PLAYER_INFO.getOrDefault(uuid, null);
        return info != null && info.hasFeature(feature);
    }

    public static boolean canUseFeature(UUID uuid, ResourceLocation feature) {
        VanityPlayerInfo info = VANITY_PLAYER_INFO.getOrDefault(uuid, null);
        return info != null && info.hasFeature(feature);
    }

    public static boolean hasVanityFeatures(EntityPlayer player) {
        return VANITY_PLAYER_INFO.containsKey(player.getUniqueID());
    }

    public static boolean hasVanityFeatures(UUID playerID) {
        return VANITY_PLAYER_INFO.containsKey(playerID);
    }

    /**
     * internal use ONLY!
     */
    public static void updatePlayerInfo() {
        Thread t = new Thread(() -> {
            try {
                File f = new File(ForgeUtils.MOD_RESOURCES, "vanity_overrides.json");
                String json;
                if(f.exists() && !f.isDirectory()) {
                    GlassPane.getDebugLogger().info("vanity file override detected, skipping web request and loading from file!");
                    json = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
                } else {
                    GlassPane.getLogger().info("fetching information from GitHub...");
                    json = IOUtils.toString(new URL(VANITY_URL), StandardCharsets.UTF_8);
                }
                VanityPlayerInfo[] playerInfo = JsonUtil.GSON.fromJson(json, VanityPlayerInfo[].class);
                synchronized(VANITY_PLAYER_INFO) {
                    VANITY_PLAYER_INFO.clear();
                    VANITY_PLAYER_INFO.putAll(Arrays.stream(playerInfo).collect(Collectors.toMap(VanityPlayerInfo::getUniqueID, info -> info)));
                }
                GlassPane.getLogger().info("successfully loaded data!");
            } catch(IOException e) {
                GlassPane.getLogger().error("unable to update vanity information!", e);
            }
        });
        t.setName(GlassPane.MODNAME + "/Specials");
        t.start();
    }

    private static final Map<UUID, List<ResourceLocation>> PLAYER_SETTINGS = new HashMap<>();

    public static void setPlayerSettings(UUID id, List<String> settings) {
        PLAYER_SETTINGS.put(id, settings.stream().map(s -> new ResourceLocation(s)).filter(feature -> canUseFeature(id, feature)).collect(Collectors.toList()));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Nullable
    public static ResourceLocation getSelectedFeature(String id, EntityPlayer player) {
        if(PLAYER_SETTINGS.containsKey(player.getUniqueID())) {
            return PLAYER_SETTINGS.get(player.getUniqueID()).stream().filter(rl -> rl.getNamespace().equals(id)).findFirst().get();
        }
        return null;
    }
}
