package com.blockninja.resourcecontrol.network;

import com.blockninja.resourcecontrol.ResourceControl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
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
                MoveUpPacket.class,
                MoveUpPacket::encode,
                MoveUpPacket::decode,
                MoveUpPacket::handle
        );

        INSTANCE.registerMessage(id++,
                MoveDownPacket.class,
                MoveDownPacket::encode,
                MoveDownPacket::decode,
                MoveDownPacket::handle
        );

        INSTANCE.registerMessage(id++,
                ActivatePacket.class,
                ActivatePacket::encode,
                ActivatePacket::decode,
                ActivatePacket::handle
        );

        INSTANCE.registerMessage(id++,
                DeactivatePacket.class,
                DeactivatePacket::encode,
                DeactivatePacket::decode,
                DeactivatePacket::handle
        );

        INSTANCE.registerMessage(id++,
                LogPacksPacket.class,
                LogPacksPacket::encode,
                LogPacksPacket::decode,
                LogPacksPacket::handle
        );

        INSTANCE.registerMessage(id++,
                LogToServerPacket.class,
                LogToServerPacket::encode,
                LogToServerPacket::decode,
                LogToServerPacket::handle
        );
    }

    public static <T extends RCPacket> void sendRCPacket(ServerPlayer to, T packet) {
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> to), packet);
    }
}

