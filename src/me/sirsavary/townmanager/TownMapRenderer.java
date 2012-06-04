package me.sirsavary.townmanager;

import java.util.ArrayList;

import me.sirsavary.townmanager.objects.Town;
import me.sirsavary.townmanager.objects.TownChunk;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

public class TownMapRenderer extends MapRenderer {


	private int renderIndex = 0;
	private int MAP_SECOND_DELAY = 5;

	private MapCanvas canvas;

	@Override
	public void render(MapView map, MapCanvas canvas, Player p) {

		Byte lastWildernessColor = MapPalette.BROWN;
		Boolean firstCycle = true;
		
		renderIndex++;
		if (((renderIndex % MAP_SECOND_DELAY) * 20) != 0) return;
		renderIndex = 0;

		//if (p.getItemInHand().getType() != Material.MAP)
			//return;

		this.canvas = canvas;

		TownChunk[][] chunkArray = new TownChunk[7][5];
		TownChunk[][] oldChunkArray = new TownChunk[7][5];
		Chunk playerChunk = p.getLocation().getChunk();

		int playerX = playerChunk.getX();
		int playerZ = playerChunk.getZ();

		ArrayList<TownChunk> chunks =  Main.fileManager.getChunks();
		if (chunks != null) {
			for (TownChunk tc : chunks)
				if ((tc.getX() <= (playerX + 3)) && (tc.getX() >= (playerX - 3)) && (tc.getZ() <= (playerZ + 2)) && (tc.getZ() >= (playerZ - 2)))
					chunkArray[(playerX - tc.getX()) + 3][(playerZ - tc.getZ()) + 2] = tc;
		}
		
		canvas.drawText(1, 9, MinecraftFont.Font, "Wilderness is gray");
		canvas.drawText(1, 19, MinecraftFont.Font, "Your town is green");
		canvas.drawText(1, 29, MinecraftFont.Font, "Other towns are blue");

		for (int x = 0; x < 7; x++) {
			for (int y = 0; y < 5; y++) {
					
				Byte color = null;
				TownChunk tc = chunkArray[x][y];
				TownChunk otc = oldChunkArray[x][y];
				Town t = null;
				
				if (tc == otc && firstCycle == false) return;

					if (tc == null) //No townchunk, aka wilderness
					{
						if (lastWildernessColor == MapPalette.DARK_GRAY) {
							color = MapPalette.GRAY_1; 
							lastWildernessColor = MapPalette.GRAY_1;
						}
						else {
							color = MapPalette.DARK_GRAY; 
							lastWildernessColor = MapPalette.DARK_GRAY;
						}
					}
					else {
						Town playerTown = Main.fileManager.getPlayerTown(p);
						t = Main.fileManager.getTown(tc.getTown());
						if ((playerTown != null) && (t == playerTown)) color = MapPalette.LIGHT_GREEN;//TownChunk belongs to players town
						else color = MapPalette.BLUE; //Other Town
						if (lastWildernessColor == MapPalette.DARK_GRAY) {
							lastWildernessColor = MapPalette.GRAY_1;
						}
						else {
							lastWildernessColor = MapPalette.DARK_GRAY;
						}
					}

					setCanvasSquare(color, (x * 18) + 1, 109 - y * 18, 18);
					MapCursorCollection cursors = canvas.getCursors();
					for (int i = 0; i < cursors.size(); i++) {
						cursors.removeCursor(cursors.getCursor(i));
					}
					
					//MapCursor mc = new MapCursor((byte) 0, (byte) 36, (byte) 15, (byte) 0, true);
					//cursors.addCursor(mc);
					oldChunkArray[x][y] = chunkArray[x][y];
				}
			}
		firstCycle = false;
		//oldChunkArray = chunkArray;
	}

	private void setCanvasSquare(byte color , int x , int z , int size){
		for(int t = x; t < (x + size); t++)
			for(int ta = z; ta < (z + size); ta++)
					canvas.setPixel(t , ta, color);
	}
	
	public static void CreateMap(Player player) {
		ItemStack is = new ItemStack(Material.MAP);
		
		int mapNumber;
		try {
			mapNumber = Main.fileManager.getPlayerMap(player.getName());
		} catch (NullPointerException e) {
			mapNumber = (int) Main.server.createMap(player.getWorld()).getId();
			Main.fileManager.setPlayerMap(player.getName(), mapNumber);
		}
		
		is.setDurability((short) mapNumber);
		player.setItemInHand(is);
		
		TownMapRenderer.drawToMap(Main.server.getMap((short) mapNumber));
	}

	public static void drawToMap(MapView map){
		for (MapRenderer renderer : map.getRenderers())
			map.removeRenderer(renderer);
		map.addRenderer(new TownMapRenderer());
	}
}
