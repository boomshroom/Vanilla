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
package org.spout.vanilla.protocol.rcon.handler;

import java.util.HashMap;
import java.util.Map;

import org.spout.vanilla.protocol.rcon.msg.AuthMessage;
import org.spout.vanilla.protocol.rcon.msg.CommandMessage;
import org.spout.vanilla.protocol.rcon.msg.CommandResponseMessage;
import org.spout.vanilla.protocol.rcon.msg.RconMessage;

/**
 * Handler lookup for the rcon protocol
 */
public class RconHandlerLookupService {
	private final Map<Class<? extends RconMessage>, RconMessageHandler<?>> handlers = new HashMap<Class<? extends RconMessage>, RconMessageHandler<?>>();

	public RconHandlerLookupService() {
		try {
			bind(AuthMessage.class, new AuthMessageHandler());
			bind(CommandMessage.class, new CommandMessageHandler());
			bind(CommandResponseMessage.class, new CommandResponseMessageHandler());
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}

	protected <T extends RconMessage> void bind(Class<T> messageClass, RconMessageHandler<T> handler) {
		handlers.put(messageClass, handler);
	}

	@SuppressWarnings("unchecked")
	public <T extends RconMessage> RconMessageHandler<T> find(Class<T> message) {
		return (RconMessageHandler<T>) handlers.get(message);
	}
}
