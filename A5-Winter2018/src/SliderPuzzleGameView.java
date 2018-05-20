import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class SliderPuzzleGameView extends Pane {
    private SliderPuzzleBoard model;
    private Button[][] buttonPieces;
    private Label thumbnail;
    private ListView<String> puzzleList;
    private Label timeLabel;
    private Button timerButton;
    private Button highScoresButton;
    private TextField timeField;

    // public getters
    public Button[][] getButtonPieces() { return buttonPieces; }
    public Label getThumbnail() { return thumbnail; }
    public ListView<String> getPuzzleList() { return puzzleList; }
    public Button getTimerButton() { return timerButton; }
    public Button getHighScoresButton() { return highScoresButton; }

    public TextField getTimeField() {  return timeField; }

    public SliderPuzzleGameView(SliderPuzzleBoard model) {
        this.model = model;
        setPrefSize(950, 748);

        buttonPieces = new Button[4][4];
        for (int i =0; i < 4; i++) { // column
            for (int j = 0; j < 4; j++) { // row
                buttonPieces[i][j] = new Button();
                buttonPieces[i][j].setStyle("-fx-border-color: white;");
                buttonPieces[i][j].relocate(j*187, i*187);
                buttonPieces[i][j].setPrefSize(187, 187);
                buttonPieces[i][j].setPadding(new Insets(0,0,0,0));
                buttonPieces[i][j].setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Images/BLANK.png"))));
             //   buttonPieces[i][j].setText("BUTTON [" + i + "]["+ j + "]");
                getChildren().addAll(buttonPieces[i][j]);
            }
        }

        thumbnail = new Label("Thumbnail");
        thumbnail.setPrefSize(187, 187);
        thumbnail.relocate(758,0);
        thumbnail.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Images/Lego_Thumbnail.png"))));

        puzzleList = new ListView<>();
        String[] puzzles = {"Pets", "Scenery", "Lego", "Numbers"};
        puzzleList.setItems(FXCollections.observableArrayList(puzzles));
        puzzleList.relocate(758, 197);
        puzzleList.setPrefSize(187, 150);
        puzzleList.getSelectionModel().select(2);

        timerButton = new Button("Start");
        timerButton.setStyle("-fx-text-fill: rgb(255,255,255); -fx-background-color: rgb(30, 100, 40);");
        timerButton.setPrefSize(187, 20);
        timerButton.relocate(758, 357);

        timeLabel = new Label("Time: ");
        timeLabel.setPrefSize(80, 20);
        timeLabel.relocate(758, 407);

        timeField = new TextField("00:00");
        timeField.setPrefSize(137, 20);
        timeField.relocate(808, 403);

        highScoresButton = new Button("Show High Scores");
        highScoresButton.setStyle("-fx-text-fill: rgb(255,255,255); -fx-background-color: rgb(128,128, 0);");
        highScoresButton.setPrefSize(187, 20);
        highScoresButton.relocate(758, 457);

        this.getChildren().addAll( thumbnail, puzzleList, timerButton, timeLabel, timeField, highScoresButton);
    }

    public void update() {
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                 buttonPieces[i][j].setGraphic(new ImageView(model.getPuzzlePieces()[i][j].getImagePiece()));
            }
        }
    }


}
