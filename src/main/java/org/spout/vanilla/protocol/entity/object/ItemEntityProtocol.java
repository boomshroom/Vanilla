/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.entity.object;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.protocol.Message;

import org.spout.vanilla.component.substance.Item;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.VanillaEntityProtocol;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityItemMessage;

public class ItemEntityProtocol extends VanillaEntityProtocol {
	@Override
	public List<Message> getSpawnMessages(Entity entity) {
		Item pi = entity.get(Item.class);
		if (pi == null) {
			return Collections.emptyList();
		}
		int id = entity.getId();
		int x = (int) (entity.getTransform().getPosition().getX() * 32);
		int y = (int) (entity.getTransform().getPosition().getY() * 32);
		int z = (int) (entity.getTransform().getPosition().getZ() * 32);
		int r = (int) (entity.getTransform().getYaw() * 32);
		int p = (int) (entity.getTransform().getPitch() * 32);
		if (pi.getStack().getMaterial() == null) {
			int typeId = VanillaMaterials.getMinecraftId(pi.getStack().getMaterial());
			if (typeId > 0) {
				return Arrays.<Message>asList(new EntityItemMessage(id, typeId, pi.getStack().getAmount(), pi.getStack().getData(), x, y, z, r, p, (int) pi.getHolder().getTransform().getRoll()));
			}
		}
		return Collections.emptyList();
	}
}
