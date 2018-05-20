import javafx.scene.image.Image;

public class SliderPuzzleBoard {
    private SliderPuzzlePiece[][] puzzlePieces;
    private int rows;
    private int cols;
    private String blankLocation;
    private String puzzleString;
    private SliderPuzzlePiece finalPiece;
    private SliderPuzzlePiece blankPiece;

    public SliderPuzzleBoard(String puzzleString)
    {
        rows = 4;
        cols = 4;
        puzzlePieces = new SliderPuzzlePiece[rows][cols];
        this.puzzleString = puzzleString;
        finalPiece = null;
        blankLocation = "BLANK.png";
        blankPiece = new SliderPuzzlePiece(new Image(getClass().getResourceAsStream("Images/" + blankLocation)), blankLocation);
        resetBoard();
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public SliderPuzzlePiece getBlankPiece() { return blankPiece; }

    public String getPuzzleString() { return puzzleString; }

    public void setPuzzleString(String puzzleString) { this.puzzleString = puzzleString; }

    public void setFinalPiece(SliderPuzzlePiece finalPiece) { this.finalPiece = finalPiece; }

    public SliderPuzzlePiece getFinalPiece() { return finalPiece; }

    public SliderPuzzlePiece[][] getPuzzlePieces() {
        return puzzlePieces;
    }


    public SliderPuzzlePiece getPuzzlePiece(int row, int col) {
        return puzzlePieces[row][col];
    }

    public void resetBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                puzzlePieces[i][j] = blankPiece;
            }
        }
    }

    public void initBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String fileName = puzzleString + "_" + i + j + ".png";
                String imageLocation = "Images/" + fileName;
                puzzlePieces[i][j]= new SliderPuzzlePiece(new Image(getClass().getResourceAsStream(imageLocation)), fileName);
            }
        }
    }
}
