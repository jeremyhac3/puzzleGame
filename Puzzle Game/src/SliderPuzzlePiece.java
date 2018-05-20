import javafx.scene.image.Image;

public class SliderPuzzlePiece {
    Image imagePiece;
    String fileName;

    public SliderPuzzlePiece(Image img, String fileName) {
        imagePiece = img;
        this.fileName = fileName;
    }

    public Image getImagePiece() { return imagePiece; }
    public String getFileName() { return fileName; }

}
