import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Results {
    private BorderPane root;

    public Results(int correctNum, int totalNum, int gameType) {
        this.root = new BorderPane();

        // Create and format heading
        Label header = new Label("Results");
        header.getStyleClass().add("headerText");
        HBox topPane = new HBox(header);
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(20, 10, 100, 10));
        root.setTop(topPane);

        // Create container for components
        VBox main = new VBox(20);
        main.setPrefWidth(400);
        main.setAlignment(Pos.TOP_CENTER);

        // Create message component and add to scene
        String message = (correctNum >= totalNum/2) ? 
            ("Congratulations, you spelled " + correctNum + " out of " + totalNum + " words correctly") :
            (correctNum > 0) ? ("Sorry, you only spelled " + correctNum + " out of " + totalNum + " words correctly") :
            ("Sorry, you didn't spell any words correctly");
        Label label = new Label(message);
        label.getStyleClass().add("subHeaderText");
        main.getChildren().add(label);

        // Create button components
        Button playAgain = new Button("New Quiz");
        playAgain.setOnAction(actionEvent -> {
            Quiz quiz = new Quiz(0);
            App.getStage().getScene().setRoot(quiz.getRoot());
        });
        playAgain.setMinSize(main.getPrefWidth(), 75);

        Button returnToMenuButton = new Button("Back to Menu");
        returnToMenuButton.setOnAction(actionEvent -> {
            App.returnToMainMenu();
        });
        returnToMenuButton.setMinSize(main.getPrefWidth(), 75);

        // Only create review mistakes button if the user was in a review mistakes quiz, and there are words to review
        if (gameType == 1 && FileController.countLines("words/.failedWords") > 0) {
            Button reviewAgain = new Button("Review Mistakes Again");
            reviewAgain.setOnAction(actionEvent -> {
                Quiz quiz = new Quiz(1);
                App.getStage().getScene().setRoot(quiz.getRoot());
            });
            reviewAgain.setMinSize(main.getPrefWidth(), 75);
            main.getChildren().add(reviewAgain);
        }

        // Add buttons to scene
        main.getChildren().addAll(playAgain, returnToMenuButton);
        root.setCenter(main);
    }

    public BorderPane getRoot() {
        return this.root;
    }
}
