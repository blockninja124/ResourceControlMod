package com.blockninja.resourcecontrol.network;

import com.blockninja.resourcecontrol.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class MoveUpPacket extends RCPacket {
    public final String packName;
    public final int amount;
    public final boolean reload;

    public MoveUpPacket(String packName, int amount, boolean reload) {
        this.packName = packName;
        this.amount = amount;
        this.reload = reload;
    }

    public static void encode(MoveUpPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.packName);
        buf.writeInt(msg.amount);
        buf.writeBoolean(msg.reload);
    }

    public static MoveUpPacket decode(FriendlyByteBuf buf) {
        return new MoveUpPacket(buf.readUtf(), buf.readInt(), buf.readBoolean());
    }

    @Override
    void handleClient(NetworkEvent.Context ctx) {
        ResourceUtils.movePackUp(packName, amount, reload);
    }
}
