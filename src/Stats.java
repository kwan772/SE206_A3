import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Stats {
    private BorderPane root;

    public Stats() {
        this.root = new BorderPane();

        // Create header containers
        HBox headerContainer = new HBox();
        HBox titleContainer = new HBox();

        // Create header elements
        Label header = new Label("Game Statistics");
        header.getStyleClass().add("headerText");

        Button returnToMenuButton = new Button("Back to Menu");
        returnToMenuButton.setOnAction(actionEvent -> {
            App.returnToMainMenu();
        });
        returnToMenuButton.setMinSize(200, 50);

        // Add elements to header containers
        titleContainer.getChildren().add(header);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(0, 0, 0, 110));
        
        headerContainer.getChildren().addAll(returnToMenuButton, titleContainer);
        headerContainer.setPadding(new Insets(20));

        root.setTop(headerContainer);

        // Create and populate table
        TableView<Word> statsTable = createTable();
        populateTable(statsTable);
        root.setCenter(statsTable);
        
    }

    public TableView<Word> createTable() {
        TableView<Word> stats = new TableView<>();

        TableColumn<Word, String> word = new TableColumn<>("Word");
        word.setCellValueFactory(new PropertyValueFactory<>("word"));

        TableColumn<Word, String> mastered = new TableColumn<>("Mastered");
        mastered.setCellValueFactory(new PropertyValueFactory<>("mastered"));

        TableColumn<Word, String> faulted = new TableColumn<>("Faulted");
        faulted.setCellValueFactory(new PropertyValueFactory<>("faulted"));

        TableColumn<Word, String> failed = new TableColumn<>("Failed");
        failed.setCellValueFactory(new PropertyValueFactory<>("failed"));

        TableColumn<Word, String> percentCorrect = new TableColumn<>("Success Rate");
        percentCorrect.setCellValueFactory(new PropertyValueFactory<>("percentCorrect"));

        stats.getColumns().add(word);
        stats.getColumns().add(mastered);
        stats.getColumns().add(faulted);
        stats.getColumns().add(failed);
        stats.getColumns().add(percentCorrect);

        stats.getColumns().forEach(column -> column.setMinWidth(100));
        percentCorrect.setMinWidth(130);

        stats.setPlaceholder(new Label("No stats to display yet!"));

        return stats;
    }

    public void populateTable(TableView<Word> table) {
        // Run through the stats file and create a new instance of the word class for each line, then display these instances on the table
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(".gameStats"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineData = line.split(" ");
                table.getItems().add(new Word(lineData[0], lineData[1], lineData[2], lineData[3]));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BorderPane getRoot() {
        return this.root;
    }
}
