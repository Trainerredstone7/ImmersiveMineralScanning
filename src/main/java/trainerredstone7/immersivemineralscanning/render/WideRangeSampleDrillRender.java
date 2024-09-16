package trainerredstone7.immersivemineralscanning.render;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntitySampleDrill;
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
import trainerredstone7.immersivemineralscanning.blocks.tileentities.WideRangeSampleDrillTile;

public class WideRangeSampleDrillRender extends TileEntitySpecialRenderer<WideRangeSampleDrillTile> {
	@Override
	public void render(WideRangeSampleDrillTile tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(tile.isDummy()||!tile.getWorld().isBlockLoaded(tile.getPos(), false))
			return;

		final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		BlockPos blockPos = tile.getPos();
		IBakedModel model = blockRenderer.getModelForState(state);
		if(state.getBlock()!=ImmersiveMineralScanning.wideRangeSampleDrillBlock)
			return;
//				.getModelFromBlockState(state, getWorld(), blockPos);
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

		//		float rot = 360*tile.rotation-(!tile.canTurn||tile.rotation==0||tile.rotation-tile.prevRotation<4?0:tile.facing.getAxis()==Axis.X?-f:f);
		//		GlStateManager.rotate(rot, 0,0,1);

//		int max = IEConfig.Machines.coredrill_time;		
//		if(tile.process > 0&&tile.process < max)
//		{
//			GlStateManager.rotate(((tile.process+partialTicks)*22.5f)%360f, 0, 1, 0);
//			float push = tile.process/(float)max;
//			if(tile.process > max/2)
//				push = 1-push;
//			GlStateManager.translate(0, -2.8f*push, 0);
//		}
		
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
