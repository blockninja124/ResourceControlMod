package com.blockninja.resourcecontrol.util;

import java.util.ArrayList;
import java.util.Collection;

import com.blockninja.resourcecontrol.ResourceControl;
import com.blockninja.resourcecontrol.network.LogToServerPacket;
import com.blockninja.resourcecontrol.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nullable;

public class ResourceUtils {
	private static final Logger LOGGER = LogManager.getLogger(ResourceControl.MODID);

	@OnlyIn(Dist.CLIENT)
	public static PackRepository getPackRepo() {
		Minecraft mc = Minecraft.getInstance();
		return mc.getResourcePackRepository();
	}

	private static void log(String message, int level) {
		// Log in server console
		NetworkHandler.INSTANCE.sendToServer(new LogToServerPacket(message, level));

		// Log in this clients console
		switch (level) {
			case 0:
				LOGGER.info(message);
				break;
			case 1:
				LOGGER.warn(message);
				break;
			case 2:
				LOGGER.error(message);
				break;
			default:
				LOGGER.info(message);
				LOGGER.warn("log was given an invalid log level. Expected between 0-2, was given {}", level);
				break;
		}
	}

	/**
	 * Reloads all of the resources on the client its run. Basically forcing an F3+T
	 */
	public static void reloadAll() {
		Minecraft mc = Minecraft.getInstance();
		mc.reloadResourcePacks();

		setNoReloadNeeded();
	}

	/**
	 * This function just prevents another reload when using the reload menu after
	 * pack changes have occurred.
	 */
	public static void setNoReloadNeeded() {
		PackRepository pr = getPackRepo();
		Minecraft mc = Minecraft.getInstance();
		mc.options.resourcePacks = new ArrayList<>(pr.getSelectedIds());
		mc.options.save();
	}

	/**
	 * Uses a {@link net.minecraft.commands.CommandSourceStack CommandSourceStack} to try and find an available
	 * pack with an id of {@link packId}, using the packRepository. Logs if pack is found
	 * in console
	 * @param packId the pack id as a string
	 * @param packRepository
	 * @return Returns the Pack object if the pack was found, or null
	 */
	public static Pack findPack(String packId, PackRepository packRepository) {

		Collection<Pack> availablePacks = packRepository.getAvailablePacks();

		log("Searching for pack with id of: " + packId, 0);


		String foundStatus = "not_found";
		Pack foundPack = null;


		for (Pack pack : availablePacks) {
			if (pack.getId().toString().equals("file/" + packId)) {
				foundStatus = "found";
				foundPack = pack;
				break;
			}

			// This should never overtake a situation where there is both a .zip and a folder, since if there's a folder it'll just break before getting here.
			if (pack.getId().toString().equals("file/" + packId + ".zip")) {
				foundStatus = "zip";
			}
		}

		switch (foundStatus) {

			case "found":
				log("Pack found!", 0);
				break;

			case "zip":
				// Maybe they meant packId.zip
				log("Pack with name of '"+ packId + "' wasn't found. Did you maybe mean '"+packId+".zip' instead?", 1);
				return null;

			case "not_found":
				// Just wasn't found
				log("Pack with name of '"+packId+"' wasn't found", 1);
				return null;

			default:
				// Something very strange happened
				log("Something unexpected happened when trying to find the pack", 2);
				return null;
		}

		return foundPack;
	}

	@OnlyIn(Dist.CLIENT)
	public static void logPacks(PackRepository packRepository) {
		String username = Minecraft.getInstance().player.getName().getString();

		log("Listing packs for player "+username, 0);
		log("Packs found:", 0);

		Collection<Pack> availablePacks = packRepository.getAvailablePacks();

		for (Pack pack : availablePacks) {
			log(pack.getId(), 0);
		}

		log("No other packs found.", 0);
	}

	public static int activatePack(String packId, boolean reload) {

		try {

			PackRepository packRepository = getPackRepo(); // Access to available packs

			Pack foundPack = findPack(packId, packRepository);

			if (foundPack == null) {
				return 0;
			}


			Collection<String> selectedPacks = packRepository.getSelectedIds();

			// Already active
			if (selectedPacks.contains(foundPack.getId())) {
				log("Pack is already selected", 1);
				return 0;
			}

			// Make a copy of selected ids list, so that we can change it
			Collection<String> mutableSelectedPacks = new ArrayList<>(selectedPacks);

			// Add our pack id
			mutableSelectedPacks.add("file/" + packId);

			// Set the selected packs to our new list with our pack added
			packRepository.setSelected(mutableSelectedPacks);

			packRepository.getAvailablePacks();


			if (reload) {
				// Reload the players resources. If not done, all sorts of weirdness happens.
				reloadAll();
			} else {
				setNoReloadNeeded();
			}

			log("Activated pack!", 0);

			return 1;

		} catch (Exception e) {
			log("Command failed with error: "+e.toString()+". See below for more info", 2);
			e.printStackTrace();
			return 0;
		}

	}

	public static int deactivatePack(String packId, boolean reload) {

		try {

			PackRepository packRepository = getPackRepo(); // Access to available packs

			Pack foundPack = findPack(packId, packRepository);

			if (foundPack == null) {
				return 0;
			}


			Collection<String> selectedPacks = packRepository.getSelectedIds();

			// Already de-activated
			if (!selectedPacks.contains(foundPack.getId())) {
				log("Pack is already disabled", 1);
				return 0;
			}

			// Make a copy of selected ids list, so that we can change it
			Collection<String> mutableSelectedPacks = new ArrayList<>(selectedPacks);

			// Remove our pack id
			mutableSelectedPacks.remove("file/" + packId);

			// Set the selected packs to our new list with our pack added
			packRepository.setSelected(mutableSelectedPacks);

			packRepository.getAvailablePacks();



			if (reload) {
				reloadAll();
			} else {
				setNoReloadNeeded();
			}

			log("Deactivated pack!", 0);

			return 1;

		} catch (Exception e) {
			log("Command failed with error: "+e.toString()+". See below for more info", 2);
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * Changes a resource packs position in the hierarchy. Will give CommandSourceStack an error if pack isn't enabled.
	 * Same for if pack isn't found or component is given as null.
	 * @param packId the pack id as a string
	 * @param amount If negative, will move the pack that amount towards index 0. If positive, away
	 * @param reload boolean, if true the clients resources will be reloaded after moving.
	 * @return returns an int. 0 if something failed or 1 if everything succeeded
	 */
	public static int movePack(@Nullable String packId, int amount, boolean reload) {
		try {

			PackRepository packRepository = getPackRepo(); // Access to available packs

			Pack foundPack = findPack(packId, packRepository);

			if (foundPack == null) {
				return 0;
			}

			Collection<String> selectedPacks = packRepository.getSelectedIds();

			// Make sure pack is enabled
			if (!selectedPacks.contains(foundPack.getId())) {
				log("Pack isn't enabled", 1);
				return 0;
			}

			// Make a copy of selected ids list, so that we can change it
			//TODO: why are we going from collection (selectedPacks) to ArrayList back to Collection (mutableSelectedPacks)
			Collection<String> mutableSelectedPacks = new ArrayList<>(selectedPacks);





			// Let me explain this array manipulation here.
			// First, we get the current index of the pack.
			// Say our pack is "b" and our list is: [a, b, c, d]. We get an index of 1
			// Then we remove index 1 from the list, leaving us with [a, c, d]
			// Then we add back our pack at oldIndex + 1 (which is 2 in this example).
			// That leaves us with the list [a, c, b, d] which has basically moved b one to the right,
			// Or one lower on the list. Boom we've moved are pack. Then you just gotta clamp
			// to not go out of bounds

			//TODO: IMPORTANT probably need to change this so it wont try to go below the mod resources and vanilla resources in priority. Probably will crash lol

			ArrayList<String> array = new ArrayList<>(selectedPacks);


			int oldIndex = array.indexOf(foundPack.getId());
			int newIndex = oldIndex + amount;

			array.remove(oldIndex);


			// This is just clamping between 0 and array.size()
			newIndex = Math.max(0, Math.min(array.size(), newIndex));


			array.add(newIndex, foundPack.getId());


			//mutableSelectedPacks.
			// Remove our pack id
			//mutableSelectedPacks.remove("file/" + component.getString());

			// Set the selected packs to our new list with our pack added
			packRepository.setSelected(array);

			//TODO: is this needed? Its also in activatePack and deactivatePack
			packRepository.getAvailablePacks();


			if (reload) {
				// If not done, all sorts of weirdness happens.
				ResourceUtils.reloadAll();
			} else {
				setNoReloadNeeded();
			}

			log("Moved pack!", 0);

			return 1;

		} catch (Exception e) {
			log("Command failed with error: "+e.toString()+". See below for more info", 2);
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Moves a pack up in the pack priority hierarchy. If amount is 0, will move it the max amount up.
	 * @param packId the pack id as a string
	 * @param amount amount of places to move the pack up, or to top if 0.
	 * @return int, 0 if something failed or 1 if everything succeeded.
	 * @see #movePackDown
	 */
	public static int movePackUp(@Nullable String packId, int amount, boolean reload) {
		//TODO: should this be ===? I saw on SO that == is comparing place in memory, which seems not quite what i want
		if (amount == 0) {
			amount = 24000; //big enough?
		}

		// Maybe should be negative? Not sure if index 0 is top priority or just max index
		return movePack(packId, amount, reload);
	}

	/**
	 * Moves a pack down in the pack priority hierarchy. If amount is 0, will move it the max amount down.
	 * It will not / cannot send the pack below the 'mod resources' and 'minecraft' resources.
	 * @param packId the pack id as a string
	 * @param amount amount of places to move the pack down, or to bottom if 0.
	 * @return int, 0 if something failed or 1 if everything succeeded.
	 * @see #movePackUp
	 */
	public static int movePackDown(@Nullable String packId, int amount, boolean reload) {

		if (amount == 0) {
			amount = 24000; //small enough?
		}

		return movePack(packId, -amount, reload);
	}
}
