package com.hagan.resourcecontrol.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    /*private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("mymod", "network"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++,
        		ReloadResourcesPacket.class,
        		ReloadResourcesPacket::encode,
        		ReloadResourcesPacket::decode,
        		ReloadResourcesPacket::handle
        );
        
        INSTANCE.registerMessage(id++,
        		ReloadResourcePacket.class,
        		ReloadResourcePacket::encode,
        		ReloadResourcePacket::decode,
        		ReloadResourcePacket::handle
        );
    }*/
}

