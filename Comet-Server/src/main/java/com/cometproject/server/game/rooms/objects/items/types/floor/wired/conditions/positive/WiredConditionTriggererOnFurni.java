package com.cometproject.server.game.rooms.objects.items.types.floor.wired.conditions.positive;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredConditionItem;
import com.cometproject.server.game.rooms.types.RoomInstance;


public class WiredConditionTriggererOnFurni extends WiredConditionItem {
    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredConditionTriggererOnFurni(int id, int itemId, RoomInstance room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean evaluate(GenericEntity entity, Object data) {
        if (entity == null) return false;

        boolean isOnFurni = false;

        for (int itemId : this.getWiredData().getSelectedIds()) {
            for (RoomItemFloor itemOnEntity : entity.getRoom().getItems().getItemsOnSquare(entity.getPosition().getX(), entity.getPosition().getY())) {
                if (itemOnEntity.getId() == itemId) isOnFurni = true;
            }
        }

        return isNegative ? !isOnFurni : isOnFurni;
    }


    @Override
    public int getInterface() {
        return 8;
    }
}