package io.github.fr33styler.worldrestore;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldRestore extends JavaPlugin {

    public void onEnable() {
        File dataFolder = getDataFolder();

        if (!dataFolder.isDirectory() && !dataFolder.delete()) {
            System.out.println("Failed to delete the file with the plugin's directory name!");
            return;
        }
        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            System.out.println("Failed to create the plugin's directory!");
            return;
        }

        File[] backupWorlds = dataFolder.listFiles();

        if (backupWorlds == null) {
            System.out.println("The backup worlds couldn't be listed!");
            return;
        }

        File rootDirectory = getDataFolder().getParentFile().getParentFile();

        System.out.println("Restore worlds from backup...");
        for (File backupFile : backupWorlds) {
            File worldFile = new File(rootDirectory, backupFile.getName());

            System.out.println("Restoring " + backupFile.getName() + "...");

            try {
                restoreWorld(worldFile, backupFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void restoreWorld(File worldFile, File backupFile) throws IOException {
        Path worldPath = worldFile.toPath();
        Path backupPath = backupFile.toPath();
        if (worldFile.exists()) {
            deleteFolder(worldPath);
        }
        copyFolder(backupPath, worldPath);
    }

    private void copyFolder(Path src, Path dst) throws IOException {
        Files.walk(src).forEach(source -> {
            try {
                Files.copy(source, dst);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void deleteFolder(Path folderPath) throws IOException {
        Files.walk(folderPath).sorted(Comparator.reverseOrder()).forEach(path -> path.toFile().delete());
    }

}
