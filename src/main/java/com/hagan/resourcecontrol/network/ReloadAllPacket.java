package com.hagan.resourcecontrol.network;

import com.hagan.resourcecontrol.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadAllPacket {

    public ReloadAllPacket() {
        //Do nothing for now
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        //Do nothing for now
    }

    public static ReloadAllPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new ReloadAllPacket();
    }

    public static void handle(ReloadAllPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg, contextSupplier));
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(ReloadAllPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        ResourceUtils.reloadAll();
    }
}
