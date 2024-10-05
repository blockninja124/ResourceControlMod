package com.hagan.resourcecontrol.commands;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegister {
    
    @SubscribeEvent
    public static void onRegisterCommands(final RegisterCommandsEvent event) {
    	//ReloadAll.register(event.getDispatcher());
    	//ReloadOne.register(event.getDispatcher());
    	//ActivatePack.register(event.getDispatcher());
    	RC.register(event.getDispatcher());
    }
}