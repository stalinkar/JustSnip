package com.JustSnip;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class JustSnipApp {

    private final String strPropertyPath = System.getProperty("user.home") + "\\Documents\\JustSnip\\Config\\";
    private final String strPropertyFile = "justsnip.config";
    protected String strTargetPath = System.getProperty("user.home") + "\\Documents\\JustSnip\\";
    protected String strTargetFileName = "ScreenShot";
    protected long interval = 2000;
    JustSnip objJustSnip;
    private JFrame frmJustSnip;
    private JTextField txtMessage;
    private JButton btnAutoSnip;
    private JButton btnRecord;
    private JTextField txtTargetFolder;
    private JTextField txtFileName;
    private JSpinner spinnerCount;
    private File file;
    private FileInputStream fis;
    private FileOutputStream fos;
    private Properties prop;
    private JButton btnHelp;

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
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JustSnipApp window = new JustSnipApp();
                    window.frmJustSnip.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        if (file.exists()) {
            try {
                fis = new FileInputStream(strPropertyPath + strPropertyFile);
                prop.load(fis);
                strTargetPath = prop.getProperty("TargetPath");
                strTargetFileName = prop.getProperty("TargetFile");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
                prop.setProperty("TargetPath", strTargetPath);
                prop.setProperty("TargetFile", strTargetFileName);
                fos = new FileOutputStream(file);
                prop.store(fos, "Target Path");
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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

        JButton btnJustSnip = new JButton("Snip!");
        btnJustSnip.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnJustSnip.setBounds(10, 10, 90, 53);
        // btnJustSnip.setIcon(new ImageIcon(System.getProperty("user.dir") +
        // "\\src\\main\\resources\\snip.PNG"));
        frmJustSnip.getContentPane().add(btnJustSnip);
        final String strDefaultMsg = "File will be saved in ";
        txtMessage = new JTextField(strDefaultMsg + strTargetPath + "\\" + strTargetFileName + "-{timestamp}.docx");
        txtMessage.setBounds(10, 240, 433, 19);
        txtMessage.setEditable(false);
        frmJustSnip.getContentPane().add(txtMessage);
        txtMessage.setColumns(10);

        btnAutoSnip = new JButton("Auto Snip!");
        btnAutoSnip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnAutoSnip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (btnAutoSnip.getText().equals("Auto Snip!")) {
                    btnAutoSnip.setText("Stop!");
                    btnJustSnip.setEnabled(false);
                    btnRecord.setEnabled(false);
                    frmJustSnip.setExtendedState(JFrame.ICONIFIED);
                    try {
                        JustSnip.file = null;
                        while (frmJustSnip.getExtendedState() == 1) {
                            Thread.sleep(interval);
                            JustSnip.strJustSnipPath = strTargetPath;
                            JustSnip.strFileName = strTargetFileName;
                            objJustSnip.SaveImgInWord(objJustSnip.TakeScreenShot());
                        }
                    } catch (AWTException | IOException | InvalidFormatException | InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                    txtMessage.setText("File saved at " + objJustSnip.getStrSavedFilePath());
                    btnAutoSnip.setText("Auto Snip!");
                    btnJustSnip.setEnabled(true);
                    btnRecord.setEnabled(true);
                }
            }
        });
        btnAutoSnip.setBounds(110, 10, 90, 53);
        frmJustSnip.getContentPane().add(btnAutoSnip);

        btnRecord = new JButton("Record");
        btnRecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (btnRecord.getText().equals("Record")) {
                    btnRecord.setText("Stop!");
                    btnJustSnip.setEnabled(false);
                    btnAutoSnip.setEnabled(false);
                    frmJustSnip.setExtendedState(JFrame.ICONIFIED);
                    try {
                        JustSnip.file = null;
                        Thread.sleep(2000);
                        while (frmJustSnip.getExtendedState() == 1) {
                            Thread.sleep(42);
                            JustSnip.strJustSnipPath = strTargetPath;
                            JustSnip.strFileName = strTargetFileName;
                            objJustSnip.SaveImgInWord(objJustSnip.TakeScreenShot());
                        }
                    } catch (AWTException | IOException | InvalidFormatException | InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                    btnRecord.setText("Record");
                    btnJustSnip.setEnabled(true);
                    btnAutoSnip.setEnabled(true);
                }
            }
        });
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
        spinnerCount.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                btnAutoSnip.setToolTipText("Sniping starts after " + spinnerCount.getValue() + " seconds");
                interval = Long.parseLong(spinnerCount.getValue().toString()) * 1000;
            }
        });
        spinnerCount.setModel(new SpinnerNumberModel(2, 1, 60, 1));
        spinnerCount.setBounds(310, 34, 35, 29);
        frmJustSnip.getContentPane().add(spinnerCount);

        JLabel lblNewLabel_1 = new JLabel("Second(s) 1-60");
        lblNewLabel_1.setBounds(348, 42, 95, 13);
        frmJustSnip.getContentPane().add(lblNewLabel_1);

        JButton btnSave = new JButton("Save File Path");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                strTargetPath = txtTargetFolder.getText();
                strTargetFileName = txtFileName.getText();
                //file.createNewFile();
                strTargetPath = strTargetPath.endsWith("\\") ? strTargetPath : strTargetPath + "\\";
                prop.setProperty("TargetPath", strTargetPath);
                prop.setProperty("TargetFile", strTargetFileName);
                try {
                    fos = new FileOutputStream(file);
                    prop.store(fos, "Target Path");
                    fos.close();
                    JustSnip.file = null;
                    File theDir = new File(strTargetPath);
                    if (!theDir.exists()) {
                        theDir.mkdirs();
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        btnSave.setBounds(20, 198, 133, 21);
        frmJustSnip.getContentPane().add(btnSave);

        btnHelp = new JButton("Help");
        btnHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String strMsg = "Author      : Stalin Kar\n" + "Reviewer : Abhinav Sinha\n" + "\n" + "This is a supperting tool for taking screenshot.";
                JOptionPane.showMessageDialog(btnSave, strMsg, "About Me/ Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnHelp.setBounds(310, 198, 133, 21);
        frmJustSnip.getContentPane().add(btnHelp);

        JButton btnOpenFile = new JButton("Open Target File");
        btnOpenFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String strText = txtMessage.getText();
                if (!strText.contains(strDefaultMsg)) {
                    try {
                        Desktop.getDesktop().open(new File(strText.split("File saved at ")[1]));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(btnOpenFile, "You are yet to genarete a file", "Stop!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnOpenFile.setBounds(163, 198, 137, 21);
        frmJustSnip.getContentPane().add(btnOpenFile);
        btnJustSnip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JustSnipApp.this.frmJustSnip.setVisible(false);
                    Thread.sleep(500);
                    JustSnip.strJustSnipPath = strTargetPath;
                    JustSnip.strFileName = strTargetFileName;
                    objJustSnip.SaveImgInWord(objJustSnip.TakeScreenShot());
                } catch (AWTException | IOException | InvalidFormatException | InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                txtMessage.setText("File saved at " + objJustSnip.getStrSavedFilePath());
                JustSnipApp.this.frmJustSnip.setVisible(true);
            }
        });

    }
}
