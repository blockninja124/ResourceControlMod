package com.blockninja.resourcecontrol.network;

import com.blockninja.resourcecontrol.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class LogPacksPacket extends RCPacket {

    public static void encode(LogPacksPacket msg, FriendlyByteBuf buf) {}

    public static LogPacksPacket decode(FriendlyByteBuf buf) {
        return new LogPacksPacket();
    }

    @Override
    void handleClient(NetworkEvent.Context ctx) {
        ResourceUtils.logPacks(ResourceUtils.getPackRepo());
    }
}
