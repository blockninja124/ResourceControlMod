package com.blockninja.resourcecontrol.network;

import com.blockninja.resourcecontrol.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ReloadAllPacket extends RCPacket {

    public static void encode(ReloadAllPacket msg, FriendlyByteBuf buf) {}

    public static ReloadAllPacket decode(FriendlyByteBuf buf) {
        return new ReloadAllPacket();
    }

    @Override
    void handleClient(NetworkEvent.Context ctx) {
        ResourceUtils.reloadAll();
    }
}
