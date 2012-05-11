package com.pratech.accesscontroldb.client.view_elements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.pratech.accesscontroldb.client.AccessControlDB_war;
import com.pratech.accesscontroldb.client.AdminService;
import com.pratech.accesscontroldb.client.AdminServiceAsync;

public class PopupMenu implements ContextMenuHandler {

	private PopupPanel popupPanel;
	private AdminServiceAsync adminService = GWT.create(AdminService.class);

	public PopupMenu(final AccessControlDB_war accessControl) {

		Command updateInstancesCommand = new Command() {
			public void execute() {
				adminService.updateInstancesXML(new AsyncCallback<String>() {
					public void onFailure(Throwable arg0) {
						if (arg0 instanceof StatusCodeException) {
							Window.alert("No est\u00E1 autorizado para ejecutar la funcionalidad.");
						} else {
							Window.alert("Ocurri\u00F3 una excepci\u00F3n de tipo: " + arg0.getClass().getName() + ". Mensaje: " + arg0.getMessage());
						}
						popupPanel.hide();
					}

					public void onSuccess(String result) {
						if (result.toUpperCase().contains("XITO")) {
							//Buscar por la palabra éxito en la respuesta.
							//En caso de éxito se carga nuevamente la lista de
							//ambientes y se deja en blanco la lista de instancias.
							accessControl.fillLisAmbi();
							accessControl.fillLisInst("");
						}
						Window.alert(result);
						popupPanel.hide();
					}
				});

			}
		};

		// initialize widget...

		this.popupPanel = new PopupPanel(true);

		MenuBar popupMenuBar = new MenuBar(true);
		MenuBar popupMenuBarAdmin = new MenuBar(true);
		popupMenuBar.addItem("Administraci&oacute;n", true, popupMenuBarAdmin);
		MenuItem updateInstancesItem = new MenuItem("Actualizar instancias en l&iacute;nea", true, updateInstancesCommand);
		
		popupMenuBarAdmin.setAutoOpen(true);
		popupMenuBar.setAutoOpen(true);

		popupMenuBarAdmin.addItem(updateInstancesItem);

		popupMenuBar.setVisible(true);
		popupMenuBarAdmin.setVisible(true);
		popupPanel.add(popupMenuBar);
	}

	public void onContextMenu(ContextMenuEvent event) {
		// stop the browser from opening the context menu
		event.preventDefault();
		event.stopPropagation();

		this.popupPanel.setPopupPosition(event.getNativeEvent().getClientX(),
				event.getNativeEvent().getClientY());
		this.popupPanel.show();
	}

}
