package com.hagan.resourcecontrol.network;

import com.hagan.resourcecontrol.util.ResourceUtils;
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
