package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.utilities.attributes.Attributable;

import java.util.HashMap;
import java.util.Map;

public abstract class RoomItem implements RoomItemAttributes, Attributable {
    protected int id;
    protected int itemId;
    protected int ownerId;

    protected int x;
    protected int y;

    protected int rotation;

    protected int ticksTimer;

    private final Map<String, Object> attributes = new HashMap<>();

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getItemId() {
        return this.itemId;
    }

    @Override
    public int getOwner() {
        return this.ownerId;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getRotation() {
        return this.rotation;
    }

    public RoomItem() {
        this.ticksTimer = -1;
    }

    public final boolean requiresTick() {
        return this.hasTicks();
    }

    protected final boolean hasTicks() {
        return (this.ticksTimer > 0);
    }

    protected final void setTicks(int time) {
        this.ticksTimer = time;
    }

    protected final void cancelTicks() {
        this.ticksTimer = -1;
    }

    public final void tick() {
        this.onTick();

        if (this.ticksTimer > 0) {
            this.ticksTimer--;
        }

        if (this.ticksTimer == 0) {
            this.cancelTicks();
            this.onTickComplete();
        }
    }

    protected void onTick() {
        // Override this
    }

    protected void onTickComplete() {
        // Override this
    }

    public void onPlaced() {
        // Override this
    }

    public void onPickup() {
        // Override this
    }

    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        // Override this
    }

    public void onLoad() {
        // Override this
    }

    public void onUnload() {
        // Override this
    }

    public int distance(GenericEntity entity) {
        int avatarX = entity.getPosition().getX();
        int avatarY = entity.getPosition().getY();

        return DistanceCalculator.calculate(avatarX, avatarY, this.getX(), this.getY());
    }

    public boolean touching(GenericEntity entity) {
        int avatarX = entity.getPosition().getX();
        int avatarY = entity.getPosition().getY();

        return DistanceCalculator.tilesTouching(avatarX, avatarY, this.getX(), this.getY());
    }

    public Position3D squareInfront() {
        Position3D pos = new Position3D(getX(), getY(), 0);

        int posX = pos.getX();
        int posY = pos.getY();

        if (getRotation() == 0) {
            posY--;
        } else if (getRotation() == 2) {
            posX++;
        } else if (getRotation() == 4) {
            posY++;
        } else if (getRotation() == 6) {
            posX--;
        }

        pos.setX(posX);
        pos.setY(posY);

        return pos;
    }

    public Position3D squareBehind() {
        Position3D pos = new Position3D(getX(), getY(), 0);

        int posX = pos.getX();
        int posY = pos.getY();

        if (getRotation() == 0) {
            posY++;
        } else if (getRotation() == 2) {
            posX--;
        } else if (getRotation() == 4) {
            posY--;
        } else if (getRotation() == 6) {
            posX++;
        }

        pos.setX(posX);
        pos.setY(posY);

        return pos;
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {
        if (this.attributes.containsKey(attributeKey)) {
            this.attributes.replace(attributeKey, attributeValue);
        } else {
            this.attributes.put(attributeKey, attributeValue);
        }
    }

    @Override
    public Object getAttribute(String attributeKey) {
        return this.attributes.get(attributeKey);
    }

    @Override
    public boolean hasAttribute(String attributeKey) {
        return this.attributes.containsKey(attributeKey);
    }

    @Override
    public void removeAttribute(String attributeKey) {
        this.attributes.remove(attributeKey);
    }

    public abstract void serialize(Composer msg);

    public abstract ItemDefinition getDefinition();

    public abstract boolean toggleInteract(boolean state);

    public abstract void sendUpdate();

    public abstract void saveData();

    public abstract String getExtraData();

    public abstract void setExtraData(String data);

    public void dispose() {

    }
}
