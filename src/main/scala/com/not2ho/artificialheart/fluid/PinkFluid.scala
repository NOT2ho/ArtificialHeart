package com.not2ho.artificialheart.fluid

import com.not2ho.artificialheart
import com.not2ho.artificialheart.ArtificialHeart
import org.joml.Vector3f
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraftforge.common.SoundAction
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object PinkFluid {
	val WATER_STILL_RL: ResourceLocation = new ResourceLocation("block/water_still")
	val WATER_FLOWING_RL: ResourceLocation  = new ResourceLocation("block/water_flow")
	val PINK_OVERLAY_RL: ResourceLocation  = new ResourceLocation(ArtificialHeart.MOD_ID, "misc/in_pink")

	val FLUID_TYPES: DeferredRegister[FluidType]  = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ArtificialHeart.MOD_ID)
	val PINK_LIQUID_FLUID_TYPE: RegistryObject[FluidType]  = register("pink_fluid"
					,FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
									SoundEvents.HONEY_DRINK))
	
	private def register(name: String , properties: FluidType.Properties ): RegistryObject[FluidType] = {
		FLUID_TYPES.register (name, () => new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, PINK_OVERLAY_RL,
							0xA1ff0066, new Vector3f(255f / 255f, 0f / 255f, 102f / 255f), properties))
	}

	def register(eventBus: IEventBus ): Unit = {
			FLUID_TYPES.register(eventBus)
	}
}