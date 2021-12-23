package ch.skyfy.tipsandtricks;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CmdDisableTips {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("disableTips")
                .then(
                        CommandManager.argument("boolean", BoolArgumentType.bool())
                                .executes(context -> {
                                    var value = context.getArgument("boolean", Boolean.class);
                                    if(value) TipsAndTricks.disable();
                                    else TipsAndTricks.enable();
                                    return Command.SINGLE_SUCCESS;
                                })
                )
        );

    }
}
