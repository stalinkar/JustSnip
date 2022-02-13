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

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class JustSnip {
	
	private static final String strJustSnipPath = System.getProperty("user.home")+"\\Documents\\";
	private File file;
	private XWPFParagraph xwpfParagraph;
	private XWPFDocument xwpfDoc;
	private int intX, intY, intWidth, intHeight;
	private String strSavedFilePath;
	int shotCounter=0;

	public String getStrSavedFilePath() {
		return strSavedFilePath;
	}
	Rectangle screenRect;
	
	protected JustSnip(int intX, int intY, int intWidth, int intHeight) {
		this.intX = intX;
		this.intY = intY;
		this.intWidth = intWidth;
		this.intHeight = intHeight;
		screenRect = new Rectangle(intX, intY, intWidth, intHeight);
		file = new File(strJustSnipPath+SetFileName()+".docx");
	}	
	
	protected JustSnip() {
		screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		file = new File(strJustSnipPath+SetFileName()+".docx");
	}
	
	public String SetFileName() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hhmmss");
		return formatter.format(date);
	}

	public static void main(String[] args) throws AWTException, IOException, InvalidFormatException {
		JustSnip localObj = new JustSnip();
		String strImgFilePath = localObj.TakeScreenShot();
		localObj.SaveImgInWord(strImgFilePath);
	}

	public void SaveImgInWord(String strImgFilePath) throws FileNotFoundException, IOException, InvalidFormatException {
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
		shotCounter+=1;
		xwpfRun.setText(imgFile+"_"+shotCounter);
		xwpfRun.addBreak();

		FileInputStream fileIn = new FileInputStream(strImgFilePath);
		xwpfRun.addPicture(fileIn, Document.PICTURE_TYPE_PNG, strImgFilePath, Units.toEMU(400), Units.toEMU(200));
		FileOutputStream out = new FileOutputStream(file);
		xwpfDoc.write(out);
		String[] strFileName = file.getName().split("[\\s]");
		out.close();
		xwpfDoc.close();
		strSavedFilePath = strJustSnipPath+"JustSnip\\"+strFileName[strFileName.length-1];
		FileUtils.moveFile(file, new File(strSavedFilePath));
		fileIn.close();
		imgFile.delete();
	}
	public String TakeScreenShot() throws IOException, AWTException {
		String path = strJustSnipPath+"Shot.png";
		BufferedImage capture = new Robot().createScreenCapture(screenRect);
		ImageIO.write(capture, "png", new File(path));
		return path;
	}

}
