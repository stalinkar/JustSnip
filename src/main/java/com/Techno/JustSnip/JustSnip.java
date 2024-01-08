package com.Techno.JustSnip;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class JustSnip {
	
	static String strJustSnipPath = "";
	private File file;
	private XWPFParagraph xwpfParagraph;
	private XWPFDocument xwpfDoc;
	private String strSavedFilePath;
	int shotCounter=0;
	Robot robot;
	
	Rectangle rectangle;

	public String getStrSavedFilePath() {
		return strSavedFilePath;
	}
	Rectangle screenRect;
	
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
	
	public void SetFileName(String strJustSnipPath) {
		strJustSnipPath = (strJustSnipPath.isEmpty())?System.getProperty("user.home")+"\\Documents\\JustSnip\\":strJustSnipPath;
		File theDir = new File(strJustSnipPath);
		if (!theDir.exists()&& !strJustSnipPath.endsWith("-")){
		    theDir.mkdirs();
		}
		if(file==null || !file.toPath().toString().contains(strJustSnipPath)) {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hhmmss");
			//return formatter.format(date);
			file = new File(strJustSnipPath+formatter.format(date)+".docx");
		}
	}

//	public static void main(String[] args) throws AWTException, IOException, InvalidFormatException {
//		JustSnip localObj = new JustSnip();
//		String strImgFilePath = localObj.TakeScreenShot();
//		localObj.SaveImgInWord(strImgFilePath);
//	}

	public void SaveImgInWord(String strImgFilePath) throws FileNotFoundException, IOException, InvalidFormatException {
		SetFileName(strJustSnipPath);
		File imgFile = new File(strImgFilePath);
		if (file.exists()) {
			xwpfDoc = new XWPFDocument(new FileInputStream(file));
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
		String path = strJustSnipPath+"Shot.png";
		BufferedImage capture = robot.createScreenCapture(screenRect);
		ImageIO.write(capture, "png", new File(path));
		return path;
	}

}
