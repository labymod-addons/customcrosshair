package net.labymod.addons.customcrosshair.renderer;

import net.labymod.api.client.render.matrix.Stack;

public interface CrosshairFormRenderer
{
    void render(Stack stack, float x, float y, float gap, int color, int outlineColor);
}
