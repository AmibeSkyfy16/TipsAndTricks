package ch.skyfy.tipsandtricks;

import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;

public class TipsToast implements Toast{

    private final String tip;

    public TipsToast(String tip) {
        this.tip = tip;
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        return null;
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
