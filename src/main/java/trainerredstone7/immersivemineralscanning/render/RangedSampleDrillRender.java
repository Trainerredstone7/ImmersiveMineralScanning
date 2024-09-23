package trainerredstone7.immersivemineralscanning.render;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.RangedSampleDrillTile;

@SuppressWarnings("deprecation")
public class RangedSampleDrillRender extends TileEntitySpecialRenderer<RangedSampleDrillTile> {
	@Override
	public void render(RangedSampleDrillTile tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(tile.isDummy()||!tile.getWorld().isBlockLoaded(tile.getPos(), false))
			return;

		final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		BlockPos blockPos = tile.getPos();
		IBakedModel model = blockRenderer.getModelForState(state);
		if(state.getBlock()!=ImmersiveMineralScanning.wideRangeSampleDrillBlock)
			return;
		if(state instanceof IExtendedBlockState)
			state = ((IExtendedBlockState)state).withProperty(Properties.AnimationProperty, new OBJState(Lists.newArrayList("drill"), true));
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldRenderer = tessellator.getBuffer();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		if(Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(7425);
		else
			GlStateManager.shadeModel(7424);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+.5, y+.5, z+.5);
		
		if(tile.drillExtension != 0 || tile.drillRotation%4 != 0) {
			GlStateManager.rotate(((tile.drillRotation+partialTicks)*22.5f)%360f, 0, 1, 0);
		}
		if (tile.drillExtension != 0) {
			GlStateManager.translate(0, -1.4*tile.drillExtension/100.0, 0);
		}
		
		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		worldRenderer.setTranslation(-.5-blockPos.getX(), -.5-blockPos.getY(), -.5-blockPos.getZ());
		worldRenderer.color(255, 255, 255, 255);
		blockRenderer.getBlockModelRenderer().renderModel(tile.getWorld(), model, state, tile.getPos(), worldRenderer, true);
		worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}
}
