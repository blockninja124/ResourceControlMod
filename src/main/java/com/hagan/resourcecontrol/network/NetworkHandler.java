package com.hagan.resourcecontrol.network;

import com.hagan.resourcecontrol.ResourceControl;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ResourceControl.MODID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++,
        		ReloadAllPacket.class,
                ReloadAllPacket::encode,
                ReloadAllPacket::decode,
                ReloadAllPacket::handle
        );
        
        INSTANCE.registerMessage(id++,
        		ActivatePackPacket.class,
                ActivatePackPacket::encode,
                ActivatePackPacket::decode,
                ActivatePackPacket::handle
        );
    }

}

