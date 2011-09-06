package com.pratech.accesscontroldb.client;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Main entry point.
 * 
 * @author Pratech: Diego Piedrahita
 * @since 2011-07-01
 * @email dpiedrahita@pratechgroup.com
 * 
 */
public class AccessControlDB_war implements EntryPoint {

	private abstract static class PendingChange<T> {
		private final RecordsTable recordsTable;
		private final T value;
		private final Integer id;

		public PendingChange(RecordsTable recordsTable, T value, Integer id) {
			this.recordsTable = recordsTable;
			this.value = value;
			this.id = id;
		}

		/**
		 * Update the appropriate field in the {@link RecordsTable}.
		 * 
		 * @param recordsTable
		 *            the recordsTable to update
		 * @param value
		 *            the new value
		 */
		protected abstract void doCommit(RecordsTable recordsTable, T value,
				Integer id);
	}

	private static class ChangeData extends PendingChange<String> {

		public ChangeData(RecordsTable recordsTable, String value, Integer id) {
			super(recordsTable, value, id);
		}

		@Override
		protected void doCommit(RecordsTable recordsTable, String value,
				Integer id) {
			recordsTable.reg[id] = value;
		}
	}

	private abstract static class PendingChangeVari<T> {
		private final BlocksVarible blocksVarible;
		private final T value;

		public PendingChangeVari(BlocksVarible blocksVarible, T value) {
			this.blocksVarible = blocksVarible;
			this.value = value;
		}

		/**
		 * Commit the change to the contact.
		 */
		public void commit() {
			doCommit(blocksVarible, value);
		}

		/**
		 * Update the appropriate field in the {@link RecordsTable}.
		 * 
		 * @param recordsTable
		 *            the recordsTable to update
		 * @param value
		 *            the new value
		 */
		protected abstract void doCommit(BlocksVarible blocksVarible, T value);
	}

	private static class VariChange extends PendingChangeVari<String> {

		public VariChange(BlocksVarible blocksVarible, String value) {
			super(blocksVarible, value);
		}

		@Override
		protected void doCommit(BlocksVarible blocksVarible, String value) {
			blocksVarible.setVariable(value);
		}
	}

	private static class TypeChange extends PendingChangeVari<String> {

		public TypeChange(BlocksVarible blocksVarible, String value) {
			super(blocksVarible, value);
		}

		@Override
		protected void doCommit(BlocksVarible blocksVarible, String value) {
			blocksVarible.setTypeData(value);
		}
	}

	private static class AddresChange extends PendingChangeVari<String> {

		public AddresChange(BlocksVarible blocksVarible, String value) {
			super(blocksVarible, value);
		}

		@Override
		protected void doCommit(BlocksVarible blocksVarible, String value) {
			blocksVarible.setAddress(value);
		}
	}

	private static class ValueChange extends PendingChangeVari<String> {

		public ValueChange(BlocksVarible blocksVarible, String value) {
			super(blocksVarible, value);
		}

		@Override
		protected void doCommit(BlocksVarible blocksVarible, String value) {
			blocksVarible.setValue(value);
		}
	}

	private static class UseChange extends PendingChangeVari<Boolean> {

		public UseChange(BlocksVarible blocksVarible, Boolean value) {
			super(blocksVarible, value);
		}

		@Override
		protected void doCommit(BlocksVarible blocksVarible, Boolean value) {
			blocksVarible.setUse(value);
		}
	}

	private final List<RecordsTable> listRow = new ArrayList<RecordsTable>(0);
	private final List<BlocksVarible> listBlock = new ArrayList<BlocksVarible>(
			0);

	private TextArea textAreaSQL = new TextArea();
	private TextArea txtrBuffer = new TextArea();
	private TextArea textClob = new TextArea();
	private DialogBox dialogo = new DialogBox();
	private DialogBox modalClob = new DialogBox();
	// private FlexTable flexTable = new FlexTable();
	private ListBox lisScope = new ListBox();
	private ListBox lisInstance = new ListBox();
	private Button btnExecute = new Button("Execute");
	private Button btnChange = new Button("Change Instance");
	private Button btnClear = new Button("Clear");
	private ResponseDTO responseDTO = new ResponseDTO();
	private RequestDTO requestDTO = new RequestDTO();
	private DataConnection dataConnection = new DataConnection();
	private List<String> lisAm = new ArrayList<String>(0);
	private List<String> lisInst = new ArrayList<String>(0);
	private SqlEngineServiceAsync sqlEng = GWT.create(SqlEngineService.class);
	private String datCombo1;
	private HorizontalPanel horizontalPanel = new HorizontalPanel();
	private ListBox lisSource = new ListBox();
	private TextBox txtSourceNumber = new TextBox();
	private ListBox lisSolution = new ListBox();
	private TextBox txtApplicacion = new TextBox();
	private TextBox txtUser = new TextBox();
	private InlineLabel txtScope1 = new InlineLabel();
	private InlineLabel txtInstance1 = new InlineLabel();
	private InlineLabel txtUser1 = new InlineLabel();
	private InlineLabel txtTrow = new InlineLabel();
	private InlineLabel pages = new InlineLabel();
	private Image image = new Image("img/preloader.gif");
	private VerticalPanel verticalPanel_1 = new VerticalPanel();
	private TabPanel tabPanel = new TabPanel();

	private CellTable<RecordsTable> table = new CellTable<RecordsTable>();
	private CellTable<BlocksVarible> tableBlock = new CellTable<BlocksVarible>();

	private Button btnNext = new Button();
	private Button btnFirst = new Button();
	private Button btnPrevious = new Button();

	private PasswordTextBox txtPassword = new PasswordTextBox();
	private Boolean valid = false;

	private int intPage = 1;
	// private int intLastPage = 1;
	private int intRowsPerPage;
	// private int intTotalRows = 0;
	private boolean rowCountFlag = false;
	private String clobAnt;
	private int valTemp;
	private boolean isRowid;
	private boolean btnNextFlag;
	private boolean btnExcFlag;

	private RecordsTable reco;

	private List<PendingChange<?>> pendingChanges = new ArrayList<PendingChange<?>>(
			0);
	private List<PendingChangeVari<?>> pendingChangesVari = new ArrayList<PendingChangeVari<?>>(
			0);

	/**
	 * Creates a new instance of MainEntryPoint
	 */
	public AccessControlDB_war() {
	}

	public void onModuleLoad() {
		image.setVisible(false);
		validateIncome();
		// rootMain();
	}

	/**
	 * Send information to execute the SQL query
	 * 
	 */
	private void sendSql() {
		final String comanSql = textAreaSQL.getText().trim();

		int j = comanSql.toUpperCase().indexOf("ROWID");
		if (j < 0) {
			isRowid = false;
		} else {
			isRowid = true;
		}

		requestDTO.setStringSQL(comanSql);

		if (comanSql.length() == 0) {
			Window.alert("Ingrese Comandos");
			return;
		}
		requestDTO.setStringSQL(comanSql);

		requestDTO.setStart(((intPage - 1) * intRowsPerPage) + 1);
		requestDTO.setNumRow(intRowsPerPage);

		// assigns the total rows flag
		requestDTO.setRowCountFlag(rowCountFlag);

		image.setVisible(true);
		pendingChanges.clear();

		sqlEng.sendSql(requestDTO, listBlock, dataConnection,
				new AsyncCallback<ResponseDTO>() {

					public void onFailure(Throwable caught) {
						clearTable(table);
						image.setVisible(false);
					}

					public void onSuccess(ResponseDTO result) {
						responseDTO = result;
						txtrBuffer.setText(responseDTO.getSqlBuffer());

						if (txtrBuffer.getText().length() > 0) {
							tabPanel.selectTab(2);
						}

						// gets the total rows
						if (rowCountFlag
								&& responseDTO.getListData().size() > 0) {
							// unsets the row count flag
							rowCountFlag = false;
						}

						if (responseDTO.getListData().size() <= 0 && btnExcFlag) {
							btnExcFlag = false;
							clearTable(table);
							tabPanel.selectTab(2);
							if (txtrBuffer.getText().length() <= 0) {
								txtrBuffer
										.setText("La consulta no retorno datos");
							}
						}

						if (responseDTO.getListData().size() > 0) {
							fillTable();
							pages.setText("Page " + intPage + " ");
						}
						image.setVisible(false);
					}
				});
	}

	/**
	 * Contains instructions that the connection window paint or blog
	 * 
	 * @return
	 */
	private DialogBox inBitacora() {
		dialogo.setHTML("Conexi\u00F3n");

		VerticalPanel verticalPanel = new VerticalPanel();
		dialogo.setWidget(verticalPanel);

		Grid grid = new Grid(12, 2);
		verticalPanel.add(grid);

		Label lblScope = new Label("Ambiente *");
		grid.setWidget(0, 0, lblScope);

		fillLisAmbi();
		lisScope.setSelectedIndex(0);
		grid.setWidget(0, 1, lisScope);

		Label lblInstance = new Label("Instancia *");
		grid.setWidget(1, 0, lblInstance);

		lisInstance.setSelectedIndex(0);
		grid.setWidget(1, 1, lisInstance);

		Label lblSource = new Label("Fuente del Ingreso a producci\u00F3n. *");
		grid.setWidget(2, 0, lblSource);

		grid.setWidget(2, 1, lisSource);
		lisSource.clear();
		lisScope.setSelectedIndex(0);
		lisSource.addItem("");
		lisSource.addItem("Analista");
		lisSource.addItem("Incidente");
		lisSource.addItem("Inconsistencias");
		lisSource.addItem("Requerimiento St-Online");

		Label lblSourceNumber = new Label("Nro. Fuente Ingreso *");
		grid.setWidget(3, 0, lblSourceNumber);

		txtSourceNumber.setText("");
		grid.setWidget(3, 1, txtSourceNumber);

		Label lblSolution = new Label("Soluci\u00F3n *");
		grid.setWidget(4, 0, lblSolution);

		grid.setWidget(4, 1, lisSolution);
		lisSolution.clear();
		lisSolution.addItem("");
		lisSolution.addItem("Ajuste en la aplicaci\u00F3n");
		lisSolution.addItem("Barrido");
		lisSolution.addItem("Desarrollo de Funcionalidad");
		lisSolution
				.addItem("Entregar ejecuci\u00F3n del proceso a operaciones");
		lisSolution.addItem("M\u00F3dulo de Par\u00E1metros");
		lisSolution.addItem("Sin soluci\u00F3n raizal");

		Label lblApplication = new Label(
				"Aplicaci\u00F3n responsable de la soluci\u00F3n *");
		grid.setWidget(5, 0, lblApplication);

		txtApplicacion.setText("");
		grid.setWidget(5, 1, txtApplicacion);

		Label lblUser = new Label("Usuario *");
		grid.setWidget(7, 0, lblUser);

		txtUser.setText("");
		grid.setWidget(7, 1, txtUser);

		Label lblPassword = new Label("Contrase\u00F1a *");
		grid.setWidget(8, 0, lblPassword);

		txtPassword.setText("");
		grid.setWidget(8, 1, txtPassword);

		Button btnIn = new Button("Ingresar");
		grid.setWidget(9, 1, btnIn);

		btnIn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				dataConnection.setScope(lisScope.getValue(lisScope
						.getSelectedIndex()));
				if (lisInstance.getSelectedIndex() < 0) {
					dataConnection.setInstance("");
				} else {
					dataConnection.setInstance(lisInstance.getValue(lisInstance
							.getSelectedIndex()));
				}
				dataConnection.setSource(lisSource.getValue(lisSource
						.getSelectedIndex()));
				dataConnection.setSourceNumber(txtSourceNumber.getValue());
				dataConnection.setSoluction(lisSolution.getValue(lisSolution
						.getSelectedIndex()));
				dataConnection.setApplication(txtApplicacion.getValue());
				dataConnection.setUser(txtUser.getValue());
				dataConnection.setPassword(txtPassword.getValue());

				if (validateText()) {
					txtScope1.setText(dataConnection.getScope());
					txtInstance1.setText(dataConnection.getInstance());
					txtUser1.setText(dataConnection.getUser());

					sqlEng.connectionEntry(dataConnection,
							new AsyncCallback<String[]>() {

								public void onFailure(Throwable caught) {
									Window.alert("*Error de usuario o Contrase\u00F1a");
								}

								public void onSuccess(String[] data) {
									if (data[0].length() > 0) {
										dataConnection.setUrl(data[0]);
										intRowsPerPage = Integer
												.parseInt(data[1]);
										dialogo.hide();
									} else {
										Window.alert("Error de conexion");
									}
								}
							});
				}
			}
		});

		return dialogo;
	}

	/**
	 * Fill the combo environment
	 * 
	 */
	private void fillLisAmbi() {
		sqlEng.listAmbi(new AsyncCallback<List<String>>() {

			public void onFailure(Throwable caught) {
				lisScope.addItem("Sin registro");
			}

			public void onSuccess(List<String> result) {
				lisAm = result;
				lisScope.clear();
				lisScope.addItem("");
				for (int i = 0; i < lisAm.size(); i++) {
					lisScope.addItem(lisAm.get(i).toString());
				}
			}
		});
	}

	/**
	 * Fill the combo instance
	 * 
	 * @param data
	 */
	private void fillLisInst(String data) {
		sqlEng.listInst(data, new AsyncCallback<List<String>>() {

			public void onFailure(Throwable caught) {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			public void onSuccess(List<String> result) {
				lisInst = result;
				lisInstance.clear();
				lisInstance.addItem("");
				for (int i = 0; i < lisInst.size(); i++) {
					lisInstance.addItem(lisInst.get(i).toString());
				}
			}
		});
	}

	/**
	 * Clean the table information when entering new records
	 * 
	 * @param table
	 */
	private void clearTable(CellTable<RecordsTable> table) {
		while (table.getColumnCount() > 0) {
			table.removeColumn(table.getColumn(0));
		}
	}

	private void clearTableBlock(CellTable<BlocksVarible> tableBlock) {
		while (tableBlock.getColumnCount() > 0) {
			tableBlock.removeColumn(tableBlock.getColumn(0));
		}

		listBlock.clear();
		BlocksVarible block = new BlocksVarible(true, "", "", "");
		listBlock.add(block);
		fillTableBlocks();
		tableBlock.setRowData(listBlock);
	}

	/**
	 * Fill results table
	 * 
	 */
	private void fillTable() {
		if (responseDTO.getListData().isEmpty() && btnNextFlag) {
			Window.alert("Ultima pagina");
			btnNextFlag = false;
			intPage--;
		} else {
			while (table.getColumnCount() > 0) {
				table.removeColumn(table.getColumn(0));
			}
			listRow.clear();
			table.redraw();
			List<String[]> dataLi = new ArrayList<String[]>(0);
			dataLi = responseDTO.getListData();

			if (dataLi != null) {
				if (dataLi.size() > 0) {
					tabPanel.selectTab(0);

					for (int i = 0; i < dataLi.get(0).length; i++) {
						if (dataLi.get(0)[i].equals("CLOB")) {
							table.addColumn(addButton(i), dataLi.get(1)[i]);
						} else if (dataLi.get(0)[i].equals("-ID-")
								|| dataLi.get(0)[i].equals("ROWID")) {
							table.addColumn(addCell(i), dataLi.get(1)[i]);
						} else {
							if (isRowid) {
								table.addColumn(addCellEdit(i),
										dataLi.get(1)[i]);
							} else {
								table.addColumn(addCell(i), dataLi.get(1)[i]);
							}
						}
					}
					for (int i = 2; i < dataLi.size(); i++) {
						RecordsTable rec = new RecordsTable(dataLi.get(i));
						listRow.add(rec);
					}
					table.setRowData(listRow);
				}
			}
		}
	}

	/**
	 * Validates the login information section
	 */
	private void validateIncome() {
		// String token = Cookies.getCookie("eoSessionCookie");
		sqlEng.isUserAdmin(new AsyncCallback<String[]>() {

			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			public void onSuccess(String[] resul) {
				valid = (resul != null);

				if (valid) {
					dataConnection.setAnalyst(resul[1]);
					dataConnection.setUserName(resul[0]);
					rootMain();
				} else {
					Window.alert("No ha iniciado sesi\u00F3n en el sistema.\nSer\u00E1 direccionado a otro sitio.");
					Window.Location
							.replace("http://dll.suranet.com/login/login.aspx?id=VisorAplicaciones");
				}

			}
		});
	}

	/**
	 * It contains the instructions to paint the screen
	 */
	private void rootMain() {
		DialogBox bitacora = inBitacora();

		bitacora.setGlassEnabled(true);
		bitacora.setAnimationEnabled(true);
		bitacora.center();
		bitacora.show();

		horizontalPanel.add(verticalPanel_1);

		textAreaSQL.setText("Enter SQL code here.");
		DOM.setElementAttribute(textAreaSQL.getElement(), "id", "textareaQuery");

		InlineLabel txtUserCode = new InlineLabel();
		InlineLabel txtUserName = new InlineLabel();
		Button btnCommit = new Button("Commit");
		Button btnRedraw = new Button("Rollback");
		Button btnExport = new Button("Export");
		btnExecute.setTitle("F8 Execute");

		FlowPanel flowPri = new FlowPanel();
		FlowPanel flowVari = new FlowPanel();
		flowPri.setStyleName("containerResultMenu");

		txtUserCode.setText(dataConnection.getAnalyst());
		txtUserName.setText(dataConnection.getUserName());

		tabPanel.add(flowPri, "Results", false);
		tabPanel.add(flowVari, "Variable", false);

		FlowPanel panelBut = new FlowPanel();
		panelBut.setStyleName("resultMenu");
		flowPri.add(panelBut);

		btnCommit.setStyleName("btnCommitChanges");
		panelBut.add(btnCommit);
		InlineHTML liHtml1 = new InlineHTML();
		liHtml1.setStyleName("separator");
		panelBut.add(liHtml1);

		btnRedraw.setStyleName("btnRedrawTable");
		panelBut.add(btnRedraw);
		InlineHTML liHtml2 = new InlineHTML();
		liHtml2.setStyleName("separator");
		panelBut.add(liHtml2);

		btnExport.setStyleName("btnExport");
		InlineHTML liHtml3 = new InlineHTML();
		liHtml3.setStyleName("separator");
		panelBut.add(liHtml3);

		panelBut.add(txtTrow);

		FlowPanel flowPage = new FlowPanel();
		flowPage.setStyleName("footerTable");
		flowPri.add(flowPage);
		flowPage.add(pages);
		flowPage.add(btnFirst);
		flowPage.add(btnPrevious);
		flowPage.add(btnNext);

		InlineLabel nlnlblNewInlinelabel = new InlineLabel("");
		nlnlblNewInlinelabel.addStyleName("clear");
		FlowPanel flowTable = new FlowPanel();
		flowTable.setStyleName("tableTab");
		flowPri.add(flowTable);
		flowTable.add(table);

		textAreaSQL.setFocus(true);

		btnExecute.setStyleName("btnExecute");
		btnClear.setStyleName("btnClear");
		btnChange.setStyleName("btnSwitchInstance");
		btnCommit.setStyleName("btnCommitChanges");

		RootPanel.get("environment").add(txtScope1);
		RootPanel.get("instance").add(txtInstance1);
		RootPanel.get("user").add(txtUser1);
		RootPanel.get("execute").add(btnExecute);
		RootPanel.get("clear").add(btnClear);
		RootPanel.get("switchInstance").add(btnChange);
		RootPanel.get("preloader").add(image);

		RootPanel.get("queryField").add(textAreaSQL);
		RootPanel.get("tabs").add(tabPanel);

		RootPanel.get("userCode").add(txtUserCode);
		RootPanel.get("userName").add(txtUserName);

		textAreaSQL.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == 119) {
					execute();
				}
			}
		});

		tabPanel.add(txtrBuffer, "Messages", false);

		tabPanel.selectTab(0);

		btnFirst.setStyleName("btnFirst");
		btnPrevious.setStyleName("btnPrevious");
		btnNext.setStyleName("btnNext");

		flowVari.add(tableBlock);
		flowVari.setStyleName("tableTab");

		BlocksVarible block = new BlocksVarible(true, "", "", "");
		listBlock.add(block);
		fillTableBlocks();
		tableBlock.setRowData(listBlock);

		btnExecute.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// asks for the row count
				rowCountFlag = true;
				btnFirst.setEnabled(false);
				btnPrevious.setEnabled(false);
				btnNext.setFocus(true);
				requestDTO.setStart(0);
				intPage = 1;
				btnNextFlag = true;
				btnExcFlag = true;
				clearTable(table);
				// intLastPage = 1;

				if (!pendingChangesVari.isEmpty()) {
					commitVariable();
				}

				sendSql();
				// intTotalRows = 0;
			}
		});

		btnNext.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// controlButtons();
				btnFirst.setEnabled(true);
				btnPrevious.setEnabled(true);
				intPage++;
				btnNextFlag = true;
				btnExcFlag = false;
				sendSql();
			}
		});

		btnPrevious.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// goes to the last page
				intPage--;
				controlButtons();
				sendSql();
			}
		});

		btnFirst.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// goes to the last page
				intPage = 1;

				btnFirst.setEnabled(false);
				btnPrevious.setEnabled(false);

				btnNext.setEnabled(true);
				sendSql();
			}
		});

		btnChange.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				DialogBox bitacora = inBitacora();

				bitacora.setGlassEnabled(true);
				bitacora.setAnimationEnabled(true);
				bitacora.center();
				bitacora.show();
			}
		});

		btnCommit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				sendModifiedRecords();
			}
		});

		lisScope.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				datCombo1 = lisScope.getValue(lisScope.getSelectedIndex());
				fillLisInst(datCombo1);
			}
		});

		btnClear.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				textAreaSQL.setText("");
				clearTable(table);
				clearTableBlock(tableBlock);
				txtrBuffer.setText("");
				image.setVisible(false);
			}
		});

		btnRedraw.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				clearTable(table);
				fillTable();
				table.redraw();
			}
		});
	}

	/**
	 * State variables change after pressing run button
	 */
	protected void execute() {
		// asks for the row count
		rowCountFlag = true;
		btnFirst.setEnabled(true);
		btnPrevious.setEnabled(true);
		btnNext.setFocus(true);
		requestDTO.setStart(0);
		intPage = 1;
		sendSql();
	}

	/**
	 * Validates the contents of the connection window and highlights areas that
	 * are empty
	 * 
	 * @return
	 */
	private boolean validateText() {
		boolean valid = true;

		if (dataConnection.getApplication().length() < 1) {
			txtApplicacion.setStyleName("textFill");
			valid = false;
		} else {
			txtApplicacion.setStyleName("textOk");
		}

		if (dataConnection.getScope() == null) {
			lisScope.setStyleName("textFillLis");
			valid = false;
		} else {
			if (dataConnection.getScope().length() < 1) {
				lisScope.setStyleName("textFillLis");
				valid = false;
			} else {
				lisScope.setStyleName("textOkLis");
			}
		}

		if (dataConnection.getInstance() == null) {
			lisInstance.setStyleName("textFillLis");
			valid = false;
		} else {
			if (dataConnection.getInstance().length() < 1) {
				lisInstance.setStyleName("textFillLis");
				valid = false;
			} else {
				lisInstance.setStyleName("textOkLis");
			}
		}

		if (dataConnection.getPassword().length() < 1) {
			txtPassword.setStyleName("textFill");
			valid = false;
		} else {
			txtPassword.setStyleName("textOk");
		}

		if (dataConnection.getScope() == null) {
			txtSourceNumber.setStyleName("textFill");
			valid = false;
		} else {
			if (dataConnection.getScope().length() < 1) {
				txtSourceNumber.setStyleName("textFill");
				valid = false;
			} else {
				txtSourceNumber.setStyleName("textOk");
			}
		}

		if (dataConnection.getSolution() == null) {
			lisSolution.setStyleName("textFillLis");
			valid = false;
		} else {
			if (dataConnection.getSolution().length() < 1) {
				lisSolution.setStyleName("textFillLis");
				valid = false;
			} else {
				lisSolution.setStyleName("textOkLis");
			}
		}

		if (dataConnection.getSource() == null) {
			lisSource.setStyleName("textFillLis");
			valid = false;
		} else {
			if (dataConnection.getSource().length() < 1) {
				lisSource.setStyleName("textFillLis");
				valid = false;
			} else {
				lisSource.setStyleName("textOkLis");
			}
		}

		if (dataConnection.getSourceNumber().length() < 1) {
			txtSourceNumber.setStyleName("textFill");
			valid = false;
		} else {
			txtSourceNumber.setStyleName("textOk");
		}

		if (dataConnection.getUser().length() < 1) {
			txtUser.setStyleName("textFill");
			valid = false;
		} else {
			txtUser.setStyleName("textOk");
		}
		if (!valid) {
			Window.alert("Ingrese todos los datos del formulario");
		}
		return valid;
	}

	/**
	 * Manages the state of the navigation buttons of pagination
	 */
	private void controlButtons() {
		btnNext.setEnabled(true);

		// checks if this is the first page
		if (intPage <= 1) {
			intPage = 1;
			btnFirst.setEnabled(false);
			btnPrevious.setEnabled(false);
		} else {
			btnFirst.setEnabled(true);
			btnPrevious.setEnabled(true);
		}
	}

	/**
	 * Add editable table cells
	 * 
	 * @param valReg
	 *            - Value of content
	 * @return
	 */
	private Column<RecordsTable, String> addCellEdit(int valReg) {
		final int val = valReg;
		Column<RecordsTable, String> cellTableColumn = null;

		cellTableColumn = new Column<RecordsTable, String>(new EditTextCell()) {
			@Override
			public String getValue(RecordsTable object) {
				return object.reg[val];
			}
		};
		cellTableColumn
				.setFieldUpdater(new FieldUpdater<RecordsTable, String>() {

					public void update(int index, RecordsTable recordsTable,
							String value) {
						if (!recordsTable.reg[val].equals(value)) {
							pendingChanges.add(new ChangeData(recordsTable,
									value, val));
						}
					}
				});
		return cellTableColumn;
	}

	/**
	 * Add a cell to the table
	 * 
	 * @param valReg
	 *            - Value of content
	 * @return
	 */
	private Column<RecordsTable, String> addCell(int valReg) {
		final int val = valReg;
		Column<RecordsTable, String> cellTableColumn = null;

		cellTableColumn = new Column<RecordsTable, String>(new TextCell()) {
			@Override
			public String getValue(RecordsTable object) {
				return object.reg[val];
			}
		};
		return cellTableColumn;
	}

	/**
	 * Add a button cell to the table
	 * 
	 * @param valReg
	 *            - Value of content
	 * @return
	 */
	private Column<RecordsTable, String> addButton(int valReg) {
		final int val = valReg;
		Column<RecordsTable, String> cellTableColumn = null;

		cellTableColumn = new Column<RecordsTable, String>(new ButtonCell()) {
			@Override
			public String getValue(RecordsTable object) {
				return object.reg[val].substring(0, 4);
			}
		};
		cellTableColumn
				.setFieldUpdater(new FieldUpdater<RecordsTable, String>() {

					public void update(int index, RecordsTable recordsTable,
							String value) {
						// reco = recordsTable;
						valTemp = val;
						String colum = responseDTO.getListData().get(1)[val];
						String row = recordsTable.reg[0];
						String[] para = new String[] { colum, row,
								textAreaSQL.getText() };
						sqlEng.getCLOB(para, dataConnection,
								new AsyncCallback<String>() {

									public void onFailure(Throwable arg0) {

									}

									public void onSuccess(String value) {
										clobAnt = value;
										textClob.setText(value);
										DialogBox modalClob = showClob();
										modalClob.setGlassEnabled(true);
										modalClob.setAnimationEnabled(true);
										modalClob.center();
										modalClob.show();
									}
								});
					}
				});
		return cellTableColumn;
	}

	/**
	 * Adds the cells to add variables in anonymous block
	 * 
	 * @return
	 */
	private Column<BlocksVarible, String> addCellVari() {
		Column<BlocksVarible, String> cellTableColumn = null;

		cellTableColumn = new Column<BlocksVarible, String>(new EditTextCell()) {
			@Override
			public String getValue(BlocksVarible object) {
				return object.getVariable();
			}
		};
		cellTableColumn
				.setFieldUpdater(new FieldUpdater<BlocksVarible, String>() {

					public void update(int index, BlocksVarible blocksVarible,
							String value) {
						pendingChangesVari.add(new VariChange(blocksVarible,
								value));
					}
				});
		return cellTableColumn;
	}

	/**
	 * Adds the cells to add values in anonymous block
	 * 
	 * @return
	 */
	private Column<BlocksVarible, String> addCellValue() {
		Column<BlocksVarible, String> cellTableColumn = null;

		cellTableColumn = new Column<BlocksVarible, String>(new EditTextCell()) {
			@Override
			public String getValue(BlocksVarible object) {
				return object.getValue();
			}
		};
		cellTableColumn
				.setFieldUpdater(new FieldUpdater<BlocksVarible, String>() {

					public void update(int index, BlocksVarible blocksVarible,
							String value) {
						pendingChangesVari.add(new ValueChange(blocksVarible,
								value));
					}
				});
		return cellTableColumn;
	}

	/**
	 * Adds the cells combo for data types -anonymous block-
	 * 
	 * @return
	 */
	private Column<BlocksVarible, String> addListType() {
		List<String> lisType = new ArrayList<String>(0);
		lisType.add("");
		lisType.add("VARCHAR2");
		lisType.add("NUMBER");
		lisType.add("DATE");
		lisType.add("CHAR");
		lisType.add("LONG");
		lisType.add("BOOLEAN");
		lisType.add("BINARY_INTEGER");
		lisType.add("CLOB");
		Column<BlocksVarible, String> cellTableColumn = null;

		cellTableColumn = new Column<BlocksVarible, String>(new SelectionCell(
				lisType)) {
			@Override
			public String getValue(BlocksVarible object) {
				return object.getTypeData();
			}
		};
		cellTableColumn
				.setFieldUpdater(new FieldUpdater<BlocksVarible, String>() {

					public void update(int index, BlocksVarible blocksVarible,
							String value) {
						pendingChangesVari.add(new TypeChange(blocksVarible,
								value));

					}
				});
		return cellTableColumn;
	}

	/**
	 * Adds the cells will combo that defines the scope of variables - anonymous
	 * block -
	 * 
	 * @return
	 */
	private Column<BlocksVarible, String> addListAddress() {
		List<String> lisAdd = new ArrayList<String>(0);
		lisAdd.add("");
		lisAdd.add("IN");
		lisAdd.add("OUT");
		Column<BlocksVarible, String> cellTableColumn = null;

		cellTableColumn = new Column<BlocksVarible, String>(new SelectionCell(
				lisAdd)) {
			@Override
			public String getValue(BlocksVarible object) {
				return object.getTypeData();
			}
		};
		cellTableColumn
				.setFieldUpdater(new FieldUpdater<BlocksVarible, String>() {

					public void update(int index, BlocksVarible blocksVarible,
							String value) {
						pendingChangesVari.add(new AddresChange(blocksVarible,
								value));
					}
				});
		return cellTableColumn;
	}

	/**
	 * Add the check type cells - anonymous block -
	 * 
	 * @return
	 */
	private Column<BlocksVarible, Boolean> addCheck() {
		Column<BlocksVarible, Boolean> cellTableColumn = null;

		cellTableColumn = new Column<BlocksVarible, Boolean>(new CheckboxCell()) {
			@Override
			public Boolean getValue(BlocksVarible object) {
				return object.isUse();
			}
		};
		cellTableColumn
				.setFieldUpdater(new FieldUpdater<BlocksVarible, Boolean>() {

					public void update(int index, BlocksVarible blocksVarible,
							Boolean value) {
						pendingChangesVari.add(new UseChange(blocksVarible,
								value));
					}
				});
		return cellTableColumn;
	}

	/**
	 * Add the button type cells - anonymous block -
	 * 
	 * @return
	 */
	private Column<BlocksVarible, String> addButtonAdd() {
		Column<BlocksVarible, String> cellTableColumn = null;

		cellTableColumn = new Column<BlocksVarible, String>(new ButtonCell()) {
			@Override
			public String getValue(BlocksVarible object) {
				return "+";
			}
		};

		cellTableColumn
				.setFieldUpdater(new FieldUpdater<BlocksVarible, String>() {

					public void update(int arg0, BlocksVarible arg1, String arg2) {
						BlocksVarible block = new BlocksVarible(true, "", "",
								"");
						listBlock.add(block);
						tableBlock.redraw();
						tableBlock.setRowData(listBlock);
					}
				});

		return cellTableColumn;
	}

	/**
	 * Add the button type cells - anonymous block -
	 * 
	 * @return
	 */
	private Column<BlocksVarible, String> addButtonRem() {
		Column<BlocksVarible, String> cellTableColumn = null;

		cellTableColumn = new Column<BlocksVarible, String>(new ButtonCell()) {
			@Override
			public String getValue(BlocksVarible object) {
				return "-";
			}
		};

		cellTableColumn
				.setFieldUpdater(new FieldUpdater<BlocksVarible, String>() {

					public void update(int index, BlocksVarible arg1,
							String arg2) {
						listBlock.remove(index);
						tableBlock.redraw();
						tableBlock.setRowData(listBlock);

						if (index == 0) {
							BlocksVarible block = new BlocksVarible(true, "",
									"", "");
							listBlock.add(block);
							tableBlock.redraw();
							tableBlock.setRowData(listBlock);
						}
					}
				});

		return cellTableColumn;
	}

	/**
	 * Create pop-up window for the data type CLOB
	 * 
	 * @return
	 */
	private DialogBox showClob() {
		modalClob.setHTML("CLOB");

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSize("572px", "279px");
		modalClob.setWidget(verticalPanel);

		verticalPanel.add(textClob);
		textClob.setSize("573px", "219px");

		Button btnAccept = new Button("Aceptar");
		verticalPanel.add(btnAccept);

		btnAccept.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				if (!clobAnt.equals(textClob.getText())) {
					pendingChanges.add(new ChangeData(reco, textClob.getText(),
							valTemp));
				}
				modalClob.hide();
			}
		});

		return modalClob;

	}

	/**
	 * Handles changes to the records
	 */
	private void sendModifiedRecords() {
		List<String[]> modifiedRecords = new ArrayList<String[]>(0);
		if (!pendingChanges.isEmpty()) {
			for (int i = 0; i < pendingChanges.size(); i++) {
				RecordsTable reco = pendingChanges.get(i).recordsTable;
				String[] vec = responseDTO.getListData().get(0);

				// ROWID field position
				int position = 0;
				for (int j = 0; j < vec.length; j++) {
					if (vec[j].equals("ROWID")) {
						position = j;
						break;
					}
				}

				String val = (String) pendingChanges.get(i).value;
				String type = responseDTO.getListData().get(0)[pendingChanges
						.get(i).id];
				String column = responseDTO.getListData().get(1)[pendingChanges
						.get(i).id];
				modifiedRecords.add(new String[] { val, reco.reg[position],
						type, column, textAreaSQL.getText() });
			}
			sqlEng.updateRecords(modifiedRecords, dataConnection,
					requestDTO.getStringSQL(),
					new AsyncCallback<ResponseDTO>() {

						public void onSuccess(ResponseDTO responseDTO) {
							txtrBuffer.setText(responseDTO.getSqlBuffer());
							if (!txtrBuffer.getText().equals(
									"Succesfully modified records")) {
								clearTable(table);
								fillTable();
								table.redraw();
							}
							pendingChanges.clear();
							if (txtrBuffer.getText().length() > 0) {
								tabPanel.selectTab(2);
							}
						}

						public void onFailure(Throwable arg0) {
							Window.alert("Error al actualizar los registros");
							pendingChanges.clear();
							table.redraw();
						}
					});
		}
	}

	private void fillTableBlocks() {
		tableBlock.addColumn(addCheck(), "");
		tableBlock.addColumn(addCellVari(), "Variable");
		tableBlock.addColumn(addListType(), "Type");
		tableBlock.addColumn(addListAddress(), "In/Out");
		tableBlock.addColumn(addCellValue(), "Value");
		tableBlock.addColumn(addButtonAdd(), "Add");
		tableBlock.addColumn(addButtonRem(), "Rem");
	}

	/**
	 * Update the record to be modified in the results table
	 */
	private void commitVariable() {
		for (PendingChangeVari<?> pendingChangeVar : pendingChangesVari) {
			pendingChangeVar.commit();
		}
		pendingChangesVari.clear();
	}
}
