package com.not2ho.artificialheart.fluid

import com.not2ho.artificialheart.ArtificialHeart
import com.not2ho.artificialheart.block.PinkBlocks.PINK_LIQUID_BLOCK
import com.not2ho.artificialheart.item.PinkItems.PINK_LIQUID_BUCKET
import net.minecraft.world.level.material.{FlowingFluid, Fluid}
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fluids.ForgeFlowingFluid
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object PinkLiquid {
  val FLUIDS: DeferredRegister[Fluid] =
    DeferredRegister.create(ForgeRegistries.FLUIDS, ArtificialHeart.MOD_ID)

  val SOURCE_PINK_LIQUID: RegistryObject[FlowingFluid] = FLUIDS.register("pink_fluid",
    () => new ForgeFlowingFluid.Source(PINK_LIQUID_FLUID_PROPERTIES))
  val FLOWING_PINK_LIQUID: RegistryObject[FlowingFluid]  = FLUIDS.register("flowing_pink_liquid",
    () => new ForgeFlowingFluid.Flowing(PINK_LIQUID_FLUID_PROPERTIES))
  
  val PINK_LIQUID_FLUID_PROPERTIES: ForgeFlowingFluid.Properties = new ForgeFlowingFluid.Properties(
    PinkFluid.PINK_LIQUID_FLUID_TYPE, SOURCE_PINK_LIQUID, FLOWING_PINK_LIQUID)
    .slopeFindDistance(5).levelDecreasePerBlock(1).block(PINK_LIQUID_BLOCK)
    .bucket(PINK_LIQUID_BUCKET)


  val register: IEventBus => Unit = (eventBus: IEventBus ) =>
    FLUIDS.register(eventBus)

}
