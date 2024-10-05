package com.hagan.resourcecontrol.commands;

import com.hagan.resourcecontrol.network.NetworkHandler;
import com.hagan.resourcecontrol.network.ReloadResourcePacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;

import javax.annotation.Nullable;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class ReloadOne {

   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(Component.translatable("commands.teleport.invalidPosition"));
   
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher_) {
	   dispatcher_.register(Commands.literal("reloadone").requires((player) -> {
		   return player.hasPermission(3);
	   }).then(Commands.argument("targets", EntityArgument.players()).executes((player) -> {
		   return reloadPlayers(player.getSource(), EntityArgument.getPlayers(player, "targets"), null);
	   }).then(Commands.argument("reason", MessageArgument.message()).executes((player) -> {
	         return reloadPlayers(player.getSource(), EntityArgument.getPlayers(player, "targets"), MessageArgument.getMessage(player, "reason"));
	   }))
	));
  };
   

   
   private static int reloadPlayers(CommandSourceStack commandSource, Collection<ServerPlayer> players, @Nullable Component component) {
	   
	   if (component == null) {
		   commandSource.sendSuccess(() -> {
			   return Component.literal("No resource specified");
		   }, false);
		   return 0;
	   }
	   
	   for(ServerPlayer serverplayer : players) {
		   //TODO: find out if the packet was successful and use that here
		   sendReloadPacketToPlayer(serverplayer, component);
	   }
	   
	  
	   
	   commandSource.sendSuccess(() -> {
		   return Component.literal("Reloaded resource of players");
	   }, true);
	   
	  return 1;
   }
   
   public static void sendReloadPacketToPlayer(ServerPlayer targetPlayer, Component component) {
	    
	    //NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> targetPlayer), new ReloadResourcePacket(component.getString()));
	}
}


