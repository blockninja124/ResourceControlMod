package com.blockninja.resourcecontrol.network;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class RCPacket {

    public RCPacket() {}

    @OnlyIn(Dist.CLIENT)
    void handleClient(NetworkEvent.Context ctx) {};

    @OnlyIn(Dist.DEDICATED_SERVER)
    void handleServer(NetworkEvent.Context ctx) {};

    static <T extends RCPacket> void handle(T msg, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) {
                // Client
                msg.handleClient(ctx.get());
            } else {
                // Server
                msg.handleServer(ctx.get());
            }
        });

        ctx.get().setPacketHandled(true);

    }
}
