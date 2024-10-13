package com.JustSnip;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class JustSnipRunner {
    private static JustSnipApp window;
    static boolean threadIsActive = true;

    public static void main(String[] args) {
        Thread thread1 = new Thread(()-> {
            try{
                window = new JustSnipApp();
                window.frmJustSnip.setVisible(true);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> EventQueue.invokeLater(() ->{
            boolean flag = true;
            while (true){
                while (threadIsActive){
                    try{
                        Thread.sleep(5000);
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_hhmmss");int frameRate = 4;
                        String imageFolderPath = "";
                        while (imageFolderPath.isEmpty()){
                            imageFolderPath = JustSnip.strJustSnipPath + JustSnip.strFileName;
                        }
                        String outputVideoPath = JustSnip.strJustSnipPath + JustSnip.strFileName + "\\" + JustSnip.strFileName + "-" + formatter.format(date) + ".mp4";
                        File imageFolder = new File(imageFolderPath);
                        File[] imageFiles = imageFolder.listFiles();
                        Arrays.sort(imageFiles);
                        if(!imageFiles[0].getPath().endsWith(".mp4")){
                            flag = true;
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
                            while (flag){
                                Thread.sleep(1000);
                                imageFiles = imageFolder.listFiles();
                                Arrays.sort(imageFiles);
                                if (imageFiles[0].getPath().endsWith(".mp4")){
                                    flag = false;
                                    threadIsActive = false;
                                }
                                // Read each image and add it to the video
                                for (File imageFile : imageFiles) {
                                    if (!imageFile.getPath().endsWith(".mp4")) {
                                        recorder.record(converter.convert(ImageIO.read(imageFile)));
                                        imageFile.delete();
                                    } else {
                                        break;
                                    }
                                }
                            }
                            recorder.stop();
                            recorder.release();
                            window.txtMessage.setText("File saved at " + outputVideoPath);
                        }
                    } catch (IOException | InterruptedException e){
                        throw new RuntimeException(e);
                    }
                }
            }
        }));
        thread1.start();
        thread2.start();
    }
}
