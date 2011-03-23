package com.zjuh.ally.downloads;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class InputUI  {
	
	private Frame frame = new Frame();
	
	private int width = 400;
	
	private int height = 200;
	
	private TextField urlTextField;
	
	private TextField pathTextField;
	
	private Button downloadButton;
	
	public InputUI() {
		init();
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	private void init() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int x = (d.width - width) / 2;
		int y = (d.height - height) / 2;
		frame.setBounds(x, y, width, height);
		frame.setTitle("ally batch download images");
		frame.addWindowListener(new ExitWindowListener());
		frame.setLayout(new GridLayout(6, 4));
		
		urlTextField = new TextField();
		pathTextField = new TextField("d:\\downloads\\images");
		
		frame.add(new Label("url"));
		frame.add(urlTextField);
		
		frame.add(new Label("保存到"));
		frame.add(pathTextField);
		
		downloadButton = new Button("下载");
		
		downloadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DownloadThread downloadThread = new DownloadThread();
				downloadThread.start();
			}
			
		});
		
		
		frame.add(downloadButton);
	}
	
	class DownloadThread extends Thread {

		@Override
		public void run() {
			try {
				downloadButton.setLabel("下载中...");
				downloadButton.setEnabled(false);
				String url = urlTextField.getText();
				if (url == null || url.trim().length() == 0) {
					return;
				}
				url = url.trim();
				String path = pathTextField.getText();
				if (path == null || path.trim().length() == 0) {
					return;
				}
				path = path.trim();
				Downloader.downloadImages(url, path);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				downloadButton.setLabel("下载");
				downloadButton.setEnabled(true);
			}
		}
		
	}
	
	private class ExitWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}

		@Override
		public void windowClosed(WindowEvent e) {
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public static void main(String[] args) {
		InputUI a = new InputUI();
		a.show();
	}

}
