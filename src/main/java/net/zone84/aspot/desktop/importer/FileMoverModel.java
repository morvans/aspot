package net.zone84.aspot.desktop.importer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.zone84.aspot.desktop.FileMove;

public class FileMoverModel implements ListModel {

	private List<FileMove> moves;

	private List<ListDataListener> listDataListeners;

	public FileMoverModel() {
		this(new ArrayList<FileMove>());
	}

	public FileMoverModel(List<FileMove> moves) {
		listDataListeners = new ArrayList<ListDataListener>();
		setMoves(moves);
	}

	public List<FileMove> getMoves() {
		return moves;
	}

	public void setMoves(List<FileMove> moves) {
		this.moves = moves;
		fireContentsChanged(new ListDataEvent(this,
				ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
	}

	public void addMove(FileMove move) {
		moves.add(move);
		fireIntervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,
				getSize() - 1, getSize() - 1));
	}

	@Override
	public int getSize() {
		return moves.size();
	}

	@Override
	public Object getElementAt(int index) {
		return moves.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listDataListeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listDataListeners.remove(l);
	}

	private void fireIntervalAdded(ListDataEvent e) {
		for (ListDataListener listener : listDataListeners) {
			listener.intervalAdded(e);
		}
	}

//	private void fireIntervalRemoved(ListDataEvent e) {
//		for (ListDataListener listener : listDataListeners) {
//			listener.intervalRemoved(e);
//		}
//	}

	private void fireContentsChanged(ListDataEvent e) {
		for (ListDataListener listener : listDataListeners) {
			listener.contentsChanged(e);
		}
	}

	public void refresh(FileMove move) {
		fireContentsChanged(new ListDataEvent(this,
				ListDataEvent.CONTENTS_CHANGED, moves.indexOf(move),
				moves.indexOf(move)));
	}
}
