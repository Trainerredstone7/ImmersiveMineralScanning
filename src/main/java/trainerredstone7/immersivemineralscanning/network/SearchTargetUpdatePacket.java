package trainerredstone7.immersivemineralscanning.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import trainerredstone7.immersivemineralscanning.ImmersiveMineralScanning;
import trainerredstone7.immersivemineralscanning.blocks.tileentities.RangedSampleDrillTile;

public class SearchTargetUpdatePacket implements IMessage {
	private BlockPos pos;
	private int dim;
	private String searchTarget;
	
	public SearchTargetUpdatePacket() {}
	
	public SearchTargetUpdatePacket(BlockPos pos, int dim, String searchTarget) {
		this.pos = pos;
		this.dim = dim;
		this.searchTarget = searchTarget;
	}
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX()).writeInt(pos.getY()).writeInt(pos.getZ()).writeInt(dim);
		ByteBufUtils.writeUTF8String(buf, searchTarget);
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		dim = buf.readInt();
		searchTarget = ByteBufUtils.readUTF8String(buf);
	}
	
	public static class ServerHandler implements IMessageHandler<SearchTargetUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(SearchTargetUpdatePacket message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				TileEntity tile = DimensionManager.getWorld(message.dim).getTileEntity(message.pos);
				if (tile instanceof RangedSampleDrillTile && !((RangedSampleDrillTile) tile).active) {
					((RangedSampleDrillTile) tile).searchTarget = message.searchTarget;
					((RangedSampleDrillTile) tile).searchingForReservoir = ImmersiveMineralScanning.instance.resourceTypeMap.getOrDefault(message.searchTarget, false);
					
				}
			});
			return null;
		}
		
	}
}
