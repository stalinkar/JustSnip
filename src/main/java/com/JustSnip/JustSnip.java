package com.JustSnip;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JustSnip {

    static String strJustSnipPath = "";
    static String strFileName = "";
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

    public void SetFileName(String strJustSnipPath) {
        File theDir = new File(strJustSnipPath);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        if (file == null || !file.toPath().toString().contains(strJustSnipPath)) {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hhmmss");
            file = new File(strJustSnipPath + "\\" + strFileName + "-" + formatter.format(date) + ".docx");
        }
    }

    public void SaveImgInWord(String strImgFilePath) throws IOException, InvalidFormatException {
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

    public String TakeScreenShot() throws IOException, AWTException {
        String path = strJustSnipPath + "Shot.png";
        BufferedImage capture = robot.createScreenCapture(screenRect);
        ImageIO.write(capture, "png", new File(path));
        return path;
    }

}
