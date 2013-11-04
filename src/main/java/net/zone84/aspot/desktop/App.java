package net.zone84.aspot.desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.zone84.aspot.desktop.importer.FileMover;
import net.zone84.aspot.desktop.importer.FileMoverModel;
import net.zone84.aspot.desktop.importer.ImportToolPane;
import net.zone84.aspot.desktop.importer.Scanner;
import net.zone84.aspot.desktop.importer.ScannerEvent;
import net.zone84.aspot.desktop.importer.ScannerListener;

import com.drew.metadata.Directory;

/**
 * Hello world!
 * 
 */
public class App implements MouseListener, ScannerListener {

	private ImportToolPane importToolPane;

	private File destinationPath;

	private JButton startMovingBtn;

	private JFrame frame;

	// private List<FileMove> moves;

	private FileMoverModel model;

	public App() {
		model = new FileMoverModel();
		
		createWindow();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				
				
				JFileChooser destinationPathChooser = new JFileChooser();
				destinationPathChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				destinationPathChooser.setDialogTitle("Destination");
				
				int ret = destinationPathChooser.showOpenDialog(frame
						.getContentPane());
				
				switch (ret) {
				case JFileChooser.APPROVE_OPTION:
					destinationPath = destinationPathChooser.getSelectedFile();
					System.out.println("destination > " + destinationPath);
					break;
					default:
						System.exit(-1);
				}
				
				JFileChooser sourcePathChooser = new JFileChooser();
				sourcePathChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				sourcePathChooser.setDialogTitle("Source");

				ret = sourcePathChooser.showOpenDialog(frame
						.getContentPane());

				switch (ret) {
				case JFileChooser.APPROVE_OPTION:
					File source = sourcePathChooser.getSelectedFile();
					System.out.println("source > " + source);

					start(source);

				default:
				}

			}
		});

	}

	private void createWindow() {
		frame = new JFrame();
		importToolPane = new ImportToolPane(model);

		startMovingBtn = new JButton("Move files");
		startMovingBtn.setEnabled(false);
		startMovingBtn.addMouseListener(this);

		frame.getContentPane().add(importToolPane);
		frame.getContentPane().add(startMovingBtn, BorderLayout.SOUTH);

		frame.setSize(new Dimension(640, 480));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	private void start(File sourcePath) {

		long start = System.currentTimeMillis();

		Scanner scanner = new Scanner(destinationPath, model, sourcePath);
		scanner.addScannerListener(this);
		
		Thread scanThread = new Thread(scanner);
		scanThread.start();
		

		System.out.println("Took " + (System.currentTimeMillis() - start)
				+ "ms");
	}

	private void startMovingFiles() {

		new Thread(new FileMover(model)).start();

	}

	public static void debugTag(Directory directory, int tag) throws Exception {
		/*
		 * if (directory.containsTag(tag)) {
		 * System.out.println(directory.getTagName(tag) + "=>" +
		 * directory.getDate(tag)); }
		 */
	}

	public static void main(String[] args) {
		new App();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == startMovingBtn) {
			startMovingBtn.setEnabled(false);
			startMovingFiles();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void scanEnded(ScannerEvent event) {
		startMovingBtn.setEnabled(true);
	}
}
