package com.cometproject.api.game.rooms;

import com.cometproject.api.game.rooms.settings.RoomBanState;
import com.cometproject.api.game.rooms.settings.RoomKickState;
import com.cometproject.api.game.rooms.settings.RoomMuteState;
import com.cometproject.api.game.rooms.settings.RoomTradeState;

import java.util.Map;

public interface RoomData {

    int getId();

    String getName();

    String getDescription();

    int getOwnerId();

    String getOwner();

    RoomCategory getCategory();

    int getMaxUsers();

    String getAccess();

    String getPassword();

    int getScore();

    String[] getTags();

    Map<String, String> getDecorations();

    String getModel();

    boolean getHideWalls();

    int getWallThickness();

    int getFloorThickness();

    void setId(int id);

    void setName(String name);

    void setDescription(String description);
    
    void setOwnerId(int ownerId);

    void setOwner(String owner);

    void setCategory(int category);

    void setMaxUsers(int maxUsers);

    void setAccess(String access);

    void setPassword(String password);

    void setScore(int score);

    void setTags(String[] tags);

    void setDecorations(Map<String, String> decorations);

    void setModel(String model);

    void setHideWalls(boolean hideWalls);

    void setThicknessWall(int thicknessWall);

    void setThicknessFloor(int thicknessFloor);

    boolean getAllowWalkthrough();

    void setAllowWalkthrough(boolean allowWalkthrough);

    void setHeightmap(String heightmap);

    String getHeightmap();

    boolean isAllowPets();

    void setAllowPets(boolean allowPets);

    long getLastReferenced();

    RoomTradeState getTradeState();

    void setTradeState(RoomTradeState tradeState);

    int getBubbleMode();

    void setBubbleMode(int bubbleMode);

    int getBubbleType();

    void setBubbleType(int bubbleType);

    int getBubbleScroll();

    void setBubbleScroll(int bubbleScroll);

    int getChatDistance();

    void setChatDistance(int chatDistance);

    int getAntiFloodSettings();

    void setAntiFloodSettings(int antiFloodSettings);

    RoomMuteState getMuteState();

    void setMuteState(RoomMuteState muteState);

    RoomKickState getKickState();

    void setKickState(RoomKickState kickState);

    RoomBanState getBanState();

    void setBanState(RoomBanState banState);
}
