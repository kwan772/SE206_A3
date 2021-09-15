import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Quiz {
    
    private BorderPane root;
    private List<String> quizWords = new ArrayList<String>();
    private int correctNum;
    private int gameType;

    public Quiz(int quizType) {
        // type = 0 for new spelling quiz, type = 1 for review mistakes
        this.gameType = quizType;
        this.correctNum = 0;

        this.root = new BorderPane();

        // Create and format heading
        Label header = new Label((quizType == 0) ? "New Spelling Quiz" : "Review Mistakes");
        header.getStyleClass().add("headerText");
        HBox topPane = new HBox(header);
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(20, 10, 10, 10));
        root.setTop(topPane);

        // Run bash process to fetch random words, alert the user if there are no words to review
        if (!fetchWords(quizType)) {

            // Create container for components
            VBox noWords = new VBox(20);
            noWords.setPrefWidth(400);
            noWords.setAlignment(Pos.TOP_CENTER);

            // Create components
            Label message = new Label("Cannot review mistakes, so far none have been made!");
            message.getStyleClass().add("subHeaderText");
            message.setPadding(new Insets(100, 0, 0, 0));
            
            Button returnToMenuButton = new Button("Return to Main Menu");
            returnToMenuButton.setOnAction(actionEvent -> {
                App.returnToMainMenu();
            });
            returnToMenuButton.setMinSize(noWords.getPrefWidth(), 75);

            // Add components to container
            noWords.getChildren().addAll(message, returnToMenuButton);
            root.setCenter(noWords);
        }
        else {
            // Give the user some instructions and wait for their confirmation to start

            // Create container for components
            VBox instructionsContainer = new VBox(20);
            instructionsContainer.setPrefWidth(400);
            instructionsContainer.setAlignment(Pos.TOP_CENTER);

            // Create components
            Label instructions = new Label("Welcome to the spelling quiz. " +
                "When you press start, the first of three words will be read aloud. " +
                "For each word, type its correct spelling into the box. " +
                "You have two attempts for each word. Good luck!");
            instructions.getStyleClass().add("subHeaderText");
            instructions.setPadding(new Insets(100, 70, 0, 70));
            instructions.setWrapText(true);
            instructions.setAlignment(Pos.CENTER);

            Button startQuiz = new Button("Begin Quiz!");
            startQuiz.setOnAction(actionEvent -> {
                spell(quizWords.get(0), 0, quizWords.size(), true);
            });
            startQuiz.setMinSize(instructionsContainer.getPrefWidth(), 75);

            // Add components to container
            instructionsContainer.getChildren().addAll(instructions, startQuiz);
            root.setCenter(instructionsContainer);
        }
    }

    public void spell(String word, int questionNum, int numQuestions, Boolean firstTry) {

        // Create container for main components
        VBox quizBox = new VBox(10);
        quizBox.setPadding(new Insets(100));
        quizBox.setPrefWidth(500);
        quizBox.setAlignment(Pos.TOP_LEFT);

        // Create components
        Label title = new Label("Spell word " + (questionNum+1) + " of " + numQuestions);
        title.getStyleClass().add("subHeaderText");

        TextField userInput = new TextField();
        userInput.setMaxWidth(quizBox.getPrefWidth());

        // Add components to container
        quizBox.getChildren().addAll(title, userInput);

        // Add a try again button, only shown if the user gets it wrong the first time
        Button tryAgain = new Button("Try Again");
        tryAgain.setOnAction(actionEvent -> {
            spell(quizWords.get(questionNum), questionNum, numQuestions, false);
        });

        // Add a next question button if there are still more words, else it goes to results
        Button nextQuestion = new Button();
        if (questionNum < numQuestions-1) {
            nextQuestion.setText("Next Question");
            nextQuestion.setOnAction(actionEvent -> {
                // Call spell again for the next word
                spell(quizWords.get(questionNum+1), questionNum+1, numQuestions, true);
            });
        }
        else {
            nextQuestion.setText("Finish");
            nextQuestion.setOnAction(actionEvent -> {
                // Go to the results screen
                Results results = new Results(correctNum, numQuestions, gameType);
                App.getStage().getScene().setRoot(results.getRoot());
            });
        }

        // Add a submit button for the user
        Button submit = new Button("Submit");

        // Create submission event for when the button is pressed, or the user presses enter
        submit.setOnAction(actionEvent -> {
            // Don't do anything if user hasn't typed anything 
            if (userInput.getText().length()>0) {
                // Check if word matches
                if (word.equalsIgnoreCase(userInput.getText().trim())) {
                    FestivalController.speak("Correct", 1);
                    correctNum++;
                    if (firstTry) {
                        FileController.incrementStats(word, 0); // Increment mastered column
                    }
                    else {
                        FileController.incrementStats(word, 1); // Increment faulted column 
                    }
                    // Remove word from the failed list if it's there
                    FileController.removeFromList("words/.failedWords", word);
                    // Show the next question button if the user gets it right the first time
                    quizBox.getChildren().add(nextQuestion);
                }
                else if (firstTry) {
                    // User gets it wrong the first time, give them another chance 
                    FestivalController.speak("Incorrect, try once more", 1);
                    quizBox.getChildren().add(tryAgain);
                }
                else {
                    // User got it wrong again, go to the next question and add to failed words
                    FestivalController.speak("Incorrect", 1);
                    FileController.incrementStats(word, 2); // Increment failed column for this word
                    FileController.writeToList("words/.failedWords", word);
                    quizBox.getChildren().add(nextQuestion);
                }
                // Hide enter button and form once the user has submitted something
                quizBox.getChildren().removeAll(submit, userInput);
            }
            
        });
        
        // Run the submission event when the user presses enter
        userInput.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    submit.fire();
                }
            }           
        });

        quizBox.getChildren().add(submit);
        root.setCenter(quizBox);

        // Read out the word to the user
        FestivalController.speak(word, firstTry ? 1 : 2);
        
    }

    public boolean fetchWords(int quizType) {
        String cmd = "";
        if (quizType == 0) {
            // New Spelling Quiz
            cmd = "shuf -n 3 words/popular";
        }
        else {
            // Review Mistakes
            int failedLength = FileController.countLines("words/.failedWords");
            if (failedLength < 1) {
                return false;
            }
            else if (failedLength < 3) {
                cmd = "shuf -n " + failedLength + " words/.failedWords";
            }
            else {
                cmd = "shuf -n 3 words/.failedWords";
            }
        }
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
        try {
            Process process = builder.start();
            process.waitFor();
            InputStream stdout = process.getInputStream();
            BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
            String line = null;
            while ((line = stdoutBuffered.readLine()) != null) {
                this.quizWords.add(line);
            }
            return true;
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public BorderPane getRoot() {
        return this.root;
    }
}
