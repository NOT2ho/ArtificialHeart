package com.not2ho.artificialheart.item

import com.not2ho.artificialheart.ArtificialHeart.ITEMS
import com.not2ho.artificialheart.entity.PinkEntities
import com.not2ho.artificialheart.fluid.PinkLiquid
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.{BucketItem, Item, Items, SwordItem}
import net.minecraftforge.common.ForgeSpawnEggItem
import net.minecraftforge.registries.RegistryObject

object PinkItems {

  val HUMANLIKE_SPAWN_EGG : RegistryObject[Item] = ITEMS.register( "humanlike_spawn_egg",
                                                                  () => new ForgeSpawnEggItem( PinkEntities.HUMANLIKE,
                                                                                               0xf2cccc, 0xfff9fd,
                                                                                               new Item.Properties() ) );
  
  val MASHED_HEART: RegistryObject[Item] = ITEMS.register("mashed_heart", () => new Item(new Item.Properties()))

  val BEAKER: RegistryObject[Item] = ITEMS.register("beaker", () => new Item(new Item.Properties()
    .stacksTo(1)
  ))

  val POPPY_JUICE: RegistryObject[Item] = ITEMS.register("poppy_juice"
    , () => new BeakerItem(new Item.Properties()
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val DANDELION_JUICE: RegistryObject[Item] = ITEMS.register("dandelion_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val ALLIUM_JUICE: RegistryObject[Item] = ITEMS.register("allium_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val AZURE_BLUET_JUICE: RegistryObject[Item] = ITEMS.register("azure_bluet_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val BLUE_ORCHID_JUICE: RegistryObject[Item] = ITEMS.register("blue_orchid_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val CORNFLOWER_JUICE: RegistryObject[Item] = ITEMS.register("cornflower_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val LILAC_JUICE: RegistryObject[Item] = ITEMS.register("lilac_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val LILY_OF_THE_VALLEY_JUICE: RegistryObject[Item] = ITEMS.register("lily_of_the_valley_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val OXEYE_DAISY_JUICE: RegistryObject[Item] = ITEMS.register("oxeye_daisy_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val PEONY_JUICE: RegistryObject[Item] = ITEMS.register("peony_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val SUNFLOWER_JUICE: RegistryObject[Item] = ITEMS.register("sunflower_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val TULIP_JUICE: RegistryObject[Item] = ITEMS.register("tulip_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))
  val WITHER_ROSE_JUICE: RegistryObject[Item] = ITEMS.register("wither_rose_juice"
    , () => new BeakerItem(new Item.Properties()
      .stacksTo(1)
      .food(new FoodProperties.Builder().alwaysEat()
        .build())))

  val CUTTER: RegistryObject[Item] = ITEMS.register("cutter", () => new SwordItem(PinkToolTiers.HEART, 1, 10, new Item.Properties()))
  val CLEAVER: RegistryObject[Item] = ITEMS.register("cleaver", () => new SwordItem(PinkToolTiers.HEART, 10, 0.1, new Item.Properties()))

  val RAW_HEART: RegistryObject[Item] = ITEMS.register("raw_heart"
    , () => new Item(new Item.Properties()))

  val HEART_INGOT: RegistryObject[Item] = ITEMS.register("heart_ingot"
    , () => new Item(new Item.Properties()))

  val PINK_LIQUID_BUCKET: RegistryObject[Item] = ITEMS.register("pink_liquid_bucket",
    () => new BucketItem(PinkLiquid.SOURCE_PINK_LIQUID, new Item.Properties().craftRemainder(Items.BUCKET)
      .stacksTo(1)))

}
