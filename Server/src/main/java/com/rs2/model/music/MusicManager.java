package com.rs2.model.music;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.music.MusicAreaDefinition;
import com.rs2.model.music.MusicTrackDefinition;
import com.rs2.model.player.Player;
import com.rs2.ServerSettings;
import com.rs2.net.packet.AudioIds443;
import com.rs2.cache.js5.Interfaces;
import com.rs2.util.CharacterFileManager;
import com.rs2.util.GameUtil;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.StandardCharsets;

public final class MusicManager {
    private static List buttonlessTrackIds = Arrays.asList(648, 649, 650, 651, 652);
    private int currentAreaId = -1;
    private int currentTrackId = -1;

    public static void setAutomaticMode(Player player, boolean enabled) {
        player.automaticMusicEnabled = enabled;
        player.configStates[18] = enabled ? 1 : 0;
        player.packetSender.sendConfig(18, player.configStates[18]);
    }

    public final void updateForPlayerPosition(Player player) {
        int playerX = player.getPosition().getX();
        int playerY = player.getPosition().getY();
        int[] areaIds = new int[5];
        int areaIdCount = 0;
        int areaId = 0;
        while (areaId < MusicAreaDefinition.areaCount) {
            boolean inArea = false;
            MusicAreaDefinition musicAreaDefinition = MusicAreaDefinition.forAreaId(areaId);
            if (musicAreaDefinition.getRegionCount() == 0) {
                inArea = musicAreaDefinition.getAreaBounds().contains(new Position(playerX, playerY, 0));
            } else {
                int regionId = GameUtil.getRegionId(playerX, playerY);
                int[] regionIds = musicAreaDefinition.getRegionIds();
                int index = 0;
                while (index < musicAreaDefinition.getRegionCount()) {
                    if (regionId == regionIds[index]) {
                        inArea = true;
                        break;
                    }
                    ++index;
                }
            }
            if (inArea) {
                areaIds[areaIdCount] = areaId;
                ++areaIdCount;
            }
            ++areaId;
        }
        int bestPriority = 0;
        int bestAreaId = -1;
        int index2 = 0;
        while (index2 < areaIdCount) {
            MusicAreaDefinition musicAreaDefinition = MusicAreaDefinition.forAreaId(areaIds[index2]);
            if (index2 == 0) {
                bestPriority = musicAreaDefinition.getPriority();
                bestAreaId = musicAreaDefinition.getAreaId();
            } else if (bestPriority < musicAreaDefinition.getPriority()) {
                bestPriority = musicAreaDefinition.getPriority();
                bestAreaId = musicAreaDefinition.getAreaId();
            }
            ++index2;
        }
        this.currentAreaId = bestAreaId;
        int trackId;
        if (this.currentAreaId != -1) {
            MusicAreaDefinition musicAreaDefinition = MusicAreaDefinition.forAreaId(this.currentAreaId);
            int areaTrackId = musicAreaDefinition.getTrackId();
            if (player.automaticMusicEnabled) {
                this.currentTrackId = areaTrackId;
            }
            MusicTrackDefinition musicTrackDefinition = MusicTrackDefinition.forTrackId(areaTrackId);
            int unlockConfigId = musicTrackDefinition.getUnlockConfigId();
            int unlockBitMask = musicTrackDefinition.getUnlockBitMask();
            if (unlockConfigId != -1 && (player.configStates[unlockConfigId] & unlockBitMask) == 0) {
                setAutomaticMode(player, true);
                player.configStates[unlockConfigId] = player.configStates[unlockConfigId] + unlockBitMask;
                player.packetSender.sendConfig(unlockConfigId, player.configStates[unlockConfigId]);
                player.packetSender.sendGameMessage("@red@You have unlocked a new music track: " + musicTrackDefinition.getName());
                this.currentTrackId = areaTrackId;
            }
            trackId = this.currentTrackId;
        } else {
            trackId = -1;
        }
        if (trackId != -1) {
            if (trackId == player.musicManagerTrackId) {
                return;
            }
            player.musicManagerTrackId = trackId;
            MusicTrackDefinition musicTrackDefinition = MusicTrackDefinition.forTrackId(trackId);
            if (ServerSettings.clientBuild == 443 || musicTrackDefinition.getButtonId() != -1 || buttonlessTrackIds.contains(trackId)) {
                player.packetSender.sendMusicTrack(musicTrackDefinition);
                if (ServerSettings.clientBuild == 443 && AudioIds443.track(trackId) < 0
                        && AudioIds443.jingle(trackId) >= 0) {
                    player.packetSender.sendMusicJingle(trackId, 0);
                }
            }
        }
    }

    public static void unlockTrack(Player player, int trackId) {
        int value;
        MusicTrackDefinition musicTrackDefinition = MusicTrackDefinition.forTrackId(trackId);
        if (musicTrackDefinition.getUnlockConfigId() < 0) return;
        int unlockConfigId = player.configStates[musicTrackDefinition.getUnlockConfigId()];
        if ((unlockConfigId & (value = musicTrackDefinition.getUnlockBitMask())) == 0) {
            int value2;
            int unlockConfigId2 = value2 = musicTrackDefinition.getUnlockConfigId();
            player.configStates[unlockConfigId2] = player.configStates[unlockConfigId2] + value;
            Player player2 = player;
            player2.packetSender.sendConfig(value2, player.configStates[value2]);
        }
    }

    public static void unlockAllTracks(Player player) {
        int index = 0;
        while (index < MusicTrackDefinition.trackCount) {
            int value;
            int value2;
            MusicTrackDefinition musicTrackDefinition = MusicTrackDefinition.forTrackId(index);
            if (musicTrackDefinition.getUnlockConfigId() != -1 && ((value2 = player.configStates[musicTrackDefinition.getUnlockConfigId()]) & (value = musicTrackDefinition.getUnlockBitMask())) == 0) {
                int value3;
                int unlockConfigId = value3 = musicTrackDefinition.getUnlockConfigId();
                player.configStates[unlockConfigId] = player.configStates[unlockConfigId] + value;
            }
            ++index;
        }
        index = 0;
        while (index < CharacterFileManager.musicUnlockConfigIds.length) {
            int value4 = CharacterFileManager.musicUnlockConfigIds[index];
            Player player2 = player;
            player2.packetSender.sendConfig(value4, player.configStates[value4]);
            ++index;
        }
        if (ServerSettings.clientBuild == 443) Music.unlockAll(player);
    }

    public static boolean isTrackUnlocked(Player player, int trackId) {
        int value;
        MusicTrackDefinition musicTrackDefinition = MusicTrackDefinition.forTrackId(trackId);
        if (musicTrackDefinition.getUnlockConfigId() < 0) return false;
        int unlockConfigId = player.configStates[musicTrackDefinition.getUnlockConfigId()];
        return (unlockConfigId & (value = musicTrackDefinition.getUnlockBitMask())) != 0 && value != -1;
    }

    public static boolean playManualTrack(Player player, int trackId) {
        if (ServerSettings.clientBuild == 443) {
            for (java.util.Map.Entry<Integer, Music.Track> entry : Music.tracks().entrySet()) {
                if (entry.getValue().legacyId == trackId) {
                    Music.Track nativeTrack = entry.getValue();
                    if (!nativeTrack.isUnlocked(player) || nativeTrack.assetId < 0) {
                        Music.play(player, entry.getKey());
                        return false;
                    }
                    return Music.play(player, entry.getKey());
                }
            }
        }
        MusicTrackDefinition track = MusicTrackDefinition.forTrackId(trackId);
        if (track.getUnlockConfigId() < 0 || !isTrackUnlocked(player, trackId)) {
            player.packetSender.sendGameMessage("You haven't unlocked that song yet.");
            return false;
        }
        player.packetSender.sendMusicTrack(track);
        player.musicManagerTrackId = trackId;
        setAutomaticMode(player, false);
        return true;
    }

    public static int trackIdForRevision443Child(int child) {
        Music.Track track = Music.tracks().get(child);
        if (track != null) return track.legacyId;
        Interfaces.Component component = Interfaces.forId(239, child);
        if (component == null || component.type != 4 || component.actionType != 1) {
            return -1;
        }
        // Song labels are NUL-terminated in the stock music tab. Match names
        // from Songs.dat instead of assuming child IDs follow legacy order.
        String data = new String(component.getData(), StandardCharsets.ISO_8859_1);
        for (int trackId = 0; trackId < MusicTrackDefinition.trackCount; trackId++) {
            String name = MusicTrackDefinition.forTrackId(trackId).getName();
            if (name.length() > 1 && data.contains(name + "\0")) {
                return trackId;
            }
        }
        return -1;
    }
}

