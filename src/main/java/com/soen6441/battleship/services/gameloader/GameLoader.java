package com.soen6441.battleship.services.gameloader;

import com.soen6441.battleship.data.model.GameControllerInfo;
import com.soen6441.battleship.data.model.OfflineGameInfo;

import java.io.*;

public class GameLoader {
    private static final String GAME_CONTROLLER_FILE = "gamecontroller.txt";
    private static final String OFFLINE_GAME_INFO_FILE = "gameinfo.txt";
    private String folderPrefix = "";

    public GameLoader() {
    }

    public GameLoader(String folderPrefix) {
        this.folderPrefix = folderPrefix;

        if (this.folderPrefix != null && !this.folderPrefix.isEmpty()) {
            if (!this.folderPrefix.endsWith("/")) {
                this.folderPrefix += "/";
            }

            File prefixFolder = new File(this.folderPrefix);
            prefixFolder.mkdir();
        }

        if (this.folderPrefix == null) {
            this.folderPrefix = "";
        }
    }

    public void saveGame(GameControllerInfo gameInfo) {
        storeAsSerializable(gameInfo);
    }

    public GameControllerInfo readSavedGame() {
        return readOfflineFile();
    }

    public boolean doesFileExist() {
        File file = new File(folderPrefix + "output", GAME_CONTROLLER_FILE);
        return file.exists();
    }

    public void deleteFile() {
        File file = new File(folderPrefix + "output", GAME_CONTROLLER_FILE);
        file.delete();
    }

    private void storeAsSerializable(GameControllerInfo gameInfo) {
        try {
            checkAndCreateFileFolder(folderPrefix + "output", GAME_CONTROLLER_FILE);
            File file = new File(folderPrefix + "output", GAME_CONTROLLER_FILE);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gameInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkAndCreateFileFolder(String folderName, String fileName) throws IOException {
        if (folderName != null) {
            File folder = new File(folderName);

            if (!folder.exists()) {
                folder.mkdir();
            }
        }

        if (fileName != null) {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }
        }
    }

    private GameControllerInfo readOfflineFile() {
        try {
            File savedFiled = new File(this.folderPrefix + "output/" + GAME_CONTROLLER_FILE);
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream(savedFiled);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (GameControllerInfo) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveOfflineGameInfo(OfflineGameInfo offlineGameInfo) {
        try {
            checkAndCreateFileFolder(folderPrefix + "output", OFFLINE_GAME_INFO_FILE);
            File file = new File(folderPrefix + "output", OFFLINE_GAME_INFO_FILE);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(offlineGameInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OfflineGameInfo readOfflineGameInfo() {
        try {
            File savedFiled = new File(this.folderPrefix + "output/" + OFFLINE_GAME_INFO_FILE);
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream(savedFiled);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (OfflineGameInfo) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
