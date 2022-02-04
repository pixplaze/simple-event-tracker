package com.pixplaze.simpleeventtracker.listener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplaze.simpleeventtracker.SimpleEventTracker;
import com.pixplaze.simpleeventtracker.json.data.PlayerProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class PlayerListener implements Listener {

    private static final Gson gson = new Gson();
    private static final String filePath = SimpleEventTracker.getInstance().getDataFolder() + "/userprofile.json";
    private static final Logger logger = SimpleEventTracker.getInstance().getLogger();

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        var profile = getPlayerProfile(player);
        savePlayerProfile(profile);
    }

    private static void savePlayerProfile(PlayerProfile profile) {
        var file = new File(filePath);
        var playerProfileType = new TypeToken<ArrayList<PlayerProfile>>(){}.getType();
        var playerProfileList = new ArrayList<PlayerProfile>();
        var json = "";
        try {
            if (file.createNewFile()) { // If file created (or not existed yet)
                playerProfileList.add(profile);
                logger.warning("File was not exits, creating file...");
            } else { // Else if file already exist
                playerProfileList = gson.fromJson(readFromFile(file), playerProfileType);
                playerProfileList.add(profile);
                logger.info("File exist, applying player profile...");
            }
            json = gson.toJson(playerProfileList);
            logger.info(json);
            writeToFile(file, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PlayerProfile getPlayerProfile(Player player) {
        return new PlayerProfile(player.getUniqueId(), player.getName());
    }

    private static void writeToFile(File file, String json) throws IOException {
        FileWriter fw = new FileWriter(file);
        fw.write(json);
        fw.close();
    }

    private static String readFromFile(File file) throws IOException {
        var br = new BufferedReader(new FileReader(file));
        var sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        } br.close();
        return sb.toString();
    }
}
