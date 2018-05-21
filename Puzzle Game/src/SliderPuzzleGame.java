import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.*;

public class SliderPuzzleGame extends Application {
    private SliderPuzzleBoard puzzleBoard;
    private SliderPuzzleGameView view;
    private Timeline updateTimer;
    private SimpleDateFormat timeFormat;
    private String pattern;
    private int elapsedTime;
    private boolean hasRecordedScore = false;
    private Map<String, Pair<String, String>> highScores = new TreeMap<>();
    private SortedSet<Map.Entry<String, Pair<String, String>>> sortedSet;
    private static int NUM_HIGH_SCORES = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        puzzleBoard = new SliderPuzzleBoard("Lego");
        view = new SliderPuzzleGameView(puzzleBoard);
        Scene scene = new Scene(view, view.getPrefWidth(), view.getPrefHeight());
        pattern = "mm:ss";
        timeFormat = new SimpleDateFormat(pattern);
        elapsedTime = 0;

        sortedSet = new TreeSet<Map.Entry<String, Pair<String, String>>>(
                new Comparator<Map.Entry<String, Pair<String, String>>>() {
                    @Override
                    public int compare(Map.Entry<String, Pair<String, String>> o1, Map.Entry<String, Pair<String, String>> o2) {
                        // get the timer values of each high score entry
                        String value1 = o1.getValue().getValue();
                        String value2 = o2.getValue().getValue();
                        int time1; int time2;
                        time1 = getTimeInSeconds(o1.getValue().getValue());
                        time2 = getTimeInSeconds(o2.getValue().getValue());
                        if (time1 == time2) { return o1.getValue().getKey().compareTo(o2.getValue().getKey());  }
                        else                {  return time1 - time2;  }
                    }
                }
        );

        primaryStage.setTitle("Slider Puzzle Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        createInitialMessage();

        for (int i = 0; i < puzzleBoard.getRows(); i++) {
            for (int j = 0; j < puzzleBoard.getCols(); j++) {
                final int row = i;
                final int col = j;
                view.getButtonPieces()[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        swap(row, col);
                        if (hasWon()) {
                            placeWinningPiece();
                            view.getTimerButton().setText("Start");
                            view.getPuzzleList().setDisable(false);
                            view.getThumbnail().setDisable(false);
                            view.getHighScoresButton().setDisable(false);
                            updateTimer.stop();
                            if (!hasRecordedScore) {
                                checkHighScores();
                                hasRecordedScore = true;
                            }
                        }
                        view.update();
                    }
                });
            }
        }

        view.getPuzzleList().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String listItem = view.getPuzzleList().getSelectionModel().getSelectedItem();
                if (listItem.equals("Pets")) {
                    view.getThumbnail().setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Images/Pets_Thumbnail.png"))));
                }
                else if (listItem.equals("Lego")) {
                    view.getThumbnail().setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Images/Lego_Thumbnail.png"))));
                }
                else if (listItem.equals("Numbers")) {
                    view.getThumbnail().setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Images/Numbers_Thumbnail.png"))));
                }
                else if (listItem.equals("Scenery")) {
                    view.getThumbnail().setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Images/Scenery_Thumbnail.png"))));
                }
            }
        });

        view.getTimerButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (view.getTimerButton().getText().equals("Start")) {
                    view.getTimerButton().setStyle("-fx-text-fill: rgb(255,255,255); -fx-background-color: rgb(100, 30, 40);");
                    view.getTimerButton().setText("Stop");
                    view.getPuzzleList().setDisable(true);
                    view.getThumbnail().setDisable(true);
                    view.getHighScoresButton().setDisable(true);
                    elapsedTime = 0;
                    view.getTimeField().setText(timeFormat.format(new Date(elapsedTime)));
                    puzzleBoard.setPuzzleString(view.getPuzzleList().getSelectionModel().getSelectedItem());
                    puzzleBoard.initBoard();
                    randomizeBlankPiece();
                    randomizeAllPieces();
                    updateTimer.play();

                }
                else {
                    view.getTimerButton().setStyle("-fx-text-fill: rgb(255,255,255); -fx-background-color: rgb(30, 100, 40);");
                    view.getTimerButton().setText("Start");
                    view.getPuzzleList().setDisable(false);
                    view.getThumbnail().setDisable(false);
                    view.getHighScoresButton().setDisable(false);
                    puzzleBoard.resetBoard();
                    updateTimer.stop();
                }
                hasRecordedScore = false;
                view.update();
            }
        });

        view.getHighScoresButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkHighScores();
            }
        });

        updateTimer = new Timeline(new KeyFrame(Duration.millis(1000),
                new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent event) {
                        elapsedTime = elapsedTime+ 1 * 1000;
                        Date d = new Date(elapsedTime);
                        view.getTimeField().setText(timeFormat.format(d));
                    }
                }));
        updateTimer.setCycleCount(Timeline.INDEFINITE);
    }

    public void createInitialMessage() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Introduction");
        info.setHeaderText("Welcome to the Slider Puzzle Game!\n");
        info.setContentText("- Select a puzzle from the list of available puzzles.\n\n- Press the 'Start' button to initiate the puzzle and start the timer. \n\n" +
                "- Click on a tile that is adjacent to the black tile to swap it.  \n\n " +
                "- To win, match all the board pieces to the corresponding positions in the top-right corner. \n\n " +
                "- When all pieces match, the game will complete.  The top 5 best scores are recorded in the high scores table. \n\n " +
                "Good luck!!!");
        info.showAndWait();
    }

    public void swap(int row, int col) {
        SliderPuzzlePiece aPiece = puzzleBoard.getPuzzlePiece(row, col);
        SliderPuzzlePiece blankPiece = null;
        int blankRow = -1;
        int blankCol = -1;

        if (row > 0 && puzzleBoard.getPuzzlePiece(row-1, col) == puzzleBoard.getBlankPiece()) { //.getFileName().equals("BLANK.png")) {
            blankRow = row-1;
            blankCol = col;
        }
        else if (row < puzzleBoard.getRows()-1 && puzzleBoard.getPuzzlePiece(row+1, col) == puzzleBoard.getBlankPiece()) { //.getFileName().equals("BLANK.png")) {
            blankRow = row+1;
            blankCol = col;
        }
        else if (col > 0 && puzzleBoard.getPuzzlePiece(row, col-1) == puzzleBoard.getBlankPiece()) { //.getFileName().equals("BLANK.png")) {
            blankRow = row;
            blankCol = col-1;
        }
        else if (col < puzzleBoard.getCols()-1 && puzzleBoard.getPuzzlePiece(row, col+1) == puzzleBoard.getBlankPiece()) { //.getFileName().equals("BLANK.png")) {
            blankRow = row;
            blankCol = col + 1;
        }

        if (blankCol != -1 && blankRow != -1) {
            blankPiece = puzzleBoard.getPuzzlePiece(blankRow, blankCol);
            puzzleBoard.getPuzzlePieces()[row][col] = blankPiece;
            puzzleBoard.getPuzzlePieces()[blankRow][blankCol] = aPiece;
        }
    }

    public void randomizeBlankPiece() {
        int randomCol = (int) (Math.random()*4);
        int randomRow = (int) (Math.random()*4);

        puzzleBoard.setFinalPiece(puzzleBoard.getPuzzlePiece(randomRow, randomCol));
        puzzleBoard.getPuzzlePieces()[randomRow][randomCol] = puzzleBoard.getBlankPiece();

       // System.out.println("The random col is: " + randomCol);
       // System.out.println("The random row is: " + randomRow);
       // System.out.println("The missing piece is: " + puzzleBoard.getFinalPiece().getFileName());
    }

    public void randomizeAllPieces() {
        int count = 0;
        while (count < 5000) {
            int randomCol = (int) (Math.random() * 4);
            int randomRow = (int) (Math.random() * 4);
            swap(randomRow, randomCol);
            count++;
        }
    }

    public boolean hasWon() {
        String puzzle = puzzleBoard.getPuzzleString();
        int blackPiecesCount = 0;
        int piecesCount = 0;
        for (int i = 0; i < puzzleBoard.getRows(); i++) {
            for (int j = 0; j < puzzleBoard.getCols(); j++) {
                        if (puzzleBoard.getPuzzlePieces()[i][j] == puzzleBoard.getBlankPiece()) {
                            blackPiecesCount++;
                        }
                        else if (puzzleBoard.getPuzzlePieces()[i][j].getFileName().equals(puzzle + "_" + i + j + ".png")) {
                            piecesCount++;
                        }
            }
        }

        if ((blackPiecesCount == 1 && piecesCount == 15) || piecesCount == 16) {
            return true;
        }
        return false;
    }

    public void placeWinningPiece() {
        for (int i = 0; i < puzzleBoard.getRows(); i++) {
            for (int j = 0; j < puzzleBoard.getCols(); j++) {
                if (puzzleBoard.getPuzzlePieces()[i][j] == puzzleBoard.getBlankPiece()) {
                    puzzleBoard.getPuzzlePieces()[i][j] = puzzleBoard.getFinalPiece();
                }
            }
        }
    }

    public int getTimeInSeconds(String time) {
        int timeValue = -1;

        try {
            String[] valueSplit = time.split(":");
            timeValue = Integer.valueOf(valueSplit[0])*60 + Integer.valueOf(valueSplit[1]);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return timeValue;
    }

    public void addHighScore() {

        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("New High Score");
        inputDialog.setHeaderText(null);
        inputDialog.setContentText("Please enter your name: ");
        Optional<String> result = inputDialog.showAndWait();

        String playerName = result.get();
        String puzzleName = puzzleBoard.getPuzzleString();
        String playerTime = view.getTimeField().getText();

        highScores.put(playerName, new Pair(puzzleName, playerTime));
        sortedSet.addAll(highScores.entrySet());
    }

    public void checkHighScores() {
        if (hasWon() && !hasRecordedScore) {
            if (NUM_HIGH_SCORES < 5) {
                addHighScore();
                NUM_HIGH_SCORES++;
            }
            // remove the lowest score and replace it with the latest score
            else if (NUM_HIGH_SCORES == 5) {
                Map.Entry<String, Pair<String, String>> bottomScore = sortedSet.last();
                String bottomTimeString = bottomScore.getValue().getValue();
                String latestTimeString = view.getTimeField().getText();
                int bottomTimeValue = getTimeInSeconds(bottomTimeString);
                int latestTimeValue = getTimeInSeconds(latestTimeString);

                if (bottomTimeValue > latestTimeValue) {
                    highScores.remove(bottomScore.getKey());
                    sortedSet.remove(bottomScore);
                    for (Map.Entry<String, Pair<String,String>> entry: highScores.entrySet()) {
                       // System.out.println(entry.getKey() + ": " + entry.getValue().getKey() + ", " +entry.getValue().getValue());
                    }
                    addHighScore();
                }

            }
        }
        HighScoreDialog highScoreDialog = new HighScoreDialog(view.getScene().getWindow(), "High Scores Table", sortedSet);
        highScoreDialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
