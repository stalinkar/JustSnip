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
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

public class JustSnip {

    static String strJustSnipPath = "";
    static String strFileName = "";
    static String strImgForVideoPath = "";
    static File file;
    Robot robot;
    Rectangle screenRect;
    private String strSavedFilePath;

    // Shared date formatter to avoid creating multiple instances
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("ddMMyyyy_hhmmss");

    protected JustSnip(int intX, int intY, int intWidth, int intHeight) {
        screenRect = new Rectangle(intX, intY, intWidth, intHeight);
        initializeRobot();
    }

    protected JustSnip() {
        screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        initializeRobot();
    }

    private void initializeRobot() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public String getStrSavedFilePath() {
        return strSavedFilePath;
    }

    void setFileName(String strJustSnipPath) {
        createDirectoryIfNotExists(strJustSnipPath);
        if (file == null || !file.toPath().toString().contains(strJustSnipPath)) {
            file = new File(strJustSnipPath + "/" + strFileName + "-" + DATE_FORMATTER.format(new Date()) + ".docx");
        }
    }

    private void createDirectoryIfNotExists(String path) {
        File theDir = new File(path);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    void saveImgInWord(String strImgFilePath) throws IOException, InvalidFormatException {
        setFileName(strJustSnipPath);
        File imgFile = new File(strImgFilePath);
        try (XWPFDocument xwpfDoc = file.exists() ? new XWPFDocument(Files.newInputStream(file.toPath())) : new XWPFDocument();
             FileInputStream fileIn = new FileInputStream(strImgFilePath);
             FileOutputStream out = new FileOutputStream(file)) {
            XWPFParagraph xwpfParagraph = xwpfDoc.createParagraph();
            XWPFRun xwpfRun = xwpfParagraph.createRun();
            xwpfRun.addPicture(fileIn, Document.PICTURE_TYPE_PNG, strImgFilePath, Units.toEMU(500), Units.toEMU(320));
            xwpfRun.addBreak();
            xwpfDoc.write(out);
        }
        strSavedFilePath = file.getPath();
        imgFile.delete();
    }

    String takeScreenShot() throws IOException {
        return saveScreenshotToFile(strJustSnipPath + "Shot.png");
    }

    void takeScreenShot(long counter) throws IOException {
        String index = String.format("%04d", counter);
        saveScreenshotToFile(strImgForVideoPath + "/" + index + ".png");
    }

    private String saveScreenshotToFile(String path) throws IOException {
        BufferedImage capture = robot.createScreenCapture(screenRect);
        ImageIO.write(capture, "png", new File(path));
        return path;
    }

    /**
     * Refactored: saveImgInVideo() for background execution with optional progress reporting.
     * Returns the output video path or throws an exception for error handling in the UI.
     */
    public String saveImgInVideo(Consumer<Integer> progressCallback) throws Exception {
        String imageFolderPath = strJustSnipPath + strFileName;
        String outputVideoPath = strJustSnipPath + strFileName + "/" + strFileName + "-" + DATE_FORMATTER.format(new Date()) + ".mp4";
        int frameRate = 4;

        File[] imageFiles = getImageFiles(imageFolderPath);
        if (imageFiles == null || imageFiles.length == 0) {
            throw new IOException("No images found in the specified folder.");
        }
        Arrays.sort(imageFiles);

        BufferedImage firstImage = ImageIO.read(imageFiles[0]);
        int width = firstImage.getWidth();
        int height = firstImage.getHeight();

        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideoPath, width, height)) {
            recorder.setVideoCodecName("libopenh264");
            recorder.setFormat("mp4");
            recorder.setFrameRate(frameRate);
            recorder.start();

            Java2DFrameConverter converter = new Java2DFrameConverter();
            int totalImages = imageFiles.length;
            int processedImages = 0;

            for (File imageFile : imageFiles) {
                if (imageFile.getPath().endsWith(".png")) {
                    BufferedImage image = ImageIO.read(imageFile);
                    Frame frame = converter.convert(image);
                    recorder.record(frame);
                    imageFile.delete();
                }
                processedImages++;
                if (progressCallback != null) {
                    int percent = (int) (((double) processedImages / totalImages) * 100);
                    progressCallback.accept(percent); // For UI progress updates
                }
            }
        } catch (Exception e) {
            throw new Exception("Error during video creation: " + e.getMessage(), e);
        }
        return outputVideoPath;
    }
    private File[] getImageFiles(String imageFolderPath) {
        File imageFolder = new File(imageFolderPath);
        return imageFolder.listFiles();
    }
}