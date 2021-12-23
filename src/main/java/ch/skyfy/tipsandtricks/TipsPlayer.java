package ch.skyfy.tipsandtricks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.*;

public class TipsPlayer {

    private final ServerPlayerEntity player;

    private final List<String> shuffledTips;

    private final Timer timer;

    //        private int delay = new Random().nextInt(7_200_00, 10_800_000); // Between 2 hours and 3 hours in millis
    private final int delay = new Random().nextInt(30000, 60000); // test

    public TipsPlayer(ServerPlayerEntity player, List<String> shuffledTips) {
        this.player = player;
        this.shuffledTips = shuffledTips;
        timer = new Timer();
        start();
    }

    public void disable(){
        timer.cancel();
    }

    /**
     * Randomly (between 1-2 hours) display a tip to the player
     */
    public void start() {
        var deque = new ArrayDeque<>(shuffledTips);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                var tip = deque.poll();
                if (tip != null) deque.offer(tip);
                announce(tip);
            }
        }, delay, delay);
    }

    private void announce(String tip) {
        // Play a sound
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_ANVIL_FALL, SoundCategory.BLOCKS, 1f, 1f);

        // TODO display the tip (a big gui (TOP CENTERED, like Advancement Plaques mod))
        MinecraftClient.getInstance().getToastManager().add(new TipsToast(tip));
    }

}