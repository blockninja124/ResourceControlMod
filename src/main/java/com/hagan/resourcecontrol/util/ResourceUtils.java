package com.hagan.resourcecontrol.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import javax.annotation.Nullable;

public class ResourceUtils {

	private static final Logger LOGGER = LogUtils.getLogger();


	public static void reloadSingleTexture(String resourcePath) {
		
		Minecraft mc = Minecraft.getInstance();
		
		
	    File textureFile = null;
	    
	    ResourceLocation rl = new ResourceLocation(resourcePath);
	    
	    TextureManager tm = mc.getTextureManager();
	    
	    
	    /*try {
	    	textureFile = new File(rl.getPath());
	    } catch (Exception ex) {
	    	
	    }

	    if (textureFile != null && textureFile.exists()) {
	        ResourceLocation MODEL_TEXTURE = Resources.OTHER_TESTMODEL_CUSTOM;

	        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
	        texturemanager.deleteTexture(MODEL_TEXTURE);
	        Object object = new ThreadDownloadImageData(textureFile, null, MODEL_TEXTURE, new ImageBufferDownload());
	        texturemanager.loadTexture(MODEL_TEXTURE, (ITextureObject)object);

	        return;
	    } else {
	        return;
	    }*/
		
//        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        
//        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

//        ModelManager modelManager = Minecraft.getInstance().getModelManager();

//        ResourceLocation resourceLocation = new ResourceLocation(resourcePath);
        
//        System.out.println(resourceManager.getResource(resourceLocation));
        
//        System.out.println("Resource location:");
//        System.out.println(resourceLocation);
        
//        AbstractTexture texture = textureManager.getTexture(resourceLocation);
//        if (texture == null) {
//        	return;
//        }
        
//        textureManager.release(resourceLocation);
        
        //SimpleTexture simpleTexture = (SimpleTexture) texture;
        
//        SimpleTexture newTexture = new SimpleTexture(resourceLocation);
//        textureManager.register(resourceLocation, newTexture);

        // Invalidate the existing texture (if already loaded)
        //textureManager.release(resourceLocation);

        // Rebind the texture, forcing it to be loaded
        //textureManager.bindForSetup(resourceLocation);
        
        //System.out.println(textureManager.getTexture(resourceLocation));
//        ResourceLocation modelLocation = new ResourceLocation("minecraft:models/block/oak_leaves1.json");
        
//        BakedModel bakedModel = modelManager.getModel(modelLocation);
        
        
//        if (bakedModel != null) {
//            System.out.println("Re-baking the model: " + modelLocation);

            // Trigger a full reload of the model manager to refresh baked models
//            modelManager.onResourceManagerReload(resourceManager);

//            System.out.println("Model reloaded and baked: " + modelLocation);
//        } else {
//            System.out.println("Model not found: " + modelLocation);
//        }
	}

	/**
	 * Reloads all of the resources on the client its run. Basically forcing an F3+T
	 */
	@OnlyIn(Dist.CLIENT)
	public static void reloadAll() {
		Minecraft mc = Minecraft.getInstance();
		mc.reloadResourcePacks();
		
		// This just prevents another reload when using the reload menu after this command has already done it
		PackRepository rm = mc.getResourcePackRepository();
		mc.options.resourcePacks = new ArrayList<>(rm.getSelectedIds());
		mc.options.save();
	}

	/**
	 * Tries to find a pack with an id of {@code packId}, using the packRepository of the current client.
	 * @param packId the pack id as a string
	 * @return Returns the Pack object if the pack was found, or null if not
	 */
	@OnlyIn(Dist.CLIENT)
	public static Pack findPack(String packId) {

		// ----- Get packs ----- //
		Minecraft mc = Minecraft.getInstance();
		PackRepository packRepository = mc.getResourcePackRepository();
		Collection<Pack> availablePacks = packRepository.getAvailablePacks();


		// ----- Find pack ----- //
		LOGGER.info("Searching for pack with id of: " + packId);

		Pack foundPack = null;

		for (Pack pack : availablePacks) {
			if (pack.getId().toString().equals("file/" + packId)) {
				foundPack = pack;
				break;
			}

		}

		// ----- Return ----- //
		return foundPack;
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean activatePack(Pack pack) {

		Minecraft mc = Minecraft.getInstance();
		PackRepository packRepository = mc.getResourcePackRepository(); // Access to available packs

		Collection<String> selectedPacks = packRepository.getSelectedIds();

		// Make sure its not already active
		if (selectedPacks.contains(pack.getId())) {
			return false;
		}

		// Make a copy of selected ids list, so that we can change it
		Collection<String> mutableSelectedPacks = new ArrayList<>(selectedPacks);

		// Add our pack id
		mutableSelectedPacks.add("file/" + pack.getId());

		// Set the selected packs to our new list with our pack added
		packRepository.setSelected(mutableSelectedPacks);

		packRepository.getAvailablePacks();

		return true;
	};

}
