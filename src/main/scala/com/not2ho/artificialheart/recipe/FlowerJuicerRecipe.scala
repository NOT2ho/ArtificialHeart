package com.not2ho.artificialheart.recipe

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.not2ho.artificialheart.ArtificialHeart
import net.minecraft.core.NonNullList
import net.minecraft.core.RegistryAccess
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import org.jetbrains.annotations.Nullable

import scala.jdk.CollectionConverters.*


object FlowerJuicerRecipe {
  object Type {
    val INSTANCE: Type = new FlowerJuicerRecipe.Type
    val ID: String = "flower_juicer"
  }

  class Type extends RecipeType[FlowerJuicerRecipe] {}

  object Serializer {
    val INSTANCE: Serializer = new FlowerJuicerRecipe.Serializer
    val ID: ResourceLocation = new ResourceLocation(ArtificialHeart.MOD_ID, "flower_juicer")
  }

  class Serializer extends RecipeSerializer[FlowerJuicerRecipe] {
    override def fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): FlowerJuicerRecipe = {
      val output: ItemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"))
      val ingredients: JsonArray = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients")
      val inputs: NonNullList[Ingredient] = NonNullList.withSize(1, Ingredient.EMPTY)
      for (i <- 0 until inputs.size) {
        inputs.set(i, Ingredient.fromJson(ingredients.get(i)))
      }
      println(output)
      println(ingredients)
      println(inputs)
      new FlowerJuicerRecipe(inputs, output, pRecipeId)

    }

    @Nullable override def fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): FlowerJuicerRecipe = {
      val inputs = NonNullList.withSize(pBuffer.readInt, Ingredient.EMPTY)
      for (i <- 0 until inputs.size) {
        inputs.set(i, Ingredient.fromNetwork(pBuffer))
      }
      val output = pBuffer.readItem
      new FlowerJuicerRecipe(inputs, output, pRecipeId)
    }

    override def toNetwork(pBuffer: FriendlyByteBuf, pRecipe: FlowerJuicerRecipe): Unit = {
      pBuffer.writeInt(pRecipe.inputItems.size)
      for (ingredient <- pRecipe.getIngredients.asScala) {
        ingredient.toNetwork(pBuffer)
      }
      pBuffer.writeItemStack(pRecipe.getResultItem(null), false)
    }
  }
}

class FlowerJuicerRecipe(private val inputItems: NonNullList[Ingredient], private val output: ItemStack, private val id: ResourceLocation) extends Recipe[SimpleContainer] {
  override def matches(pContainer: SimpleContainer, pLevel: Level): Boolean = {
    if (pLevel.isClientSide) return false
    inputItems.get(0).test(pContainer.getItem(1))
  }

  override def getIngredients: NonNullList[Ingredient] = inputItems

  override def assemble(pContainer: SimpleContainer, pRegistryAccess: RegistryAccess): ItemStack = output.copy

  override def canCraftInDimensions(pWidth: Int, pHeight: Int) = true

  override def getResultItem(pRegistryAccess: RegistryAccess): ItemStack = output.copy

  override def getId: ResourceLocation = id

  override def getSerializer: RecipeSerializer[_] = FlowerJuicerRecipe.Serializer.INSTANCE

  override def getType: RecipeType[_] = FlowerJuicerRecipe.Type.INSTANCE
}