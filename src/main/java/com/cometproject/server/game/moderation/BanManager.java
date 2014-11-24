package com.cometproject.server.game.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.types.Ban;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.storage.queries.moderation.BanDao;
import com.google.common.collect.Lists;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class BanManager {
    private Map<String, Ban> bans;

    Logger logger = Logger.getLogger(BanManager.class.getName());

    public BanManager() {
        loadBans();
    }

    public void loadBans() {
        if (this.bans != null)
            this.bans.clear();

        try {
            this.bans = BanDao.getActiveBans();
            logger.info("Loaded " + this.bans.size() + " bans");
        } catch (Exception e) {
            logger.error("Error while loading bans", e);
        }
    }

    public void tick() {
        List<Ban> bansToRemove = Lists.newArrayList();

        for(Ban ban : this.bans.values()) {
            if(ban.getExpire() != 0 && Comet.getTime() >= ban.getExpire()) {
                bansToRemove.add(ban);
            }
        }

        if(bansToRemove.size() != 0) {
            for(Ban ban : bansToRemove) {
                this.bans.remove(ban.getData());
            }
        }

        bansToRemove.clear();
    }

    public void add(Ban ban) {
        this.bans.put(ban.getData(), ban);
    }

    public boolean hasBan(String data, BanType type) {
        if(this.bans.containsKey(data)) {
            Ban ban = this.bans.get(data);

            if(ban != null && ban.getType() == type) {
                if(ban.getExpire() != 0 && Comet.getTime() >= ban.getExpire()) {
                    return false;
                }
                
                return true;
            }
        }

        return false;
    }

    public Ban get(String data) {
        return this.bans.get(data);
    }
}
