package sample;

import javafx.util.Pair;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class SeamCarving {
    private BufferedImage Copy;
    private BufferedImage image;
    private int Energy[][];

    private int dpHor[][],dpVer[][];
    private ArrayList<Pair<Integer,Integer> > seamHor = new ArrayList<Pair<Integer, Integer>>();
    private ArrayList<Pair<Integer,Integer> > seamVer = new ArrayList<Pair<Integer, Integer>>();
    private boolean isResizing = false;

    SeamCarving(BufferedImage image){
        this.image = image;
        this.Copy=new BufferedImage(image.getWidth(),image.getHeight(),TYPE_3BYTE_BGR);
        for (int i=0;i<image.getHeight();i++)
            for (int j=0;j<image.getWidth();j++)
                Copy.setRGB(j,i,image.getRGB(j,i));
        Energy = new int[image.getHeight()+10][image.getWidth()+10];
        dpHor = new int[image.getHeight()+10][image.getWidth()+10];
        dpVer = new int[image.getHeight()+10][image.getWidth()+10];
        for (int i=0;i<image.getHeight()+5;i++)
            for (int j=0;j<image.getWidth()+5;j++)
                dpVer[i][j]=-1;
        for (int i=0;i<image.getHeight()+5;i++)
            for (int j=0;j<image.getWidth()+5;j++)
                dpHor[i][j]=-1;
    }

    private void CalculateEnergyMatrix() {
        for (int i=0;i<image.getHeight();i++)
        {
            for (int j=0;j<image.getWidth();j++)
            {
                Color cLeft=new Color(image.getRGB(Math.max(j-1,0),i));
                Color cRight=new Color(image.getRGB(Math.min(j+1,image.getWidth()-1),i));
                Color cUp=new Color(image.getRGB(j,Math.max(i-1,0)));
                Color cDown=new Color(image.getRGB(j,Math.min(i+1,image.getHeight()-1)));
                int HorizentalEnergy = Square(cLeft.getRed()-cRight.getRed())+Square(cLeft.getBlue()-cRight.getBlue())+Square(cLeft.getGreen()-cRight.getGreen());
                int VerticalEnergy = Square(cUp.getRed()-cDown.getRed())+Square(cUp.getBlue()-cDown.getBlue())+Square(cUp.getGreen()-cDown.getGreen());
                Energy[i][j]=HorizentalEnergy+VerticalEnergy;
            }
        }
    }

    public void writeEnergyImage() {
        CalculateEnergyMatrix();
        BufferedImage b= new BufferedImage(image.getWidth(),image.getHeight(),3);
        for (int i=0;i<image.getHeight();i++)
            for (int j=0;j<image.getWidth();j++)
                b.setRGB(j, i, (int)Energy[i][j]<<16 | (int)Energy[i][j] << 8 | (int)Energy[i][j]);
        try {
            File f = new File("src/sample/EnergyImage.png");
            ImageIO.write(b,"png",f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int Square(int x){
        return x*x;
    }

    // Horizental

    private int CalculateLowEnergySeamHor(int i,int j) {
        if (j==image.getWidth())
            return 0;
        if (dpHor[i][j]!=-1)
            return dpHor[i][j];
        int res1=1000000000,res2=1000000000,res3=1000000000;
        if (i-1>=0)
            res1=CalculateLowEnergySeamHor(i-1,j+1)+Energy[i][j];
        res2=CalculateLowEnergySeamHor(i,j+1)+Energy[i][j];
        if (i+1<image.getHeight())
            res3=CalculateLowEnergySeamHor(i+1,j+1)+Energy[i][j];
        return dpHor[i][j]=Math.min(Math.min(res1,res2),res3);
    }

    private void getLowEnergySeamHor(int i,int j) {
        if (j==image.getWidth())
            return ;
        seamHor.add(new Pair(i,j));
        int res1=1000000000,res2=1000000000,res3=1000000000;
        if (i-1>=0)
            res1=CalculateLowEnergySeamHor(i-1,j+1)+Energy[i][j];
        res2=CalculateLowEnergySeamHor(i,j+1)+Energy[i][j];
        if (i+1<image.getHeight())
            res3=CalculateLowEnergySeamHor(i+1,j+1)+Energy[i][j];
        if (res1==dpHor[i][j])
            getLowEnergySeamHor(i-1,j+1);
        else if (res2==dpHor[i][j])
            getLowEnergySeamHor(i,j+1);
        else getLowEnergySeamHor(i+1,j+1);
    }

    private int fillSeamHor(){
        int minSeamSum=1000000000,i;
        for (i=0;i<image.getHeight();i++)
            minSeamSum = Math.min( CalculateLowEnergySeamHor(i, 0),minSeamSum );
        for (i=0;i<image.getHeight();i++)
            if (minSeamSum == CalculateLowEnergySeamHor(i,0))
                break;
        getLowEnergySeamHor(i,0);
        return minSeamSum;
    }

    public void writeImageWithHorSeam() {
        fillSeamHor();
        if (!isResizing) {
        BufferedImage ret = new BufferedImage(image.getWidth(),image.getHeight(),TYPE_3BYTE_BGR);
        for (int i=0;i<image.getHeight();i++)
            for (int j=0;j<image.getWidth();j++)
                ret.setRGB(j,i,image.getRGB(j,i));
        Color red = new Color(255,0,0);
        for (Pair <Integer,Integer> p : seamHor)
        {
            int x=p.getKey();
            int y=p.getValue();
            ret.setRGB(y,x,red.getRGB());
        }
            try {
                File f = new File("src/sample/SeamHorImage.png");
                ImageIO.write(ret, "png", f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeImageAfterDeleteHorSeam() {

        BufferedImage DeletedSeamImage = DeleteHorSeam();

        if (isResizing)
            image = DeletedSeamImage;
        try {
            File f;
            if (!isResizing)
                f = new File("src/sample/ImageAfterDeleteHorSeam.png");
            else f = new File("src/sample/ImageAfterDeleteHorSeamResizing.png");
            ImageIO.write(DeletedSeamImage,"png",f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage DeleteHorSeam() {
        BufferedImage ret = new BufferedImage(image.getWidth(),image.getHeight()-1,TYPE_3BYTE_BGR);
        int index=0;
        for (int j=0;j<image.getWidth();j++)
        {
            int x=seamHor.get(index).getKey();
            for (int i=0;i<x;i++)
                    ret.setRGB(j, i, image.getRGB(j, i));
            for (int i=x+1;i<image.getHeight();i++)
                ret.setRGB(j,i-1,image.getRGB(j,i));
            index++;
        }
        return ret;
    }

    public int ResizeImageHeight(int newHeight) {
        if (newHeight>image.getHeight())
            return -1;
        isResizing=true;
        int Seams=image.getHeight()-newHeight;
        while (Seams!=0) {
            for (int i = 0; i < image.getHeight()+5; i++)
                for (int j = 0; j < image.getWidth()+5; j++)
                    dpHor[i][j] = -1;
            seamHor.clear();
            CalculateEnergyMatrix();
            writeImageWithHorSeam();
            writeImageAfterDeleteHorSeam();
            Seams--;
        }
        isResizing=false;
        return 1;
    }


    // Vertical

    private int CalculateLowEnergySeamVer(int i,int j)
    {
        if (i==image.getHeight())
            return 0;
        if (dpVer[i][j]!=-1)
            return dpVer[i][j];
        int res1=1000000000,res2=1000000000,res3=1000000000;
        if (j-1>=0)
            res1=CalculateLowEnergySeamVer(i+1,j-1)+Energy[i][j];
        res2=CalculateLowEnergySeamVer(i+1,j)+Energy[i][j];
        if (j+1<image.getWidth())
            res3=CalculateLowEnergySeamVer(i+1,j+1)+Energy[i][j];
        return dpVer[i][j]=Math.min(Math.min(res1,res2),res3);
    }

    private void getLowEnergySeamVer(int i,int j) {
        if (i==image.getHeight())
            return ;
        seamVer.add(new Pair(i,j));
        int res1=1000000000,res2=1000000000,res3=1000000000;
        if (j-1>=0)
            res1=CalculateLowEnergySeamVer(i+1,j-1)+Energy[i][j];
        res2=CalculateLowEnergySeamVer(i+1,j)+Energy[i][j];
        if (j+1<image.getWidth())
            res3=CalculateLowEnergySeamVer(i+1,j+1)+Energy[i][j];
        if (res1==dpVer[i][j])
            getLowEnergySeamVer(i+1,j-1);
        else if (res2==dpVer[i][j])
            getLowEnergySeamVer(i+1,j);
        else getLowEnergySeamVer(i+1,j+1);
    }

    private int fillSeamVer(){
        int minSeamSum=1000000000,j;
        for (j=0;j<image.getWidth();j++)
            minSeamSum = Math.min( CalculateLowEnergySeamVer(0, j),minSeamSum );
        for (j=0;j<image.getWidth();j++)
            if (minSeamSum == CalculateLowEnergySeamVer(0, j))
                break;
        getLowEnergySeamVer(0,j);
        return minSeamSum;
    }

    public void writeImageWithVerSeam()
    {
        fillSeamVer();
        if (!isResizing) {
        BufferedImage ret = new BufferedImage(image.getWidth(),image.getHeight(),TYPE_3BYTE_BGR);
        for (int i=0;i<image.getHeight();i++)
            for (int j=0;j<image.getWidth();j++)
                ret.setRGB(j,i,image.getRGB(j,i));
        Color red = new Color(255,0,0);
            for (Pair <Integer,Integer> p : seamVer)
            {
                int x=p.getKey();
                int y=p.getValue();
                ret.setRGB(y,x,red.getRGB());
            }
            try {
                File f = new File("src/sample/SeamVerImage.png");
                ImageIO.write(ret, "png", f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeImageAfterDeleteVerSeam()
    {

        BufferedImage DeletedSeamImage = DeleteVerSeam();

        if (isResizing)
            image = DeletedSeamImage;
        try {
            File f;
            if (!isResizing)
                f = new File("src/sample/ImageAfterDeleteVerSeam.png");
            else f = new File("src/sample/ImageAfterDeleteVerSeamResizing.png");
            ImageIO.write(DeletedSeamImage,"png",f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage DeleteVerSeam()
    {
        BufferedImage ret = new BufferedImage(image.getWidth()-1,image.getHeight(),TYPE_3BYTE_BGR);
        int index=0;
        for (int i=0;i<image.getHeight();i++)
        {
            int x=seamVer.get(index).getValue();
            for (int j=0;j<x;j++)
                ret.setRGB(j,i,image.getRGB(j,i));
            for (int j=x+1;j<image.getWidth();j++)
                ret.setRGB(j-1,i,image.getRGB(j,i));
            index++;
        }
        return ret;
    }

    public int ResizeImageWidth(int newWidth)
    {
        if (newWidth>image.getWidth())
            return -1;
        isResizing=true;
        int Seams=image.getWidth()-newWidth;
        while (Seams!=0) {
            System.out.println(Seams);
            for (int i = 0; i < image.getHeight()+5; i++)
                for (int j = 0; j < image.getWidth()+5; j++)
                    dpVer[i][j] = -1;
            seamVer.clear();
            CalculateEnergyMatrix();
            writeImageWithVerSeam();
            writeImageAfterDeleteVerSeam();
            Seams--;
        }
        isResizing=false;
        return 1;
    }

    public int ResizeImage(int numOfTimes)
    {
        if (numOfTimes>Math.max(image.getHeight(),image.getWidth())-1 || numOfTimes <= 0)
            return -1;
        int ret=0;
        isResizing=true;
        int Seams=numOfTimes;
        while (Seams!=0){
            for (int i = 0; i < image.getHeight()+5; i++)
                for (int j = 0; j < image.getWidth()+5; j++) {
                    dpVer[i][j] = -1;
                    dpHor[i][j]=-1;
                }
            seamVer.clear();
            seamHor.clear();
            CalculateEnergyMatrix();
            if (fillSeamVer() > fillSeamHor()) {
                writeImageAfterDeleteHorSeam();
                ret=0;
            }
            else {
                writeImageAfterDeleteVerSeam();
                ret=1;
            }
            Seams--;
        }
        isResizing=false;
        return ret;
    }

    public void RestoreImage()
    {
        image = new BufferedImage(Copy.getWidth(),Copy.getHeight(),TYPE_3BYTE_BGR);
        for (int i=0;i<image.getHeight();i++)
            for (int j=0;j<image.getWidth();j++)
                image.setRGB(j,i,Copy.getRGB(j,i));
    }
}
