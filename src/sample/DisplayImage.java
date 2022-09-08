package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DisplayImage implements Initializable {
    @FXML
    private ImageView myImage;
    @FXML
    private Text size;

    private static int type=-1;
    private static String path;
    private static String value;
    private static SeamCarving seamCarving;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        myImage.setImage(image);
        String s="";
        s+= image.getWidth();
        s+="*";
        s+= image.getHeight();
        size.setText(s);
        if (type == 0)
            Resize();
        else if (type == 1)
            ResizeHeight();
        else if (type == 2)
            ResizeWidth();
    }

    public static void setPath(String p)
    {
        path = p;
    }

    public static void setValue(String val){
        value=val;
    }

    public void Resize()
    {
        try {
            final int num = Integer.parseInt(value);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    seamCarving.RestoreImage();
                    int val = seamCarving.ResizeImage(num);
                    if (val==-1)
                        updateImage("src/sample/failed.jpg");
                    else if (val==0)
                        updateImage("src/sample/ImageAfterDeleteHorSeamResizing.png");
                    else updateImage("src/sample/ImageAfterDeleteVerSeamResizing.png");
                }
            });
            t.start();
        }
        catch (Exception e){
            updateImage("src/sample/failed.jpg");
        }
    }

    public void ResizeHeight()
    {
        try {
            final int num = Integer.parseInt(value);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    seamCarving.RestoreImage();
                    int val = seamCarving.ResizeImageHeight(num);
                    if (val==-1)
                        updateImage("src/sample/failed.jpg");
                    else updateImage("src/sample/ImageAfterDeleteHorSeamResizing.png");
                }
            });
            t.start();
        }
        catch (Exception e){
            updateImage("src/sample/failed.jpg");
        }
    }

    public void ResizeWidth()
    {
        try {
            final int num = Integer.parseInt(value);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    seamCarving.RestoreImage();
                    int val = seamCarving.ResizeImageWidth(num);
                    if (val==-1)
                        updateImage("src/sample/failed.jpg");
                    else updateImage("src/sample/ImageAfterDeleteVerSeamResizing.png");
                }
            });
            t.start();
        }
        catch (Exception e){
            updateImage("src/sample/failed.jpg");
        }
    }



    public static void setSeamCarving(SeamCarving x){
        seamCarving = x;
    }

    public void updateImage(String p)
    {
        File file = new File(p);
        Image image = new Image(file.toURI().toString());
        myImage.setImage(image);
        String s="";
        s+= image.getWidth();
        s+="*";
        s+= image.getHeight();
        size.setText(s);
    }

    public static void setType(int t){
        type=t;
    }
}
