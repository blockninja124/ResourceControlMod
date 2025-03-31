package com.hagan.resourcecontrol.network;

import com.hagan.resourcecontrol.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class DeactivatePacket extends RCPacket {
    public final String packName;
    public final boolean reload;

    public DeactivatePacket(String packName, boolean reload) {
        this.packName = packName;
        this.reload = reload;
    }

    public static void encode(DeactivatePacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.packName);
        buf.writeBoolean(msg.reload);
    }

    public static DeactivatePacket decode(FriendlyByteBuf buf) {
        return new DeactivatePacket(buf.readUtf(), buf.readBoolean());
    }

    @Override
    void handleClient(NetworkEvent.Context ctx) {
        ResourceUtils.deactivatePack(packName, reload);
    }
}
