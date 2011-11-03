package com.pratech.accesscontroldb.client.view_elements;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.view.client.CellPreviewEvent;

public class FastCellTable<T> extends CellTable<T> {

	private boolean cellIsEditing;
	private int keyboardSelectedColumn = 0;
	final Set<String> focusableTypes;
	private boolean isFocused = false;

	public FastCellTable() {
		super();
		sinkEvents(Event.ONMOUSEDOWN | Event.ONFOCUS | Event.ONCLICK);
		focusableTypes = new HashSet<String>();
		focusableTypes.add("select");
		focusableTypes.add("input");
		focusableTypes.add("textarea");
		focusableTypes.add("option");
		focusableTypes.add("button");
		focusableTypes.add("label");
	}

	public FastCellTable(int pageSize, Resources resources) {
		super(pageSize, resources);
		
		focusableTypes = new HashSet<String>();
		focusableTypes.add("select");
		focusableTypes.add("input");
		focusableTypes.add("textarea");
		focusableTypes.add("option");
		focusableTypes.add("button");
		focusableTypes.add("label");
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
		case Event.KEYEVENTS:
			System.out.println(event.getKeyCode());
		case Event.ONCLICK:
			onClick(event);
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
	
//	private void eventTab(Event event){
//		Element target = event.getEventTarget().cast();
//		target.getChildCount();
//	}

	/**
	 * Fire an event to the Cell within the specified {@link TableCellElement}.
	 */

	@SuppressWarnings("unchecked")
	protected <C> void onClick(Event event) {
		// Get the event target.
		EventTarget eventTarget = event.getEventTarget();
		if (!Element.is(eventTarget)) {
			return;
		}

		Element target = event.getEventTarget().cast();

		TableCellElement tableCell = findNearestParentCell(target);
		Element trElem = tableCell.getParentElement();
		if (trElem == null) {
			return;
		}			

		TableRowElement tr = TableRowElement.as(trElem);
		Element sectionElem = tr.getParentElement();
		if (sectionElem == null) {
			return;
		}
		int row = tr.getSectionRowIndex();
		int col = tableCell.getCellIndex();
		boolean isFocusable = isFocusable(target);
		isFocused = isFocused || isFocusable;
		keyboardSelectedColumn = col;

		// getPresenter().setKeyboardSelectedRow(row, !isFocusable, true);

		T value = getVisibleItem(row);
		Context context = new Context(row + getPageStart(), col,
				getValueKey(value));

		boolean isSelectionHandled = false;
		CellPreviewEvent<T> previewEvent = CellPreviewEvent.fire(this, event,
				this, context, value, cellIsEditing, isSelectionHandled);

		// Pass the event to the cell.
		if (!previewEvent.isCanceled()) {
			if (cellConsumesEventType(super.getColumn(col).getCell(),
					event.getType())) {

				Cell<C> cell = (Cell<C>) super.getColumn(col).getCell();
				C cellValue = (C) super.getColumn(col).getValue(value);
				Element parentElem = tableCell.getFirstChildElement();
				boolean cellWasEditing = cell.isEditing(context, parentElem,
						cellValue);

				super.getColumn(col).onBrowserEvent(context, parentElem, value,
						event);
				cellIsEditing = cell.isEditing(context, parentElem, cellValue);

				if (cellWasEditing && !cellIsEditing) {
					resetFocus(new Scheduler.ScheduledCommand() {
						public void execute() {
							setFocus(true);
						}
					});

				}
			}
		}

	}
	
	private TableCellElement findNearestParentCell(Element elem) {
		while ((elem != null) && (elem != getElement().cast())) {
			// TODO: We need is() implementations in all Element subclasses.
			// This would allow us to use TableCellElement.is() -- much cleaner.
			String tagName = elem.getTagName();
			if ("td".equalsIgnoreCase(tagName)
					|| "th".equalsIgnoreCase(tagName)) {
				return elem.cast();
			}
			elem = elem.getParentElement();
		}
		return null;
	}

	/**
	 * Reset focus on an element.
	 * 
	 * @param command
	 *            the command to execute when resetting focus
	 */
	public void resetFocus(ScheduledCommand command) {
		command.execute();
	}

	public boolean isFocusable(Element elem) {
		return focusableTypes.contains(elem.getTagName().toLowerCase())
				|| elem.getTabIndex() >= 0;
	}
}
