package com.hagan.resourcecontrol.commands;

import com.hagan.resourcecontrol.network.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.logging.LogUtils;

import org.slf4j.Logger;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RC {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher_) {
	    dispatcher_.register(Commands.literal("rc").requires((ctx) -> {
                    return true;
	   	        }
            ).then(Commands.literal("reloadall")
		        .executes((ctx) -> {
						// 'execute as' will have the source player be the one we need to send to
						NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new ReloadAllPacket());
		                return 1;
		            }
		        )
		    ).then(Commands.literal("activate")
		         .then(Commands.argument("packname", StringArgumentType.string())
			         .executes((ctx) -> {
						 	NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new ActivatePacket(StringArgumentType.getString(ctx, "packname"), true));
						 	return 1;
			             }
			         ).then(Commands.argument("reload", BoolArgumentType.bool())
			               .executes((ctx) -> {
							       NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new ActivatePacket(StringArgumentType.getString(ctx, "packname"), BoolArgumentType.getBool(ctx, "reload")));
								   return 1;
			                   }
			               )
			         )
			     )
		    ).then(Commands.literal("deactivate")
			     .then(Commands.argument("packname", StringArgumentType.string())
			         .executes((ctx) -> {
						 	NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new DeactivatePacket(StringArgumentType.getString(ctx, "packname"), true));
						 	return 1;
					     }
					 ).then(Commands.argument("reload", BoolArgumentType.bool())
			               .executes((ctx) -> {
							   NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new DeactivatePacket(StringArgumentType.getString(ctx, "packname"), BoolArgumentType.getBool(ctx, "reload")));
							   return 1;
			                   }
			               )
			         )
				 )
			).then(Commands.literal("moveup")
		         .then(Commands.argument("packname", StringArgumentType.string())
					 .executes((ctx) -> {
						 NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new MoveUpPacket(StringArgumentType.getString(ctx, "packname"), 0, true));
						 return 1;
					 })
		             .then(Commands.argument("amount", IntegerArgumentType.integer())
		                 .executes((ctx) -> {
							 	NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new MoveUpPacket(StringArgumentType.getString(ctx, "packname"), IntegerArgumentType.getInteger(ctx, "amount"), true));
							 	return 1;
		                     }
		                 ).then(Commands.argument("reload", BoolArgumentType.bool())
					               .executes((ctx) -> {
									   NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new MoveUpPacket(StringArgumentType.getString(ctx, "packname"), IntegerArgumentType.getInteger(ctx, "amount"), BoolArgumentType.getBool(ctx, "reload")));
									   return 1;
				                   }
				               )
				         )
		             )
	    		 )
	        ).then(Commands.literal("movedown")
			     .then(Commands.argument("packname", StringArgumentType.string())
					 .executes((ctx) -> {
						 NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new MoveDownPacket(StringArgumentType.getString(ctx, "packname"), 0, true));
						 return 1;
					 })
				     .then(Commands.argument("amount", IntegerArgumentType.integer())
				         .executes((ctx) -> {
							 	NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new MoveDownPacket(StringArgumentType.getString(ctx, "packname"), IntegerArgumentType.getInteger(ctx, "amount"), true));
							 	return 1;
				             }
				         ).then(Commands.argument("reload", BoolArgumentType.bool())
					               .executes((ctx) -> {
									   NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new MoveDownPacket(StringArgumentType.getString(ctx, "packname"), IntegerArgumentType.getInteger(ctx, "amount"), BoolArgumentType.getBool(ctx, "reload")));
									   return 1;
				                   }
				               )
				         )
				     )
			     )
		    ).then(Commands.literal("logpacks")
				.executes((ctx) -> {
					NetworkHandler.sendRCPacket(ctx.getSource().getPlayer(), new LogPacksPacket());
					return 1;
				})
			)
        );

  };


}
