package com.Techno.JustSnip;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JustSnipApp {

	private JFrame frmJustSnip;
	JustSnip objJustSnip;
	private JTextField txtMessage;
	private JButton btnAutoSnip;
	private JButton btnRecord;
	private JTextField txtTargetFolder;
	private JTextField txtFileName;
	protected String strTargetPath="";
	protected String strFileName;

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
	 * Create the application.
	 */
	public JustSnipApp() {
		initialize();
		objJustSnip = new JustSnip();
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
		btnJustSnip.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		btnJustSnip.setBounds(59, 10, 90, 77);
		// btnJustSnip.setIcon(new ImageIcon(System.getProperty("user.dir") +
		// "\\src\\main\\resources\\snip.PNG"));
		frmJustSnip.getContentPane().add(btnJustSnip);
		final String strDefaultMsg = "File will be saved in ";
		txtMessage = new JTextField(strDefaultMsg + "C:\\Users\\Stalin\\Documents\\JustSnip\\{filename}");
		txtMessage.setBounds(10, 230, 433, 19);
		txtMessage.setEditable(false);
		frmJustSnip.getContentPane().add(txtMessage);
		txtMessage.setColumns(10);

		btnAutoSnip = new JButton("Auto Snip!");
		btnAutoSnip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnAutoSnip.getText().equals("Auto Snip!")) {
					btnAutoSnip.setText("Stop!");
					frmJustSnip.setExtendedState(JFrame.ICONIFIED);
					try {
						while(frmJustSnip.getExtendedState()==1) {
							Thread.sleep(2000);
							JustSnip.strJustSnipPath = (strTargetPath.trim().isEmpty())?JustSnip.strJustSnipPath : strTargetPath+"-";
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
		btnAutoSnip.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		btnAutoSnip.setBounds(159, 10, 90, 77);
		frmJustSnip.getContentPane().add(btnAutoSnip);
		
		btnRecord = new JButton("Record");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnAutoSnip.getText().equals("Record")) {
					btnAutoSnip.setText("Stop!");
					frmJustSnip.setExtendedState(JFrame.ICONIFIED);
					try {
						Thread.sleep(2000);
						while(frmJustSnip.getExtendedState()==1) {
							Thread.sleep(42);
							objJustSnip.SaveImgInWord(objJustSnip.TakeScreenShot());
						}
					} catch (AWTException | IOException | InvalidFormatException | InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					btnAutoSnip.setText("Record");
				}
			}
		});
		btnRecord.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		btnRecord.setBounds(259, 10, 90, 77);
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
				strTargetPath = txtTargetFolder.getText();
				if(!strTargetPath.equals("")) {
					strTargetPath = strTargetPath+"\\";
					txtMessage.setText(strDefaultMsg+strTargetPath+"\\");
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
				strTargetPath = txtTargetFolder.getText();
				if(!strTargetPath.isEmpty()) {
					strTargetPath = strTargetPath+"\\";
				}
				strFileName = txtFileName.getText();
				if(!strTargetPath.equals("")) {
					strTargetPath = strTargetPath + strFileName;
					txtMessage.setText(strTargetPath+"-{ddMMyyyy_hhmmss}.docx");
				}
			}
		});
		txtFileName.setToolTipText("Please enter the Filename need to save the Screenshots");
		txtFileName.setColumns(10);
		txtFileName.setBounds(10, 183, 433, 29);
		frmJustSnip.getContentPane().add(txtFileName);
		btnJustSnip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JustSnipApp.this.frmJustSnip.setVisible(false);
					Thread.sleep(500);
					JustSnip.strJustSnipPath = (strTargetPath.trim().isEmpty())?JustSnip.strJustSnipPath : strTargetPath+"-";
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
