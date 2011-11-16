package com.pratech.accesscontroldb.client.view_elements;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.user.cellview.client.CellTable;
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
		EventTarget eventTarget = event.getEventTarget();
		if (!Element.is(eventTarget)) {
			return;
		}		

		// Ignore Mouseover events. Better performance in IE6 and FF3.
		switch (DOM.eventGetType(event)) {

		case Event.ONMOUSEOVER:
			return;
		case Event.ONMOUSEDOWN:
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
	protected void setKeyboardSelected(int index, boolean selected,
			boolean stealFocus) {
		return;

	}
}
