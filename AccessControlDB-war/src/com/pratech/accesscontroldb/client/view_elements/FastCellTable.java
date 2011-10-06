package com.pratech.accesscontroldb.client.view_elements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

public class FastCellTable<T> extends CellTable<T> {

	public FastCellTable() {
		super();
	}

	public FastCellTable(int pageSize, Resources resources) {
		super(pageSize, resources);
	}

	@Override
	protected void onBrowserEvent2(Event event) {
		// Get the event target.
		EventTarget eventTarget = event.getEventTarget();
		if (!Element.is(eventTarget)) {
			return;
		}

		// Ignore Mouseover events. Better performance in IE6 and FF3.
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEOVER:
		case Event.ONMOUSEOUT:
			return;	
		default:
			super.onBrowserEvent2(event);
		}

	}

	@Override
	protected void onBlur() {
		return;
	}

	@Override
	protected void onFocus() {
		return;
	}

	@Override
	public void addColumn(Column<T, ?> col, Header<?> header) {
		return;
	}
	
	@Override
	protected void setKeyboardSelected(int index, boolean selected,
		      boolean stealFocus) {
		return;
		
	}

//	private void setRowStyleName(TableRowElement tr, String rowStyle,
//			String cellStyle, boolean add) {
//		setStyleName(tr, rowStyle, add);
//		NodeList<TableCellElement> cells = tr.getCells();
//		for (int i = 0; i < cells.getLength(); i++) {
//			setStyleName(cells.getItem(i), cellStyle, add);
//		}
//	}

}
