package com.github.upcraftlp.glasspane.util;

import com.github.upcraftlp.glasspane.GlassPane;
import com.github.upcraftlp.glasspane.api.util.serialization.JsonPostProcessor;
import com.github.upcraftlp.glasspane.api.util.serialization.JsonSerializerResourceLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ResourceLocation;

public class JsonUtil {

    public static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        if(GlassPane.isDebugMode()) builder.setPrettyPrinting();
        builder.disableHtmlEscaping();
        builder.serializeNulls();
        builder.registerTypeAdapter(ResourceLocation.class, new JsonSerializerResourceLocation());
        builder.registerTypeAdapterFactory(new JsonPostProcessor());
        //TODO register more type adapters!
        GSON = builder.create();
    }
}
