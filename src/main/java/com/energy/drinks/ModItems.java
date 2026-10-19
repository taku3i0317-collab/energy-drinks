package com.energy.drinks;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;   // ← 追加！
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

import java.util.List;
import java.util.function.Function;

public class ModItems {

    // 飲んだときの効果：移動速度II と 採掘速度I を30秒（20tick = 1秒）
    public static final Consumable ENERGY_DRINK_CONSUMABLE = Consumables.defaultDrink()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.SPEED, 20 * 30, 1),
                    new MobEffectInstance(MobEffects.HASTE, 20 * 30, 0)
            )))
            .build();

    // 満腹度などの設定
    public static final FoodProperties ENERGY_DRINK_FOOD = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0.1f)
            .alwaysEdible()
            .build();

    // エナジードリンク本体
    public static final Item ENERGY_DRINK = register("energy_drink", Item::new,
            new Item.Properties()
                    .food(ENERGY_DRINK_FOOD, ENERGY_DRINK_CONSUMABLE)
                    .usingConvertsTo(Items.GLASS_BOTTLE)   // ← 追加！飲んだらガラス瓶が残る
                    .stacksTo(16));

    // アイテムを登録する関数
    public static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, EnergyDrinks.id(name));
        Item item = factory.apply(properties.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register(tab -> tab.accept(ENERGY_DRINK));
    }
}