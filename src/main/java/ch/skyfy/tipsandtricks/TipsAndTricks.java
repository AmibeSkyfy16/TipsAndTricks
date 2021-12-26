package ch.skyfy.tipsandtricks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

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
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> CmdDisableTips.register(dispatcher));
    }

    public static List<TranslatableText> shuffle() {
        var list = new ArrayList<>(Data.TIPS);
        Collections.shuffle(list);
        return list;
    }

    public static void disable() {
        if (tipsPlayer != null) tipsPlayer.disable();
    }

}
