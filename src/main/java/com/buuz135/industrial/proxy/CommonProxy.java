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
package com.buuz135.industrial.proxy;

import com.buuz135.industrial.IndustrialForegoing;
import com.buuz135.industrial.config.CustomConfiguration;
import com.buuz135.industrial.entity.EntityPinkSlime;
import com.buuz135.industrial.gui.GuiHandler;
import com.buuz135.industrial.proxy.event.*;
import com.buuz135.industrial.proxy.network.ConveyorButtonInteractMessage;
import com.buuz135.industrial.proxy.network.ConveyorSplittingSyncEntityMessage;
import com.buuz135.industrial.registry.IFRegistries;
import com.buuz135.industrial.utils.CraftingUtils;
import com.buuz135.industrial.utils.RecipeUtils;
import com.buuz135.industrial.utils.Reference;
import com.buuz135.industrial.utils.apihandlers.CraftTweakerHelper;
import com.buuz135.industrial.utils.apihandlers.PlantRecollectableRegistryHandler;
import com.buuz135.industrial.utils.apihandlers.RecipeHandlers;
import com.buuz135.industrial.utils.apihandlers.json.ConfigurationConditionFactory;
import com.buuz135.industrial.utils.compat.baubles.MeatFeederBauble;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Random;

public class CommonProxy {

    public static Random random;

    public static DamageSource custom = new DamageSource("if_custom");
    public static ResourceLocation PINK_SLIME_LOOT;

    public void preInit(FMLPreInitializationEvent event) {
        IFRegistries.poke();

        CraftingHelper.register(new ResourceLocation(Reference.MOD_ID, "configuration_value"), new ConfigurationConditionFactory());
        random = new Random();

        FluidsRegistry.registerFluids();
        BlockRegistry.poke();

        MinecraftForge.EVENT_BUS.register(new BlockRegistry());
        MinecraftForge.EVENT_BUS.register(new ItemRegistry());
        MinecraftForge.EVENT_BUS.register(new StrawRegistry());
        MinecraftForge.EVENT_BUS.register(new ConveyorRegistry());
        MinecraftForge.EVENT_BUS.register(new MeatFeederTickHandler());
        MinecraftForge.EVENT_BUS.register(new MobDeathHandler());
        MinecraftForge.EVENT_BUS.register(new WorldTickHandler());
        MinecraftForge.EVENT_BUS.register(new PlantRecollectableRegistryHandler());
        MinecraftForge.EVENT_BUS.register(new FakePlayerRideEntityHandler());
        MinecraftForge.EVENT_BUS.register(new PlantInteractorHarvestDropsHandler());
        MinecraftForge.EVENT_BUS.register(new SkullHandler());

        NetworkRegistry.INSTANCE.registerGuiHandler(IndustrialForegoing.instance, new GuiHandler());
        int id = 0;
        IndustrialForegoing.NETWORK.registerMessage(ConveyorButtonInteractMessage.Handler.class, ConveyorButtonInteractMessage.class, ++id, Side.SERVER);
        IndustrialForegoing.NETWORK.registerMessage(ConveyorSplittingSyncEntityMessage.Handler.class, ConveyorSplittingSyncEntityMessage.class, ++id, Side.CLIENT);

        CustomConfiguration.config = new Configuration(event.getSuggestedConfigurationFile());
        CustomConfiguration.config.load();
        CustomConfiguration.sync();
        CustomConfiguration.configValues = new HashMap<>();
        CustomConfiguration.configValues.put("useTEFrames", CustomConfiguration.config.getBoolean("useTEFrames", Configuration.CATEGORY_GENERAL, true, "Adds recipes using Thermal Expansion frames"));
        CustomConfiguration.configValues.put("useEnderIOFrames", CustomConfiguration.config.getBoolean("useEnderIOFrames", Configuration.CATEGORY_GENERAL, true, "Adds recipes using EnderIO frames"));
        CustomConfiguration.configValues.put("useOriginalFrames", CustomConfiguration.config.getBoolean("useOriginalFrames", Configuration.CATEGORY_GENERAL, true, "Adds recipes using TeslaCoreLib frames"));
        CustomConfiguration.configValues.put("useMekanismFrames", CustomConfiguration.config.getBoolean("useMekanismFrames", Configuration.CATEGORY_GENERAL, true, "Adds recipes using Mekanism Steel Casing"));
        CustomConfiguration.configValues.put("machines.wither_builder.HCWither", CustomConfiguration.config.getBoolean("HCWither", "machines.wither_builder", false, "If enabled, only the wither builder will be able to place wither skulls. That means that players won't be able to place wither skulls. The recipe will change, but that will need a restart."));
        if (CustomConfiguration.config.hasChanged()) CustomConfiguration.config.save();

        if (Loader.isModLoaded("crafttweaker")) CraftTweakerHelper.register();
        if (Loader.isModLoaded("baubles")) MinecraftForge.EVENT_BUS.register(new MeatFeederBauble.Event());

        EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, "pink_slime"), EntityPinkSlime.class, "pink_slime", 135135, IndustrialForegoing.instance, 32, 1, false, 10485860, 16777215);
        PINK_SLIME_LOOT = LootTableList.register(new ResourceLocation(Reference.MOD_ID, "entities/pink_slime"));
    }

    public void init() {
        RecipeHandlers.loadBioReactorEntries();
        RecipeHandlers.loadLaserLensEntries();
        RecipeHandlers.loadSludgeRefinerEntries();
        RecipeHandlers.loadProteinReactorEntries();
        RecipeHandlers.loadFluidDictionaryEntries();
        RecipeHandlers.loadWoodToLatexEntries();
    }

    public void postInit() {
        CraftingUtils.generateCrushedRecipes();
        BlockRegistry.createRecipes();
        ItemRegistry.createRecipes();
        RecipeUtils.generateConstants();
        RecipeHandlers.executeCraftweakerActions();
    }
}
