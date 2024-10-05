package com.hagan.resourcecontrol.network;

import java.util.function.Supplier;

import com.hagan.resourcecontrol.util.ResourceUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;



public class ReloadResourcePacket {
	private final String resourcePath;


    public ReloadResourcePacket(String resourcePath) {
    	this.resourcePath = resourcePath;
    }


    public static void encode(ReloadResourcePacket msg, FriendlyByteBuf buf) {
    	buf.writeUtf(msg.resourcePath);
    }

    
    public static ReloadResourcePacket decode(FriendlyByteBuf buf) {
    	//System.out.println(buf);
    	//System.out.println(buf.readUtf());
        return new ReloadResourcePacket(buf.readUtf());
    }

    // TODO: This is less of a security risk then ResourcesPacket, but still bad
    public static void handle(ReloadResourcePacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			try {
                ResourceUtils.reloadSingleTexture(msg.resourcePath);
			} catch (Exception e) {
                e.printStackTrace(); // Print the full exception stack trace
                System.out.println("Exception during resource handling: " + e.getMessage());
            }
        });

    	ctx.get().setPacketHandled(true);
    	
    }
}
