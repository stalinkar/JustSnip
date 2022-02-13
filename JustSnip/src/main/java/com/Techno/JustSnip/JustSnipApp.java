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

public class JustSnipApp {

	private JFrame frmJustsnip;
	JustSnip objJustSnip;
	private JTextField txtMessage;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JustSnipApp window = new JustSnipApp();
					window.frmJustsnip.setVisible(true);
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
		frmJustsnip = new JFrame();
		frmJustsnip.setTitle("JustSnip");
		frmJustsnip.setBounds(100, 100, 466, 153);
		frmJustsnip.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton btnJustSnip = new JButton("");
		btnJustSnip.setIcon(new ImageIcon(System.getProperty("user.dir") + "\\src\\main\\resources\\snip.PNG"));
		frmJustsnip.getContentPane().add(btnJustSnip, BorderLayout.WEST);
		
		txtMessage = new JTextField("File will be saved in "+System.getProperty("user.home")+"\\Documents\\JustSnip\\"+" path");
		txtMessage.setEditable(false);
		frmJustsnip.getContentPane().add(txtMessage, BorderLayout.SOUTH);
		txtMessage.setColumns(10);
		btnJustSnip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JustSnipApp.this.frmJustsnip.setVisible(false);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					objJustSnip.SaveImgInWord(objJustSnip.TakeScreenShot());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				txtMessage.setText("File saved at "+objJustSnip.getStrSavedFilePath());
				JustSnipApp.this.frmJustsnip.setVisible(true);
			}
		});

	}
}
