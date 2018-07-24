/*
 * This file is part of Hot or Not.
 *
 * Copyright 2018, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.industrial.proxy.client.infopiece;

import com.buuz135.industrial.proxy.client.ClientProxy;
import com.buuz135.industrial.tile.world.LaserBaseTile;
import net.minecraft.util.text.TextComponentTranslation;
import net.ndrei.teslacorelib.gui.BasicRenderedGuiPiece;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;

import java.text.DecimalFormat;
import java.util.Arrays;

public class LaserBaseInfoPiece extends BasicRenderedGuiPiece {

    private LaserBaseTile tile;

    public LaserBaseInfoPiece(LaserBaseTile tile, int left, int top) {
        super(left, top, 18, 54, ClientProxy.GUI, 93, 72);
        this.tile = tile;
    }

    @Override
    public void drawBackgroundLayer(BasicTeslaGuiContainer container, int guiX, int guiY, float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundLayer(container, guiX, guiY, partialTicks, mouseX, mouseY);
        container.mc.getTextureManager().bindTexture(ClientProxy.GUI);
        double work = this.tile.getCurrentWork() / (double) this.tile.getMaxWork();
        container.drawTexturedRect(this.getLeft() + 3, (int) (this.getTop() + 3 + (50 - (50 * work))), 112, (int) (72 + (50 - (50 * work))), 12, (int) (work * 50));

    }

    @Override
    public void drawForegroundLayer(BasicTeslaGuiContainer container, int guiX, int guiY, int mouseX, int mouseY) {
        super.drawForegroundLayer(container, guiX, guiY, mouseX, mouseY);
        if (isInside(container, mouseX, mouseY)) {
            double eff = (this.tile.getCurrentWork() / (double) this.tile.getMaxWork());
            container.drawTooltip(Arrays.asList(new TextComponentTranslation("text.industrialforegoing.display.work").getUnformattedComponentText() + " " + new DecimalFormat("##.##").format(100 * eff) + "%"), mouseX - guiX, mouseY - guiY);
        }
    }
}
