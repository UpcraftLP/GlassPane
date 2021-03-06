package com.github.upcraftlp.glasspane.config;

import com.github.upcraftlp.glasspane.GlassPane;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config.LangKey("config.glasspane.general.title")
@Config(modid = GlassPane.MODID, name = "glasspanemods/GlassPane") //--> /config/glasspanemods/GlassPane.cfg
public class Lens {

    @Config.Name("Client Settings")
    @Config.Comment("These settings affect the client ONLY")
    public static final Client client = new Client();

    @Config.Name("Updater Settings")
    public static final Update updater = new Update();

    @Config.Name("Debug Mode")
    @Config.Comment({"En/Disable global debug mode", "this is a global flag for all glasspane mods!"})
    public static boolean debugMode = false;

    @Config.Name("Colored Chat Messages")
    @Config.Comment("If enabled, will convert \u0026, followed by a formatting code to the respective formatting code in chat messages. \u0026\u0026 to escape.")
    public static boolean colorChat = true;

    @Config.Name("Color OP Player Names")
    @Config.Comment({"If enabled, will tint the name of opped players red in chat messages", "will have no effect if chat color formatting is disabled"})
    public static boolean colorOps = false;

    public static class Client {

        //@Config.Name("Gui Background Type")
        //@Config.Comment("The background that should be used for GUIs that support dynamic backgrounds")
        //public EnumGuiBackgroundType guiBackgroundType = EnumGuiBackgroundType.VANILLA;

        @Config.Name("Advanced Tooltips")
        @Config.Comment("Show advanced tooltip information?")
        public boolean showAdvancedTooltipInfo = true;
    }

    public static class Update {

        @Config.Name("Show Available Updates")
        @Config.Comment("whether or not to show a notification about updates")
        public boolean showUpdates = true;

        @Config.Name("Show Experimental Builds")
        @Config.Comment("whether or not to show experimental builds that might be unstable")
        public boolean showBetaUpdates = true;
    }

    @Mod.EventBusSubscriber(modid = GlassPane.MODID)
    public static class Handler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent event) {
            if(GlassPane.MODID.equals(event.getModID())) ConfigManager.sync(event.getModID(), Config.Type.INSTANCE);
        }
    }
}
