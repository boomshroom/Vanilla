/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.generator.normal;

import java.util.ArrayList;
import java.util.Random;

import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGenerator;
import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.VanillaBlocks;

public class NormalGenerator implements WorldGenerator {
	int seed = 42;
	Perlin layerCount = new Perlin();
	ArrayList<Perlin> layers = new ArrayList<Perlin>();
	
	
	public NormalGenerator() {
		layerCount.setSeed(seed);
		layerCount.setOctaveCount(5);
	}

	public Perlin getLayer(int layer) {
		if(layer >= 0) {
			if(layer < layers.size()) {
				return layers.get(layer);
			} else {
				Perlin p = new Perlin();
				p.setSeed(seed + layer);
				p.setOctaveCount(5);
				layers.add(p);
				return p;
			}
		} else {
			return null;
		}
	}
	
	private final Populator[] populators = new Populator[]{new TreePopulator(), new PondPopulator(), new StrongholdPopulator(), new VillagePopulator(), new AbandonedMineshaftPopulator(), new DungeonPopulator()};
	
	public Populator[] getPopulators() {
		return populators;
	}

	public void generate(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ) {
		int x = chunkX * 16;
		int y = chunkY * 16;
		int z = chunkZ * 16;
		
		if (y > 127) {
			blockData.flood((short)0);
			//return;
		}
		if (chunkY < 0) {
			blockData.flood(VanillaBlocks.bedrock.getId());
			//return;
		}

		for (int dx = x; dx < (x+16); dx++) {
			for (int dz = z; dz < (z+16); dz++) {
				int layers = (int) ((layerCount.GetValue(dx / 16.0 + 0.05, 0.05, dz / 16.0 + 0.05) + 1.0) * 10.0 + 2);
				
				if(layers <= 0) {
					layers = 2;
				}
				
				int heightPerLayer = 128/layers;
				
				for (int layer = 0; layer < layers; layer+=2) {
					Perlin bottom = getLayer(layer);
					Perlin top = getLayer(layer+1);
					int min = heightPerLayer * layer;
					int max = heightPerLayer * (layer + 1);
					int b = (int) ((getPerlinValueXZ(bottom, dx, dz) + 1.0) * heightPerLayer / 3.0 + min);
					int t = (int) ((getPerlinValueXZ(top, dx, dz) + 1.0) * heightPerLayer / 3.0 + max);
					if(b < t) {
						for(int dy = y; dy < y + 16; dy++) {
							short id = VanillaBlocks.air.getId();
							if(dy <= b || dy >= t) {
								id = VanillaBlocks.stone.getId();
							}
							
							if(dy < 66 && dy > 62 && dx < 16 && dx > -16 && dz < 16 && dz > -16) {
								id = 0;
							}
							blockData.set(dx, dy, dz, id);
						}
					}
				}
				
			}
		}
	}
	
	public static double getPerlinValueXZ(Perlin perlin, int x, int z) {
		return perlin.GetValue(x / 16.0 + 0.05, 0.05, z / 16.0 + 0.05);
	}
 
	private short getBlockId(int top, int dy) {
		short id;
		if(dy > top && dy > 64) {
			id = VanillaBlocks.air.getId();
		} else if (dy <= 64 && dy > top) {
			id = VanillaBlocks.water.getId();
		} else if(dy == (int)top && dy >= 64) {
			id = VanillaBlocks.grass.getId();
		} else if(dy + 4 >=(int)top) {
			id = VanillaBlocks.dirt.getId();
		} else if(dy != 0){
			id = VanillaBlocks.stone.getId();
		} else {
			id = VanillaBlocks.bedrock.getId();
		}
		return id;
	}
}