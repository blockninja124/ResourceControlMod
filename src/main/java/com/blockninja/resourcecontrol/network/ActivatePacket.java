package com.blockninja.resourcecontrol.network;

import com.blockninja.resourcecontrol.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ActivatePacket extends RCPacket {
    public final String packName;
    public final boolean reload;

    public ActivatePacket(String packName, boolean reload) {
        this.packName = packName;
        this.reload = reload;
    }

    public static void encode(ActivatePacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.packName);
        buf.writeBoolean(msg.reload);
    }

    public static ActivatePacket decode(FriendlyByteBuf buf) {
        return new ActivatePacket(buf.readUtf(), buf.readBoolean());
    }

    @Override
    void handleClient(NetworkEvent.Context ctx) {
        ResourceUtils.activatePack(packName, reload);
    }
}
