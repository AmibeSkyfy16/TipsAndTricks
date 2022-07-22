package ch.skyfy.tipsandtricks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TipsAndTricks implements ModInitializer {

    private static TipsPlayer tipsPlayer;

    public static void enable() {
        if (tipsPlayer != null) tipsPlayer.start();
    }

    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity player) {
                tipsPlayer = new TipsPlayer(player, shuffle());
            }
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CmdDisableTips.register(dispatcher));
    }

    public static List<String> shuffle() {
        var list = new ArrayList<>(Data.TIPS);
        Collections.shuffle(list);
        return list;
    }

    public static void disable() {
        if (tipsPlayer != null) tipsPlayer.disable();
    }

}
