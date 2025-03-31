package com.hagan.resourcecontrol.network;

import com.hagan.resourcecontrol.util.ResourceUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
