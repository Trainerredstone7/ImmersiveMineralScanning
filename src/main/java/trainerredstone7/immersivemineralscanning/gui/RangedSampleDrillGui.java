package trainerredstone7.immersivemineralscanning.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.client.gui.elements.GuiReactiveList;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.RangedSampleDrillTile;
import trainerredstone7.immersivemineralscanning.network.SearchTargetUpdatePacket;
import net.minecraftforge.fml.common.Optional;

/**
 * 
 * @author trainerredstone7
 *
 * Will not need an IGuiHandler as no container is needed
 * This info might help
 * https://jabelarminecraft.blogspot.com/p/minecraft-forge-1721710-gui-and-input.html
 * 
 * Will need to send packet to server whenever a setting changes in the GUI
 * This can be done in actionPerformed
 * (Should a packet be sent from server to client if someone else changes a setting? totally unnecessary but might be nice)
 */
public class RangedSampleDrillGui extends GuiScreen {
	
	RangedSampleDrillTile tile;
	GuiReactiveList resourceList;
	private final int X_SIZE = 80; //176;
	private final int Y_SIZE = 60; //166;
	private final int LIST_WIDTH = 80;
	private final int LIST_HEIGHT = 60;
	private String[] mineralNames;
	private String currentTarget;
	private int guiLeft;
	private int guiTop;
	
	
	
	//Consider sending just the important info rather than the entire tile
	//Make sure to save position and dim as well for later packet sending
	public RangedSampleDrillGui(RangedSampleDrillTile tile) {
		this.tile = tile;
		currentTarget = tile.searchTarget;
		ExcavatorHandler.mineralList.keySet().stream().filter(p -> p.validDimension(tile.getWorld().provider.getDimension())).map(r -> r.name);
		Stream<String> mineralNamesStream = ExcavatorHandler.mineralList.keySet().stream()
				.filter(p -> p.validDimension(tile.getWorld().provider.getDimension()))
				.map(m -> m.name);
		//TODO uncomment and fix
		if (ImmersiveMineralScanning.immersivePetroleumPresent) {
			mineralNamesStream = Stream.concat(mineralNamesStream, PumpjackHandler.reservoirList.keySet().stream()
					.filter(p -> p.validDimension(tile.getWorld().provider.getDimension()))
					.map(r -> r.name));
		}
		mineralNames = mineralNamesStream
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.toArray(String[]::new);
	}

	
	
	@Override
	public void initGui() {
		// TODO Auto-generated method stub
		super.initGui();
		guiLeft = (width-X_SIZE)/2;
		guiTop = (height-Y_SIZE)/2;
		buttonList.add(new GuiReactiveList(this, 0, guiLeft, guiTop, LIST_WIDTH, LIST_HEIGHT, mineralNames));
	}



	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0 && mineralNames[((GuiReactiveList) button).selectedOption] != currentTarget) {
			currentTarget = mineralNames[((GuiReactiveList) button).selectedOption];
			ImmersiveMineralScanning.PACKET_HANDLER.sendToServer(new SearchTargetUpdatePacket(
					tile.getPos(), tile.getWorld().provider.getDimension(), currentTarget));
		}
	}



	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		//TODO: add something to draw the currentTarget string
	}
}
