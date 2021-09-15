import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClearStats {
    private BorderPane root;

    public ClearStats() {
        this.root = new BorderPane();

        // Create and format heading 
        Label header = new Label("Are you sure you want to clear stats?");
        header.getStyleClass().add("headerText");
        HBox topPane = new HBox(header);
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(20, 10, 100, 10));
        root.setTop(topPane);

        // Create container for buttons
        VBox optionButtons = new VBox(20);
        optionButtons.setPrefWidth(400);
        optionButtons.setAlignment(Pos.TOP_CENTER);

        // Create buttons
        Button returnToMenuButton = new Button("Back to Menu");
        returnToMenuButton.setOnAction(actionEvent -> {
            App.returnToMainMenu();
        });
        returnToMenuButton.setMinSize(optionButtons.getPrefWidth(), 75);

        Button dontClearStats = new Button("No, return to main menu");
        dontClearStats.setOnAction(actionEvent -> {
            App.returnToMainMenu();
        });
        dontClearStats.setMinSize(optionButtons.getPrefWidth(), 75);

        Button clearStats = new Button("Yes, clear stats");
        clearStats.setOnAction(actionEvent -> {
            // Clear both stats files
            FileController.wipeFile("words/.failedWords");
            FileController.wipeFile(".gameStats");

            // Give feedback to the user that stats have been cleared
            header.setText("Stats have been cleared!");
            optionButtons.getChildren().removeAll(clearStats, dontClearStats);
            optionButtons.getChildren().add(returnToMenuButton);
        });
        clearStats.setMinSize(optionButtons.getPrefWidth(), 75);

        // Add buttons to scene
        optionButtons.getChildren().addAll(clearStats, dontClearStats);
        root.setCenter(optionButtons);
    }

    public BorderPane getRoot() {
        return this.root;
    }
}

