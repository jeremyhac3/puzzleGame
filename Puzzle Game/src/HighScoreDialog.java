/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 *
 * @author jhfinkelstein
 */
public class HighScoreDialog extends Dialog {
    private final TableView<HighScoreStatus> table = new TableView<>();
    private final ObservableList<HighScoreStatus> data;
    private final ArrayList<HighScoreStatus> highScoresList;
    //Node okButton;
    
    public HighScoreDialog(Window owner, String title, SortedSet<Map.Entry<String, Pair<String, String>>> highScores) {
        setTitle(title);
        highScoresList = new ArrayList<>();


        // load all the high scores
        for (Map.Entry<String, Pair<String,String>> entry : highScores) {
            HighScoreStatus hs;
            String playerName = entry.getKey();
            String puzzleName = entry.getValue().getKey();
            String playerTime = entry.getValue().getValue();

            hs = new HighScoreStatus(playerName, puzzleName, playerTime);
            highScoresList.add(hs);
        }
        
        this.data = FXCollections.observableArrayList(highScoresList);
        
        ButtonType okButtonType = new ButtonType("OK",
                ButtonBar.ButtonData.OK_DONE);

        getDialogPane().getButtonTypes().addAll(okButtonType);
        
        //okButton = getDialogPane().lookupButton(okButtonType);
        /*o
        Button okButton = new Button("OK");
        okButton.setMinWidth(75);
        //setMargin(okButton, new Insets(0,-150,0,100));
        setMargin(okButton, new Insets(0, -160, 0, 160));
        Button cancelButton = new Button("Cancel");
        cancelButton.setMinWidth(75);
        setMargin(cancelButton, new Insets(0,0,0,170));
        */
        
        table.setRowFactory(new Callback<TableView<HighScoreStatus>, TableRow<HighScoreStatus>>(){
            @Override
            public TableRow<HighScoreStatus> call(TableView<HighScoreStatus> param) {
                final TableRow<HighScoreStatus> row = new TableRow<>(); 
                row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        final int index = row.getIndex();
                        if (index < 0 || index > table.getItems().size() || table.getSelectionModel().isSelected(index)) {
                            table.getSelectionModel().clearSelection();
                            event.consume();
                        }
                    }
                });
                return row;
            }
        });
        
        // Create the labels and fields.
        // col, row, colSpan, rowSpan
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        
        table.setEditable(false);
        
        TableColumn playerNameCol = new TableColumn("Player Name");
        playerNameCol.setMinWidth(200);
        playerNameCol.setCellValueFactory(
                new PropertyValueFactory<HighScoreStatus, String>("playerName"));
 
        TableColumn puzzleNameCol = new TableColumn("Puzzle Name");
        puzzleNameCol.setMinWidth(150);
        puzzleNameCol.setCellValueFactory(
                new PropertyValueFactory<HighScoreStatus, String>("puzzleName"));
 
        TableColumn playerTimeCol = new TableColumn("Player Time");
        playerTimeCol.setMinWidth(100);
        playerTimeCol.setCellValueFactory(
                new PropertyValueFactory<HighScoreStatus, String>("playerTime"));
 
        table.setItems(data);
        table.getColumns().addAll(playerNameCol, puzzleNameCol, playerTimeCol);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        grid.add(table, 0, 0, 4, 1);
        getDialogPane().setContent(grid);
        
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("file:opromaLOGO.PNG"));
        
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static class HighScoreStatus {
        private final SimpleStringProperty playerName;
        private final SimpleStringProperty puzzleName;
        private final SimpleStringProperty playerTime;
        
        private HighScoreStatus(String pname, String puzzleName, String ptime) {
            this.playerName = new SimpleStringProperty(pname);
            this.puzzleName = new SimpleStringProperty(puzzleName);
            this.playerTime = new SimpleStringProperty(ptime);
        }

        public String getPlayerName() {
            return playerName.get();
        }

        public String getPuzzleName() { return puzzleName.get(); }

        public String getPlayerTime() {
            return playerTime.get();
        }
        
        public void setPlayerName(String pName) {
            playerName.set(pName);
        }
        
        public void setPuzzleName(String puzName) {
            puzzleName.set(puzName);
        }
    }
    
}
