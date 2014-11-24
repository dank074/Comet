package com.cometproject.server.game;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.system.StatisticsDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManagement;
import org.apache.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameThread implements CometTask {
    private static Logger log = Logger.getLogger(GameThread.class.getName());

    private ScheduledFuture gameFuture;

    private boolean active = false;

    private int onlineRecord = 0;

    public GameThread(CometThreadManagement mgr) {
        int interval = Integer.parseInt(Comet.getServer().getConfig().get("comet.game.thread.interval"));
        this.gameFuture = mgr.executePeriodic(this, interval, interval, TimeUnit.MINUTES);
        this.active = true;
    }

    private int cycleCount = 0;

    @Override
    public void run() {
        try {
            if (!this.active) {
                return;
            }

            CometManager.getBans().tick();

            int usersOnline = Comet.getServer().getNetwork().getSessions().getUsersOnlineCount();

            if (usersOnline > this.onlineRecord)
                onlineRecord = usersOnline;

            if (cycleCount >= 15) {
                this.cycleRewards();
            }

            StatisticsDao.saveStatistics(usersOnline, CometManager.getRooms().getRoomInstances().size(), Comet.getBuild());
            cycleCount++;
        } catch (Exception e) {
            log.error("Error during game thread", e);
        }
    }

    private void cycleRewards() throws Exception {
        if (CometSettings.quarterlyCreditsEnabled || CometSettings.quarterlyDucketsEnabled) {
            for (Session client : Comet.getServer().getNetwork().getSessions().getSessions().values()) {
                if (client.getPlayer() == null || client.getPlayer().getData() == null) {
                    continue;
                }

                if(CometSettings.quarterlyCreditsEnabled) {
                    client.getPlayer().getData().increaseCredits(CometSettings.quarterlyCreditsAmount);
                }

                if(CometSettings.quarterlyDucketsEnabled) {
                    client.getPlayer().getData().increaseActivityPoints(CometSettings.quarterlyDucketsAmount);
                }

                client.getPlayer().sendBalance();
                client.getPlayer().getData().save();
            }
        }

        cycleCount = 0;
    }

    public void stop() {
        this.active = false;
        this.gameFuture.cancel(false);
    }

    public int getOnlineRecord() {
        return this.onlineRecord;
    }
}
