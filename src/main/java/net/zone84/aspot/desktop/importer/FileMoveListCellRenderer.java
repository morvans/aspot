package net.zone84.aspot.desktop.importer;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import net.zone84.aspot.desktop.FileMove;
import net.zone84.aspot.desktop.FileMove.FileMoveStatus;

@SuppressWarnings("serial")
public class FileMoveListCellRenderer extends JPanel implements
		ListCellRenderer {

	private JLabel filenameLabel;

	private JLabel sourcePathLabel;

	private JLabel destinationPathLabel;
	
	private JLabel pictureLabel;

	private JLabel existingPictureLabel;
	
	private JLabel statusLabel;
	
	private JButton removeWhenExistingBtn;
	
	public FileMoveListCellRenderer() {
		buildRenderer();
	}

	private void buildRenderer() {
		filenameLabel = new JLabel();
		pictureLabel = new JLabel();
		existingPictureLabel = new JLabel();
		destinationPathLabel = new JLabel();
		sourcePathLabel = new JLabel();
		statusLabel = new JLabel();
		
		removeWhenExistingBtn = new JButton("Supprimer");
		
		filenameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		sourcePathLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		destinationPathLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 4;
		constraints.gridwidth = 1;
		add(pictureLabel, constraints);

		constraints.weightx = 0;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 4;
		constraints.gridwidth = 1;
		add(existingPictureLabel, constraints);

		
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 100;
		constraints.anchor=GridBagConstraints.NORTHWEST;
		add(filenameLabel, constraints);
		
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(sourcePathLabel, constraints);

		constraints.gridy = 2;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(destinationPathLabel, constraints);

		constraints.gridy = 3;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		add(statusLabel, constraints);


		JPanel btnPanel = new JPanel();
		
		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridheight = 4;
		constraints.gridwidth = 1;
		constraints.weightx = 0;
		
		add(btnPanel, constraints);

		btnPanel.add(removeWhenExistingBtn);
		
		setBorder(new EmptyBorder(4, 4, 4, 4));
		
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		if (!(value instanceof FileMove)) {
			throw new IllegalStateException("value is not a FileMove");
		}

		if (!(list.getModel() instanceof FileMoverModel)) {
			throw new IllegalStateException("model is not a FileMoveModel");
		}
		
		FileMove move = (FileMove) value;

		filenameLabel.setText(move.getSource().getName());
		sourcePathLabel.setText(move.getSource().getParent());
		destinationPathLabel.setText(move.getDestinationPath().getAbsolutePath());
		
		statusLabel.setText(getMoveStatusLabel(move.getStatus()));
		
		if( isSelected ){
			setBackground(Color.LIGHT_GRAY);
		}else{
			setBackground(Color.WHITE);
		}
		
		if( move.getThumbnail() != null ){
			pictureLabel.setIcon(new ImageIcon(move.getThumbnail()));
		}else{
			pictureLabel.setIcon(null);
		}
		
		if( move.getExistingThumbnail() != null ){
			existingPictureLabel.setIcon(new ImageIcon(move.getExistingThumbnail()));
		}else{
			existingPictureLabel.setIcon(null);
		}
		
		removeWhenExistingBtn.setEnabled(move.getStatus() == FileMoveStatus.TARGET_EXISTS);
		
		return this;
		
		
	}
	
	public static String getMoveStatusLabel(FileMoveStatus status){
		switch (status) {
		case MOVED:
			return "Déplacé";

		case PENDING:
			return "En attente";

		case TARGET_EXISTS:
			return "Existe déjà";

		default:
			return "Inconnu";
		}
	}

}
