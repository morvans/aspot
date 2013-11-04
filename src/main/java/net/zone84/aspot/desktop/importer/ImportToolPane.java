package net.zone84.aspot.desktop.importer;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

@SuppressWarnings("serial")
public class ImportToolPane extends JPanel implements ListDataListener {

	private JList list;

	private FileMoverModel model;

	public ImportToolPane(FileMoverModel model) {
		this.model = model;
		createPane();
	}

	private void createPane() {
		setLayout(new BorderLayout());

		list = new JList();
		list.setModel(model);
		list.setCellRenderer(new FileMoveListCellRenderer());

		model.addListDataListener(this);

		add(new JScrollPane(list));

	}

	public FileMoverModel getModel() {
		return model;
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		try {
			list.ensureIndexIsVisible(e.getIndex0());
		} catch (Exception ex) {

		}
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
	}
}
