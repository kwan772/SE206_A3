

public class Word {

    private String word;
    private String mastered;
    private String faulted;
    private String failed;

    public Word(String word, String mastered, String faulted, String failed) {
        this.word = word;
        this.mastered = mastered;
        this.faulted = faulted;
        this.failed = failed;
    }

    public String getWord() {
        return this.word;
    }

    public String getMastered() {
        return this.mastered;
    }

    public String getFaulted() {
        return this.faulted;
    }

    public String getFailed() {
        return this.failed;
    }

    public String getPercentCorrect() {
        return 100*Integer.parseInt(this.mastered)/(Integer.parseInt(this.mastered)+Integer.parseInt(this.faulted)+Integer.parseInt(this.failed)) + "%";
    }
}
