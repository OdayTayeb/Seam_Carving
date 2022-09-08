package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.io.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TextCompress implements Initializable {

    @FXML
    private TextField uncompressText;

    @FXML
    private TextField compressText;

    private String path;
    private String FileContent;

    void setPath(String x){
        path = "";
        for (int i=0;i<x.length();i++)
        {
            if (x.charAt(i)==34)
                continue;
            path+=x.charAt(i);
            if (x.charAt(i)==92)
                path+=x.charAt(i);
        }
    }

    public void readFile() {
        FileContent = "";
        try {

            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                FileContent += st;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeFile(String text){
        try {
            String P = path+"2";
            FileWriter fWriter = new FileWriter(P);
            fWriter.write(text);
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ShowFile(){
        String p = path + "2";
        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", p );
        try {
            pb.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void compress() {
        setPath(uncompressText.getText());
        readFile();
        String res=RLE.Compress(FileContent);
        writeFile(res);
        ShowFile();
    }

    public void decompress(){
        setPath(compressText.getText());
        readFile();
        String res=RLE.DeCompress(FileContent);
        writeFile(res);
        ShowFile();
    }
}
