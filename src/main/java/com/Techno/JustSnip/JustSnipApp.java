package com.Techno.JustSnip;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;

public class JustSnipApp {

    protected String strTargetPath = "";
    protected String strFileName;
    protected long interval = 2000;
    JustSnip objJustSnip;
    private JFrame frmJustSnip;
    private JTextField txtMessage;
    private JButton btnAutoSnip;
    private JButton btnRecord;
    private JTextField txtTargetFolder;
    private JTextField txtFileName;
    private JSpinner spinnerCount;

    /**
     * Create the application.
     */
    public JustSnipApp() {
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
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmJustSnip = new JFrame();
        frmJustSnip.setTitle("Just Snip (Author - Stalin Kar)");
        frmJustSnip.setBounds(100, 100, 467, 296);
        frmJustSnip.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmJustSnip.getContentPane().setLayout(null);

        JButton btnJustSnip = new JButton("Snip!");
        btnJustSnip.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnJustSnip.setBounds(10, 10, 90, 53);
        // btnJustSnip.setIcon(new ImageIcon(System.getProperty("user.dir") +
        // "\\src\\main\\resources\\snip.PNG"));
        frmJustSnip.getContentPane().add(btnJustSnip);
        strTargetPath = System.getProperty("user.home")+"\\Documents\\JustSnip\\";
        final String strDefaultMsg = "File will be saved in ";
        txtMessage = new JTextField(strDefaultMsg + strTargetPath + "{filename}");
        txtMessage.setBounds(10, 230, 433, 19);
        txtMessage.setEditable(false);
        frmJustSnip.getContentPane().add(txtMessage);
        txtMessage.setColumns(10);

        btnAutoSnip = new JButton("Auto Snip!");
        btnAutoSnip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnAutoSnip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (btnAutoSnip.getText().equals("Auto Snip!")) {
                    btnAutoSnip.setText("Stop!");
                    frmJustSnip.setExtendedState(JFrame.ICONIFIED);
                    try {
                        while (frmJustSnip.getExtendedState() == 1) {
                            Thread.sleep(interval);
                            JustSnip.strJustSnipPath = (strTargetPath.trim().isEmpty()) ? JustSnip.strJustSnipPath : strTargetPath + "-";
                            objJustSnip.SaveImgInWord(objJustSnip.TakeScreenShot());
                        }
                    } catch (AWTException | IOException | InvalidFormatException | InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                    btnAutoSnip.setText("Auto Snip!");
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
                    frmJustSnip.setExtendedState(JFrame.ICONIFIED);
                    try {
                        Thread.sleep(2000);
                        while (frmJustSnip.getExtendedState() == 1) {
                            Thread.sleep(42);
                            JustSnip.strJustSnipPath = (strTargetPath.trim().isEmpty()) ? JustSnip.strJustSnipPath : strTargetPath + "-";
                            objJustSnip.SaveImgInWord(objJustSnip.TakeScreenShot());
                        }
                    } catch (AWTException | IOException | InvalidFormatException | InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                    btnRecord.setText("Record");
                }
            }
        });
        btnRecord.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnRecord.setBounds(210, 10, 90, 53);
        frmJustSnip.getContentPane().add(btnRecord);

        JLabel lblTargetFolder = new JLabel("Target Folder:");
        lblTargetFolder.setBounds(10, 97, 180, 13);
        frmJustSnip.getContentPane().add(lblTargetFolder);

        JLabel lblTargetFileName = new JLabel("Screenshot File Name:");
        lblTargetFileName.setBounds(10, 160, 180, 13);
        frmJustSnip.getContentPane().add(lblTargetFileName);

        txtTargetFolder = new JTextField();
        txtTargetFolder.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                strTargetPath = txtTargetFolder.getText().trim();
                if (!strTargetPath.isEmpty()) {
                    strTargetPath = strTargetPath + "\\";
                    txtMessage.setText(strDefaultMsg + strTargetPath + "\\");
                } else {
                    strTargetPath = System.getProperty("user.home")+"\\Documents\\JustSnip\\";
                }
            }
        });
        txtTargetFolder.setToolTipText("Please enter the folder/path of the file need to be saved in");
        txtTargetFolder.setBounds(10, 121, 433, 29);
        frmJustSnip.getContentPane().add(txtTargetFolder);
        txtTargetFolder.setColumns(10);

        txtFileName = new JTextField();
        txtFileName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                strFileName = txtFileName.getText().trim();
                if (!strFileName.isEmpty()) {
                    txtMessage.setText(strDefaultMsg + strTargetPath + strFileName + "-{ddMMyyyy_hhmmss}.docx");
                }
            }
        });
        txtFileName.setToolTipText("Please enter the Filename need to save the Screenshots");
        txtFileName.setColumns(10);
        txtFileName.setBounds(10, 183, 433, 29);
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
        btnJustSnip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JustSnipApp.this.frmJustSnip.setVisible(false);
                    Thread.sleep(500);
                    String strFinalTargetPath = strTargetPath + txtFileName.getText().trim();
                    JustSnip.strJustSnipPath = (strTargetPath.trim().isEmpty()) ? JustSnip.strJustSnipPath : strFinalTargetPath + "-";
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
