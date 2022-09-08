package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ImageResize implements Initializable {

    private SeamCarving seamCarving;
    @FXML
    private Text stateText;

    @FXML
    private TextField resizeNum;

    @FXML
    private TextField resizeHeightNum;

    @FXML
    private TextField resizeWidthNum;

    @FXML
    private TextField pathEntered;

    @FXML
    private Button okButton;

    @FXML
    private Button originalButton;

    @FXML
    private Button energyButton;

    @FXML
    private Button plusVerButton;

    @FXML
    private Button minusVerButton;

    @FXML
    private Button plusHorButton;

    @FXML
    private Button minusHorButton;

    @FXML
    private Button resizeButton;

    @FXML
    private Button resizeHeight;

    @FXML
    private Button resizeWidth;


    static String imagePath="";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    public void okClicked()
    {
        okButton.setDisable(true);
        EnableButtons(false);
        stateText.setText("Doing some processing .. Please Wait");
        String x= pathEntered.getText();
        imagePath="";
        for (int i=0;i<x.length();i++)
        {
            if (x.charAt(i)==34)
                continue;
            imagePath+=x.charAt(i);
            if (x.charAt(i)==92)
                imagePath+=x.charAt(i);
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedImage image;
                try {
                    image = ImageIO.read(new File(imagePath));
                    seamCarving = new SeamCarving(image);
                    seamCarving.writeEnergyImage();
                    seamCarving.RestoreImage();
                    seamCarving.writeImageWithVerSeam();
                    seamCarving.writeImageAfterDeleteVerSeam();
                    seamCarving.RestoreImage();
                    seamCarving.writeImageWithHorSeam();
                    seamCarving.writeImageAfterDeleteHorSeam();
                    EnableButtons(true);
                    stateText.setText("Done!!");
                    okButton.setDisable(false);
                } catch (IOException e) {
                    stateText.setText("Invalid Path");
                    okButton.setDisable(false);
                }
            }
        });
        t.start();
    }

    public void EnableButtons(boolean enabled)
    {
        originalButton.setVisible(enabled);
        originalButton.setDisable(!enabled);

        energyButton.setVisible(enabled);
        energyButton.setDisable(!enabled);

        plusHorButton.setVisible(enabled);
        plusHorButton.setDisable(!enabled);

        minusHorButton.setVisible(enabled);
        minusHorButton.setDisable(!enabled);

        plusVerButton.setVisible(enabled);
        plusVerButton.setDisable(!enabled);

        minusVerButton.setVisible(enabled);
        minusVerButton.setDisable(!enabled);

        resizeButton.setVisible(enabled);
        resizeButton.setDisable(!enabled);

        resizeHeight.setVisible(enabled);
        resizeHeight.setDisable(!enabled);

        resizeWidth.setVisible(enabled);
        resizeWidth.setDisable(!enabled);

        resizeNum.setVisible(enabled);
        resizeNum.setDisable(!enabled);

        resizeHeightNum.setVisible(enabled);
        resizeHeightNum.setDisable(!enabled);

        resizeWidthNum.setVisible(enabled);
        resizeWidthNum.setDisable(!enabled);
    }

    public void originalClicked()
    {
        try {
            DisplayImage.setPath(imagePath);
            DisplayImage.setType(-1);

            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Original Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void energyClicked()
    {
        try {
            DisplayImage.setPath("src/sample/EnergyImage.png");
            DisplayImage.setType(-1);
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Energy Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public void plusVerClicked()
    {
        try {
            DisplayImage.setPath("src/sample/SeamVerImage.png");
            DisplayImage.setType(-1);
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Seam Ver Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public void minusVerClicked()
    {
        try {
            DisplayImage.setPath("src/sample/ImageAfterDeleteVerSeam.png");
            DisplayImage.setType(-1);
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Deleted Ver Seam Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public void plusHorClicked()
    {
        try {
            DisplayImage.setPath("src/sample/SeamHorImage.png");
            DisplayImage.setType(-1);
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Seam Hor Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public void minusHorClicked()
    {
        try {
            DisplayImage.setPath("src/sample/ImageAfterDeleteHorSeam.png");
            DisplayImage.setType(-1);
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Deleted Hor Seam Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public void resizeClicked()
    {
        try {
            String x = resizeNum.getText();
            DisplayImage.setValue(x);
            DisplayImage.setPath("src/sample/wait.jpg");
            DisplayImage.setSeamCarving(seamCarving);
            DisplayImage.setType(0);
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Resized Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }
    public void resizeHeightClicked()
    {
        try {
            String x = resizeHeightNum.getText();
            DisplayImage.setValue(x);
            DisplayImage.setPath("src/sample/wait.jpg");
            DisplayImage.setSeamCarving(seamCarving);
            DisplayImage.setType(1);
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Resize Height Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public void resizeWidthClicked()
    {
        try {
            String x = resizeWidthNum.getText();
            DisplayImage.setValue(x);
            DisplayImage.setPath("src/sample/wait.jpg");
            DisplayImage.setSeamCarving(seamCarving);
            DisplayImage.setType(2);
            FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("DisplayImage.fxml"));
            Parent P=(Parent) fxmlloader.load();
            Stage stage=new Stage();
            stage.setTitle("Resize width Image");
            stage.setScene(new Scene(P));
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }


}
