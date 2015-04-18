package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.TypingStatusMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class StopTypingMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null)
            return;

        client.getPlayer().getEntity().unIdle();
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new TypingStatusMessageComposer(client.getPlayer().getEntity().getId(), 0));
    }
}