package ch.skyfy.tipsandtricks;

import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static final List<TranslatableText> TIPS = new ArrayList<>(){{
        for (int i = 0; i < 3; i++) {
            add(new TranslatableText("tipsandtricks.tip."+i));
        }
    }};
}
