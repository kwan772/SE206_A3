import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileController {

    public static boolean containsWord(String fileName, String word) {
        try {
            BufferedReader readerBuffered = new BufferedReader(new FileReader(fileName));
            String currentLine;
            while ((currentLine = readerBuffered.readLine()) != null) {
                if (currentLine.contains(word)) {
                    readerBuffered.close();
                    return true;
                }
            }
            readerBuffered.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void removeFromList(String fileName, String word) {
        if (containsWord(fileName, word)) {
            String cmd = "sed -i \"/" + word + "/d\" " + fileName;
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            try {
                Process remove = builder.start();
                remove.waitFor();
            }
            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToList(String fileName, String word) {
        if (!containsWord(fileName, word)) {
            try { 
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
                bufferedWriter.write(word);
                bufferedWriter.newLine();
                bufferedWriter.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int countLines(String fileName) {
        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while (reader.readLine() != null) {
                count++;
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void incrementStats(String word, int column) {
        // Column 0 = Mastered, Column 1 = Faulted, Column 2 = Failed
        
        // Add word to stats if it's not already there
        if (!containsWord(".gameStats", word)) {
            try { 
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(".gameStats", true));
                bufferedWriter.write(word + " 0 0 0");
                bufferedWriter.newLine();
                bufferedWriter.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Pull the current data for the word from the stats column
        String statsString = null;
        try {
            BufferedReader readerBuffered = new BufferedReader(new FileReader(".gameStats"));
            String currentLine;
            while ((currentLine = readerBuffered.readLine()) != null) {
                if (currentLine.contains(word)) {
                    statsString = currentLine;
                }
            }
            readerBuffered.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // Increment the stat in the correct column
        String[] statsData = statsString.split(" ");
        statsData[column+1] = Integer.toString(Integer.parseInt(statsData[column+1])+1);
        statsString = word + " " + statsData[1] + " " + statsData[2] + " " + statsData[3];

        // Rewrite the new line into the file
        removeFromList(".gameStats", word);
        try { 
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(".gameStats", true));
            bufferedWriter.write(statsString);
            bufferedWriter.newLine();
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void wipeFile(String fileName) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false));
            bufferedWriter.write("");
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createGameFiles() {
        File gameStats = new File(".gameStats");
        File failedWords = new File("words/.failedWords");
        try {
            gameStats.createNewFile();
            failedWords.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
