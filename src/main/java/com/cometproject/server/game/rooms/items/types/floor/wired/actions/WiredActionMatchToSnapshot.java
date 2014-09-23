package com.cometproject.server.game.rooms.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.misc.Position3D;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.types.floor.wired.WiredItemSnapshot;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.google.common.collect.Lists;

import java.util.List;

public class WiredActionMatchToSnapshot extends WiredActionItem implements WiredItemSnapshot.Refreshable {
    private static final int PARAM_MATCH_STATE = 0;
    private static final int PARAM_MATCH_ROTATION = 1;
    private static final int PARAM_MATCH_POSITION = 2;

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param roomId   The ID of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionMatchToSnapshot(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 3;
    }

    @Override
    public boolean evaluate(GenericEntity entity, Object data) {
        if(this.hasTicks()) return false;

        if(this.getWiredData().getDelay() >= 1) {
            this.setTicks(this.getWiredData().getDelay());
        } else {
            this.onTickComplete();
        }

        return true;
    }

    @Override
    public void onTickComplete() {
        if(this.getWiredData().getSnapshots().size() == 0) {
            return;
        }

        final boolean matchState = this.getWiredData().getParams().get(PARAM_MATCH_STATE) == 1;
        final boolean matchRotation = this.getWiredData().getParams().get(PARAM_MATCH_ROTATION) == 1;
        final boolean matchPosition = this.getWiredData().getParams().get(PARAM_MATCH_POSITION) == 1;

        for(int itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);
            if (floorItem == null) continue;

            WiredItemSnapshot itemSnapshot = this.getWiredData().getSnapshots().get(itemId);
            if(itemSnapshot == null) continue;

            if(matchState) {
                floorItem.setExtraData(itemSnapshot.getExtraData());
            }

            if(matchPosition || matchRotation) {
                Position3D currentPosition = new Position3D(floorItem.getX(), floorItem.getY(), floorItem.getHeight());
                Position3D newPosition = new Position3D(itemSnapshot.getX(), itemSnapshot.getY());

                if(this.getRoom().getItems().moveFloorItem(floorItem.getId(), !matchPosition ? currentPosition : newPosition, matchRotation ? itemSnapshot.getRotation() : floorItem.getRotation(), true)) {
                    newPosition.setZ(floorItem.getHeight());

                    if(!matchRotation)
                        this.getRoom().getEntities().broadcastMessage(SlideObjectBundleMessageComposer.compose(currentPosition, newPosition, 0, 0, floorItem.getId()));
                }
            }

            if(matchRotation)
                this.getRoom().getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(floorItem, this.getRoom().getData().getOwnerId()));

            floorItem.sendUpdate();
        }
    }

    @Override
    public void refreshSnapshots() {
        List<Integer> toRemove = Lists.newArrayList();
        this.getWiredData().getSnapshots().clear();

        for(int itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if(floorItem == null) {
                toRemove.add(itemId);
                continue;
            }

            this.getWiredData().getSnapshots().put(itemId, new WiredItemSnapshot(floorItem));
        }

        for(Integer itemToRemove : toRemove) {
            this.getWiredData().getSelectedIds().remove(itemToRemove);
        }

        this.save();
    }
}
