package com.cometsrv.network.messages.incoming.room.item;

import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ExchangeItemMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if(client.getPlayer().getEntity() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        FloorItem item = room.getItems().getFloorItem(itemId);

        if(item == null) {
            return;
        }

        int value = Integer.parseInt(item.getDefinition().getItemName().split("_")[1]);

        room.getItems().removeItem(item, client, false);
        client.getPlayer().getData().increaseCredits(value);
        client.getPlayer().sendBalance();
    }
}
