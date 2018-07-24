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
package com.buuz135.industrial.tile.block;

import com.buuz135.industrial.book.BookCategory;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.tile.magic.EnchantmentRefinerTile;
import com.buuz135.industrial.utils.RecipeUtils;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.ndrei.teslacorelib.items.MachineCaseItem;

public class EnchantmentRefinerBlock extends CustomOrientedBlock<EnchantmentRefinerTile> {

    public EnchantmentRefinerBlock() {
        super("enchantment_refiner", EnchantmentRefinerTile.class, Material.ROCK, 400, 10);
    }

    public void createRecipe() {
        RecipeUtils.addShapedRecipe(new ItemStack(this, 1), "pep", "bmn", "pgp",
                'p', ItemRegistry.plastic,
                'e', Items.ENDER_PEARL,
                'b', Items.BOOK,
                'm', MachineCaseItem.INSTANCE,
                'n', Items.ENCHANTED_BOOK,
                'g', "gearDiamond");
    }

    @Override
    public BookCategory getCategory() {
        return BookCategory.MAGIC;
    }

}
