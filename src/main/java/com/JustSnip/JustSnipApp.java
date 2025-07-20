package com.JustSnip;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.List;

public class JustSnipApp {

    private final String strPropertyPath = System.getProperty("user.home") + "\\Documents\\JustSnip\\Config\\";
    private final String strPropertyFile = "justsnip.config";
    protected String strTargetPath = System.getProperty("user.home") + "\\Documents\\JustSnip\\";
    protected String strTargetFileName = "ScreenShot";
    protected long interval = 2000;

    long counter = 0;
    JustSnip objJustSnip;
    private JFrame frmJustSnip;
    private JTextField txtMessage;
    private JButton btnJustSnip;
    private JButton btnAutoSnip;
    private JButton btnRecord;
    private JTextField txtTargetFolder;
    private JTextField txtFileName;
    private JSpinner spinnerCount;
    private File file;
    private Properties prop;
    private JButton btnHelp;
    private JButton btnSave;
    private JButton btnOpenFile;
    private JProgressBar progressBar;

    /**
     * Create the application.
     */
    public JustSnipApp() {
        setProperty();
        initialize();
        objJustSnip = new JustSnip();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JustSnipApp window = new JustSnipApp();
                window.frmJustSnip.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Setting Target path and File Name for the file.
     */
    private void setProperty() {
        File theDir = new File(strPropertyPath);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        prop = new Properties();
        file = new File(strPropertyPath + strPropertyFile);
        try (FileInputStream fis = new FileInputStream(file)) {
            prop.load(fis);
            strTargetPath = prop.getProperty("targetPath", strTargetPath);
            strTargetFileName = prop.getProperty("targetFileName", strTargetFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmJustSnip = new JFrame();
        frmJustSnip.setTitle("Just Snip (Author - Stalin Kar)");
        frmJustSnip.setBounds(100, 100, 467, 306);
        frmJustSnip.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmJustSnip.setResizable(false);
        frmJustSnip.getContentPane().setLayout(null);

        addComponentsToFrame();
        addActionListeners();
    }

    private void addComponentsToFrame() {
        btnJustSnip = new JButton("Snip!");
        btnJustSnip.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnJustSnip.setBounds(10, 10, 90, 53);
        frmJustSnip.getContentPane().add(btnJustSnip);

        final String strDefaultMsg = "File will be saved in ";
        txtMessage = new JTextField(strDefaultMsg + strTargetPath + "\\" + strTargetFileName + "-{timestamp}.docx");
        txtMessage.setBounds(10, 230, 433, 20);
        txtMessage.setEditable(false);
        frmJustSnip.getContentPane().add(txtMessage);
        txtMessage.setColumns(10);

        btnAutoSnip = new JButton("Auto Snip!");
        btnAutoSnip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnAutoSnip.setBounds(110, 10, 90, 53);
        frmJustSnip.getContentPane().add(btnAutoSnip);

        btnRecord = new JButton("Record");
        btnRecord.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnRecord.setBounds(210, 10, 90, 53);
        frmJustSnip.getContentPane().add(btnRecord);

        JLabel lblTargetFolder = new JLabel("Target Folder:");
        lblTargetFolder.setBounds(10, 73, 180, 13);
        frmJustSnip.getContentPane().add(lblTargetFolder);

        JLabel lblTargetFileName = new JLabel("Screenshot File Name:");
        lblTargetFileName.setBounds(10, 136, 180, 13);
        frmJustSnip.getContentPane().add(lblTargetFileName);

        txtTargetFolder = new JTextField();
        txtTargetFolder.setToolTipText("Please enter the folder/path of the file need to be saved in");
        txtTargetFolder.setBounds(10, 97, 433, 29);
        txtTargetFolder.setColumns(10);
        txtTargetFolder.setText(strTargetPath);
        frmJustSnip.getContentPane().add(txtTargetFolder);

        txtFileName = new JTextField();
        txtFileName.setToolTipText("Please enter the Filename need to save the Screenshots");
        txtFileName.setColumns(10);
        txtFileName.setBounds(10, 159, 433, 29);
        txtFileName.setText(strTargetFileName);
        frmJustSnip.getContentPane().add(txtFileName);

        JLabel lblNewLabel = new JLabel("Auto Snip Interval:");
        lblNewLabel.setBounds(310, 10, 133, 13);
        frmJustSnip.getContentPane().add(lblNewLabel);

        spinnerCount = new JSpinner();
        spinnerCount.setModel(new SpinnerNumberModel(2, 1, 60, 1));
        spinnerCount.setBounds(310, 34, 35, 29);
        frmJustSnip.getContentPane().add(spinnerCount);

        JLabel lblNewLabel_1 = new JLabel("Second(s) 1-60");
        lblNewLabel_1.setBounds(348, 42, 95, 13);
        frmJustSnip.getContentPane().add(lblNewLabel_1);

        btnSave = new JButton("Save File Path");
        btnSave.setBounds(20, 198, 133, 21);
        frmJustSnip.getContentPane().add(btnSave);

        btnHelp = new JButton("Help");
        btnHelp.setBounds(310, 198, 133, 21);
        frmJustSnip.getContentPane().add(btnHelp);

        btnOpenFile = new JButton("Open Target File");
        btnOpenFile.setBounds(163, 198, 137, 21);
        frmJustSnip.getContentPane().add(btnOpenFile);

//        frmJustSnip.getContentPane().setLayout(null);

        progressBar = new JProgressBar();
        progressBar.setBounds(10, 255, 433, 10);
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setString("0%");
        progressBar.setForeground(Color.BLUE);
        progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        frmJustSnip.getContentPane().add(progressBar);
    }

    private void addActionListeners() {
        btnJustSnip.addActionListener(e -> {
            setTargetFolderAndFilePath();
            try {
                frmJustSnip.setVisible(false);
                Thread.sleep(500);
                JustSnip.strJustSnipPath = strTargetPath;
                JustSnip.strFileName = strTargetFileName;
                objJustSnip.saveImgInWord(objJustSnip.takeScreenShot());
                txtMessage.setText("File saved at " + objJustSnip.getStrSavedFilePath());
                frmJustSnip.setVisible(true);
            } catch (IOException | InvalidFormatException | InterruptedException e1) {
                setErrorMessagePopUp(e1);
            }
        });

        btnAutoSnip.addActionListener(e -> {
            if (btnAutoSnip.getText().equals("Auto Snip!")) {
                setTargetFolderAndFilePath();
                btnAutoSnip.setText("Stop!");
                btnJustSnip.setEnabled(false);
                btnRecord.setEnabled(false);
                frmJustSnip.setExtendedState(JFrame.ICONIFIED);
                try {
                    JustSnip.file = null;
                    while (frmJustSnip.getExtendedState() == JFrame.ICONIFIED) {
                        Thread.sleep(interval);
                        JustSnip.strJustSnipPath = strTargetPath;
                        JustSnip.strFileName = strTargetFileName;
                        objJustSnip.saveImgInWord(objJustSnip.takeScreenShot());
                    }
                } catch (IOException | InvalidFormatException | InterruptedException e1) {
                    setErrorMessagePopUp(e1);
                }
            } else if (btnAutoSnip.getText().startsWith("Resume")) {
                frmJustSnip.setExtendedState(JFrame.ICONIFIED);
                try {
                    while (frmJustSnip.getExtendedState() == JFrame.ICONIFIED) {
                        objJustSnip.takeScreenShot(counter++);
                    }
                } catch (IOException e1) {
                    setErrorMessagePopUp(e1);
                }
            } else {
                txtMessage.setText("File saved at " + objJustSnip.getStrSavedFilePath());
                btnAutoSnip.setText("Auto Snip!");
                btnJustSnip.setEnabled(true);
                btnRecord.setEnabled(true);
            }
        });

        btnRecord.addActionListener(e -> {
            if (btnRecord.getText().equals("Record")) {
                setTargetFolderAndFilePath();
                btnRecord.setBounds(180, 10, 90, 53);
                btnRecord.setText("Stop!");
                btnJustSnip.setVisible(false);
                btnJustSnip.setEnabled(false);
                btnAutoSnip.setEnabled(false);
                frmJustSnip.setExtendedState(JFrame.ICONIFIED);
                try {
                    JustSnip.file = null;
                    Thread.sleep(2000);
                    counter = 0;
                    JustSnip.strJustSnipPath = strTargetPath;
                    JustSnip.strFileName = strTargetFileName;
                    JustSnip.strImgForVideoPath = strTargetPath + "\\" + strTargetFileName;
                    File theDir = new File(strTargetPath + "\\" + strTargetFileName);
                    if (!theDir.exists()) {
                        theDir.mkdirs();
                    }
                    while (frmJustSnip.getExtendedState() == JFrame.ICONIFIED) {
                        objJustSnip.takeScreenShot(counter++);
                    }
                    btnAutoSnip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    btnAutoSnip.setBounds(50, 10, 90, 53);
                    btnAutoSnip.setText("Resume");
                    btnAutoSnip.setEnabled(true);
                } catch (IOException | InterruptedException e1) {
                    setErrorMessagePopUp(e1);
                }
            } else {
                progressBar.setVisible(true);
                btnAutoSnip.setBounds(110, 10, 90, 53);
                btnAutoSnip.setText("Auto Snip!");
                btnAutoSnip.setEnabled(false);
                btnRecord.setBounds(210, 10, 90, 53);
                btnRecord.setText("Record");
                btnRecord.setEnabled(false); // Disable until done
                btnJustSnip.setVisible(true);
                txtMessage.setText("Generating video, please wait...");
//                String strVideoPath = objJustSnip.saveImgInVideo();
//                txtMessage.setText("File saved at " + strVideoPath);
                // Start video generation in background
                new SwingWorker<String, Integer>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        return objJustSnip.saveImgInVideo(this::publish); // publish progress
                    }
                    @Override
                    protected void process(List<Integer> chunks) {
                        int latestProgress = chunks.get(chunks.size() - 1);
                        progressBar.setValue(latestProgress);
                        progressBar.setString(latestProgress + "%");
                    }
                    @Override
                    protected void done() {
                        try {
                            String videoPath = get();
                            txtMessage.setText("File saved at " + videoPath);
                        } catch (Exception ex) {
                            txtMessage.setText("Error generating video: " + ex.getMessage());
                        }
                        btnJustSnip.setEnabled(true);
                        btnAutoSnip.setEnabled(true);
                        btnRecord.setEnabled(true);
                        progressBar.setVisible(false);
                    }
                }.execute();
            }
        });

        btnSave.addActionListener(e -> saveFilePath());

        btnHelp.addActionListener(e -> {
            String strMsg = "Author      : Stalin Kar\n\n" + "This is a supporting tool for taking screenshots and recording\nscreen as part of Proof testing.";
            JOptionPane.showMessageDialog(frmJustSnip, strMsg, "About Me/ Help", JOptionPane.INFORMATION_MESSAGE);
        });

        btnOpenFile.addActionListener(e -> {
            String strText = txtMessage.getText();
            if (!strText.contains("File will be saved in ")) {
                try {
                    Desktop.getDesktop().open(new File(strText.split("File saved at ")[1]));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(frmJustSnip, "You are yet to generate a file", "Stop!", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        spinnerCount.addChangeListener(e -> {
            btnAutoSnip.setToolTipText("Sniping starts after " + spinnerCount.getValue() + " seconds");
            interval = Long.parseLong(spinnerCount.getValue().toString()) * 1000;
        });
    }

    private void setErrorMessagePopUp(Exception e1) {
        String strMsg = e1.toString().substring(0, 50) + "...\n\n " +
                "Please check below points:\n " +
                "1. File Format - Do not provide 'Special chars' in file name\n " +
                "2. Avoid giving the file name of length more that 50 chars (including space)";
        frmJustSnip.setVisible(true);
        JOptionPane.showMessageDialog(frmJustSnip, strMsg, "Something went wrong", JOptionPane.ERROR_MESSAGE);
    }

    private void setTargetFolderAndFilePath() {
        strTargetPath = txtTargetFolder.getText();
        strTargetFileName = txtFileName.getText();
    }

    private void saveFilePath() {
        prop.setProperty("targetPath", strTargetPath);
        prop.setProperty("targetFileName", strTargetFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            prop.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}