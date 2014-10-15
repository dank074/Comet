package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Random;

public class DiceFloorItem extends RoomItemFloor {
    private boolean isInUse = false;

    public DiceFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if(!isWiredTrigger) {
            if (!this.getPosition().touching(entity)) {
                entity.moveTo(this.getPosition().squareInFront(this.rotation).getX(), this.getPosition().squareBehind(this.rotation).getY());
                return;
            }
        }

        if (this.isInUse) {
            return;
        }

        if (requestData >= 0) {
            if (!"-1".equals(this.getExtraData())) {
                this.setExtraData("-1");
                this.sendUpdate();

                this.isInUse = true;

                this.setTicks(RoomItemFactory.getProcessTime(2.5));
            }
        } else {
            this.setExtraData("0");
            this.sendUpdate();
        }

    }

    @Override
    public void onPlaced() {
        if (!"0".equals(this.getExtraData())) {
            this.setExtraData("0");
        }
    }

    @Override
    public void onPickup() {
        this.cancelTicks();
    }

    @Override
    public void onTickComplete() {
        int num = new Random().nextInt(6) + 1;

        this.setExtraData(Integer.toString(num));
        this.sendUpdate();
        this.saveData();

        this.isInUse = false;
    }
}
