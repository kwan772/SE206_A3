import java.io.IOException;

public class FestivalController {
    
    public static void speak(String word, int numTimes) {
        new Thread(() -> {
            String cmd = "echo \"" + word + "\" | festival --tts";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
            try {
                Process read = builder.start();
                read.waitFor();
                if (numTimes == 2) {
                    Process secondRead = builder.start();
                    secondRead.waitFor();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
