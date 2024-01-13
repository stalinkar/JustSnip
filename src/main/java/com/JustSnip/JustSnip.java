package com.JustSnip;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class JustSnip {

    static String strJustSnipPath = "";
    static String strFileName = "";
    static String strImgForVideoPath = "";
    static File file;
    int shotCounter = 0;
    Robot robot;
    Rectangle screenRect;
    private String strSavedFilePath;

    protected JustSnip(int intX, int intY, int intWidth, int intHeight) {
        screenRect = new Rectangle(intX, intY, intWidth, intHeight);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		SetFileName(strJustSnipPath);
    }

    protected JustSnip() {
        screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            robot = new Robot();
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//		SetFileName(strJustSnipPath);
    }

    public String getStrSavedFilePath() {
        return strSavedFilePath;
    }

    void SetFileName(String strJustSnipPath) {
        //strJustSnipPath = (strJustSnipPath.isEmpty())?System.getProperty("user.home")+"\\Documents\\JustSnip\\":strJustSnipPath;
        File theDir = new File(strJustSnipPath);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        if (file == null || !file.toPath().toString().contains(strJustSnipPath)) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hhmmss");
            //return formatter.format(date);
            file = new File(strJustSnipPath + "\\" + strFileName + "-" + formatter.format(date) + ".docx");
        }
    }

    void SaveImgInWord(String strImgFilePath) throws IOException, InvalidFormatException {
        SetFileName(strJustSnipPath);
        File imgFile = new File(strImgFilePath);
        XWPFParagraph xwpfParagraph;
        XWPFDocument xwpfDoc;
        if (file.exists()) {
            xwpfDoc = new XWPFDocument(Files.newInputStream(file.toPath()));
            List<XWPFParagraph> paragraphs = xwpfDoc.getParagraphs();
            xwpfParagraph = paragraphs.get(paragraphs.size() - 1);
        } else {
            xwpfDoc = new XWPFDocument();
            xwpfParagraph = xwpfDoc.createParagraph();
        }
        XWPFRun xwpfRun = xwpfParagraph.createRun();
//		shotCounter+=1;
//		//xwpfRun.setText(imgFile+"_"+shotCounter);
//		xwpfRun.setText(String.valueOf(shotCounter));
//		xwpfRun.addBreak();

        FileInputStream fileIn = new FileInputStream(strImgFilePath);
        xwpfRun.addPicture(fileIn, Document.PICTURE_TYPE_PNG, strImgFilePath, Units.toEMU(500), Units.toEMU(320));
        xwpfRun.addBreak();
        FileOutputStream out = new FileOutputStream(file);
        xwpfDoc.write(out);
        strSavedFilePath = file.getPath();
        out.close();
        xwpfDoc.close();
        fileIn.close();
        imgFile.delete();
    }

    String TakeScreenShot() throws IOException, AWTException {
        String path = strJustSnipPath + "Shot.png";
        BufferedImage capture = robot.createScreenCapture(screenRect);
        ImageIO.write(capture, "png", new File(path));
        return path;
    }

    void TakeScreenShot(long counter) throws IOException, AWTException {
        String index = "";
        switch (String.valueOf(counter).length()) {
            case 1:
                index = "000" + counter;
                break;
            case 2:
                index = "00" + counter;
                break;
            case 3:
                index = "0" + counter;
                break;
            default:
                index = "" + counter;
                break;
        }
        BufferedImage capture = robot.createScreenCapture(screenRect);
        ImageIO.write(capture, "png", new File(strImgForVideoPath + "\\" + index + ".png"));
    }

    String SaveImgInVideo() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hhmmss");
        String imageFolderPath = strJustSnipPath + strFileName;
        String outputVideoPath = strJustSnipPath + strFileName + "\\" + strFileName + "-" + formatter.format(date) + ".mp4";
        int frameRate = 4;
        try {
            File imageFolder = new File(imageFolderPath);
            File[] imageFiles = imageFolder.listFiles();

            if (imageFiles == null || imageFiles.length == 0) {
                return "No images found in the specified folder.";
            } else {
                Arrays.sort(imageFiles);
            }

            // Get the dimensions of the first image
            BufferedImage firstImage = ImageIO.read(imageFiles[0]);
            int width = firstImage.getWidth();
            int height = firstImage.getHeight();

            // Create a frame recorder
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideoPath, width, height);
            recorder.setVideoCodecName("libopenh264");
            recorder.setFormat("mp4");
            recorder.setFrameRate(frameRate);
            recorder.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();

            // Read each image and add it to the video
            for (File imageFile : imageFiles) {
                if (!imageFile.getPath().endsWith(".mp4")) {
                    BufferedImage image = ImageIO.read(imageFile);
                    Frame frame = converter.convert(image);
                    recorder.record(frame);
                    imageFile.delete();
                }
            }

            recorder.stop();
            recorder.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputVideoPath;
    }

}
