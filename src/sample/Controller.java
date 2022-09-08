package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;

public class Controller {
    @FXML
    private Button ImageResize;
    @FXML
    private Button TextCompress;

    public void ImageClicked()
    {
        try {
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("ImageResize.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Image Resize");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void TextClicked()
    {
        try {
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("TextCompress.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Text Compress");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
