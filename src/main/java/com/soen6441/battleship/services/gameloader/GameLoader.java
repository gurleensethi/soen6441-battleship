package com.soen6441.battleship.services.gameloader;

import com.soen6441.battleship.data.model.OfflineGameInfo;

import java.io.*;

public class GameLoader {
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

    public void saveGame(OfflineGameInfo gameInfo) {
        storeAsSerializable(gameInfo);
    }

    public OfflineGameInfo readSavedGame() {
        return readOfflineFile();
    }

    private void storeAsSerializable(OfflineGameInfo gameInfo) {
        try {
            checkAndCreateFileFolder(folderPrefix + "output", "gameinfo.txt");
            File file = new File(folderPrefix + "output", "gameinfo.txt");
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

    private OfflineGameInfo readOfflineFile() {
        try {
            File savedFiled = new File(this.folderPrefix + "output/gameinfo.txt");
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
