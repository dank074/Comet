package com.cometproject.server.game.wired.effects;

import com.cometproject.server.game.rooms.entities.effects.UserEffect;
import com.cometproject.server.game.rooms.entities.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.game.wired.types.WiredEffect;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;
import java.util.Random;


public class MoveUserEffect extends WiredEffect {
    private Random randomGenerator = new Random();

    @Override
    public void onActivate(List<GenericEntity> entities, RoomItemFloor item) {
        WiredDataInstance data = WiredDataFactory.get(item);

        if (data.getItems().size() == 0) {
            return;
        }

        int locationItemId = data.getItems().get(randomGenerator.nextInt(data.getItems().size()));
        Room room = entities.get(0).getRoom();

        RoomItemFloor itemInstance = room.getItems().getFloorItem(locationItemId);

        if (itemInstance == null)
            return;

        Position3D position = new Position3D(itemInstance.getX(), itemInstance.getY(), itemInstance.getHeight());

        for (GenericEntity entity : entities) {
            // Teleport player to position
            entity.applyEffect(new UserEffect(4, 5));
            entity.updateAndSetPosition(position);

            entity.markNeedsUpdate();
        }
    }

    @Override
    public void onSave(Event event, RoomItemFloor item) {
        // We do need the stuff but I'm just being lazy so yeah ;-)
        event.readInt(); // don't need this
        event.readString(); // don't need this

        int itemCount = event.readInt();
        WiredDataInstance instance = WiredDataFactory.get(item);

        if (instance == null) {
            return;
        }

        instance.getItems().clear();

        for (int i = 0; i < itemCount; i++) {
            instance.addItem(event.readInt());
        }

        instance.setDelay(event.readInt());

        WiredDataFactory.save(instance);
    }
}
