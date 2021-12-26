package ch.skyfy.tipsandtricks;

import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class TipsToast implements Toast{

    private final TranslatableText tip;

    public TipsToast(TranslatableText tip) {
        this.tip = tip;
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        return Visibility.SHOW;
    }

    @Override
    public Object getType() {
        return Toast.super.getType();
    }

    @Override
    public int getWidth() {
        return Toast.super.getWidth();
    }

    @Override
    public int getHeight() {
        return Toast.super.getHeight();
    }
}
