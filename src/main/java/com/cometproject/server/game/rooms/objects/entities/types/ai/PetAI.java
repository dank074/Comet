package com.cometproject.server.game.rooms.objects.entities.types.ai;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;


public class PetAI extends AbstractBotAI {
    private PetEntity entity;

    public PetAI(GenericEntity entity) {
        super(entity);
    }

    @Override
    public boolean onTalk(PlayerEntity entity, String message) {
        return false;
        /*String command = message.split(" ")[1];

        if(command.equals(PetCommands.FREE)) {

        } else if(command.equals(PetCommands.COME)) {

        } else if(command.equals(PetCommands.DEAD)) {

        } else if(command.equals(PetCommands.JUMP)) {

        } else if(command.equals(PetCommands.SIT)) {

        } else if(command.equals(PetCommands.LAY)) {

        } else if(command.equals(PetCommands.SLEEP)) {

        }
        return false;*/
    }

}
