package com.hagan.resourcecontrol.network;

import com.hagan.resourcecontrol.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ActivatePackPacket {

    public final String packId;
    public final boolean reload;

    public ActivatePackPacket(String packId_, boolean reload_) {
        packId = packId_;
        reload = reload_;
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(packId);
        friendlyByteBuf.writeBoolean(reload);
    }

    public static ActivatePackPacket decode(FriendlyByteBuf friendlyByteBuf) {
        String decodedPackId = friendlyByteBuf.readUtf();
        boolean decodedReload = friendlyByteBuf.readBoolean();
        return new ActivatePackPacket(decodedPackId, decodedReload);
    }

    public static void handle(ActivatePackPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(msg, contextSupplier));
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(ActivatePackPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        Pack foundPack = ResourceUtils.findPack(msg.packId);

        // Pack wasn't found. TODO: Send this back to command source somehow?
        if (foundPack == null) {
            return;
        }

        ResourceUtils.activatePack(foundPack);

        if (msg.reload) {
            ResourceUtils.reloadAll();
        }
    }
}
