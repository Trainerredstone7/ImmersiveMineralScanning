package trainerredstone7.immersivemineralscanning.gui;

import java.io.IOException;
import java.util.stream.Stream;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.client.gui.elements.GuiReactiveList;
import flaxbeard.immersivepetroleum.api.crafting.PumpjackHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.RangedSampleDrillTile;
import trainerredstone7.immersivemineralscanning.network.SearchTargetUpdatePacket;

/**
 * 
 * @author trainerredstone7
 *
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
	
	
	
	public RangedSampleDrillGui(RangedSampleDrillTile tile) {
		this.tile = tile;
		currentTarget = tile.searchTarget;
		ExcavatorHandler.mineralList.keySet().stream().filter(p -> p.validDimension(tile.getWorld().provider.getDimension())).map(r -> r.name);
		Stream<String> mineralNamesStream = ExcavatorHandler.mineralList.keySet().stream()
				.filter(p -> p.validDimension(tile.getWorld().provider.getDimension()))
				.map(m -> m.name);
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
		super.initGui();
		guiLeft = (width-X_SIZE)/2;
		guiTop = (height-Y_SIZE)/2;
		buttonList.add(new GuiReactiveList(this, 0, guiLeft, guiTop, LIST_WIDTH, LIST_HEIGHT, mineralNames));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0 && mineralNames[((GuiReactiveList) button).selectedOption] != currentTarget) {
			currentTarget = mineralNames[((GuiReactiveList) button).selectedOption];
			tile.searchTarget = currentTarget;
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
		fontRenderer.drawString("Current target:", (width - fontRenderer.getStringWidth("Current target:"))/2, guiTop + LIST_HEIGHT + 10, 0xFFFFFFFF);
		fontRenderer.drawString(currentTarget, (width - fontRenderer.getStringWidth(currentTarget))/2, guiTop + LIST_HEIGHT + 10 + fontRenderer.FONT_HEIGHT, 0xFFFFFFFF);
	}
}
