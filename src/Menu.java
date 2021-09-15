import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Menu {
    
    private BorderPane root;

    public Menu() {
        this.root = new BorderPane();

        // Create and format heading
        Label welcomeText = new Label("Welcome to Spelling Wiz!");
        welcomeText.getStyleClass().add("headerText");
        HBox topPane = new HBox(welcomeText);
        topPane.setAlignment(Pos.CENTER);
        welcomeText.setPadding(new Insets(20, 10, 100, 10));
        root.setTop(topPane);

        // Create container for buttons
        VBox menuButtons = new VBox(20);
        menuButtons.setPrefWidth(400);
        menuButtons.setAlignment(Pos.TOP_CENTER);

        // Create menu buttons
        Button newGame = new Button("New Spelling Quiz");   
        newGame.setOnAction(actionEvent -> {
            Quiz quiz = new Quiz(0);
            App.getStage().getScene().setRoot(quiz.getRoot());
        });
        newGame.setMinSize(menuButtons.getPrefWidth(), 75);

        Button reviewMistakes = new Button("Review Mistakes");
        reviewMistakes.setOnAction(actionEvent -> {
            Quiz quiz = new Quiz(1);
            App.getStage().getScene().setRoot(quiz.getRoot());
        });
        reviewMistakes.setMinSize(menuButtons.getPrefWidth(), 75);

        Button viewStats = new Button("View Statistics");
        viewStats.setOnAction(actionEvent -> {
            Stats stats = new Stats();
            App.getStage().getScene().setRoot(stats.getRoot());
        });
        viewStats.setMinSize(menuButtons.getPrefWidth(), 75);

        Button clearStats = new Button("Clear Statistics");
        clearStats.setOnAction(actionEvent -> {
            ClearStats clearStatsConfirm = new ClearStats();
            App.getStage().getScene().setRoot(clearStatsConfirm.getRoot());
        });
        clearStats.setMinSize(menuButtons.getPrefWidth(), 75);

        Button quitGame = new Button("Quit Game");
        quitGame.setOnAction(actionEvent -> {
            App.quit();
        });
        quitGame.setMinSize(menuButtons.getPrefWidth(), 75);

        // Add buttons to scene
        menuButtons.getChildren().addAll(newGame, reviewMistakes, viewStats, clearStats, quitGame);
        root.setCenter(menuButtons);

    }

    public BorderPane getRoot() {
        return this.root;
    }
}
