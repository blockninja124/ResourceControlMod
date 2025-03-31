package com.hagan.resourcecontrol.network;

import com.hagan.resourcecontrol.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class MoveDownPacket extends RCPacket {
    public final String packName;
    public final int amount;
    public final boolean reload;

    public MoveDownPacket(String packName, int amount, boolean reload) {
        this.packName = packName;
        this.amount = amount;
        this.reload = reload;
    }

    public static void encode(MoveDownPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.packName);
        buf.writeInt(msg.amount);
        buf.writeBoolean(msg.reload);
    }

    public static MoveDownPacket decode(FriendlyByteBuf buf) {
        return new MoveDownPacket(buf.readUtf(), buf.readInt(), buf.readBoolean());
    }

    @Override
    void handleClient(NetworkEvent.Context ctx) {
        ResourceUtils.movePackDown(packName, amount, reload);
    }
}
