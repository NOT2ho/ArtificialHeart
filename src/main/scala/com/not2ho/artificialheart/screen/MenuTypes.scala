package com.not2ho.artificialheart.screen

import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.common.extensions.IForgeMenuType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object MenuTypes {
  val MENUS: DeferredRegister[MenuType[_]] = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ArtificialHeart.MOD_ID)
  val FLOWER_JUICER_MENU: RegistryObject[MenuType[FlowerJuicerMenu]] = registerMenuType("flower_juicer_menu", new FlowerJuicerMenu(_,_,_))
  private def registerMenuType[T <: AbstractContainerMenu](name: String, factory: IContainerFactory[T]) = MENUS.register(name, () => IForgeMenuType.create(factory))

  def register(eventBus: IEventBus): Unit = {
    MENUS.register(eventBus)
  }
}