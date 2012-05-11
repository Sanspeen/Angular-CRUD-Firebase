package com.pratech.accesscontroldb.DTO;

/**
 * Contiene la informacion para que se utilizara en el LOG
 * 
 * @since 20110701
 * @author dpiedrahita
 * @email dpiedrahita@pratechgroup.com
 * @company PRATECH S.A.S.
 */
@SuppressWarnings("serial")
public class LogSql extends DTO {

	/** usuario */
	private String usuario;
	/** Código definido por la auditoría de la empresa */
	private String codigoAuditoria;
	/** Representa el identificador de la transacción */
	private Long transaccion;
	/**
	 * Representa el nombre del proceso de auditoría en marcha (de clase y / o
	 * procedimiento de almacenamiento) puede incluir el método y la línea de lo
	 * ocurrido.
	 */
	private String proceso;
	/** Representa el identificador de la transacción */
	private String descripcionAudit;
	/**
	 * Representa los campos adicionales de la definición del mensaje
	 *  
	 * 1.Url
	 * 2.Analista
	 * 3.Fuente de ingreso a produccion
	 * 4.Numero fuente ingreso
	 * 5.Solucion
	 * 6.Aplicacion responsable de la solucion.
	 * 
	 */
	private String[] camposTexto;

	private String cod;

	/**
	 * Mensaje tecnico
	 */
	private String technicalMsg;

	private Throwable throwable;

	public LogSql() {
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the codigoAuditoria
	 */
	public String getCodigoAuditoria() {
		return codigoAuditoria;
	}

	/**
	 * @param codigoAuditoria
	 *            the codigoAuditoria to set
	 */
	public void setCodigoAuditoria(String codigoAuditoria) {
		this.codigoAuditoria = codigoAuditoria;
	}

	/**
	 * @return the transaccion
	 */
	public Long getTransaccion() {
		return transaccion;
	}

	/**
	 * @param transaccion
	 *            the transaccion to set
	 */
	public void setTransaccion(Long transaccion) {
		this.transaccion = transaccion;
	}

	/**
	 * @param transaccion
	 *            the transaccion to set
	 */
	public void setTransaccion(String transaccion) {
		this.transaccion = Long.parseLong(transaccion);
	}

	/**
	 * @return the proceso
	 */
	public String getProceso() {
		return proceso;
	}

	/**
	 * @param proceso
	 *            the proceso to set
	 */
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	/**
	 * @return the descripcionAudit
	 */
	public String getDescripcionAudit() {
		return descripcionAudit;
	}

	/**
	 * @param descripcionAudit
	 *            the descripcionAudit to set
	 */
	public void setDescripcionAudit(String descripcionAudit) {
		this.descripcionAudit = descripcionAudit;
	}

	/**
	 * @return the camposTexto
	 */
	public String[] getCamposTexto() {
		return camposTexto;
	}

	/**
	 * @param camposTexto
	 *            the camposTexto to set
	 */
	public void setCamposTexto(String[] camposTexto) {
		this.camposTexto = camposTexto;
	}

	/**
	 * @return the cod
	 */
	public String getCod() {
		return cod;
	}

	/**
	 * @param cod
	 *            the cod to set
	 */
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	 * @return the technicalMsg
	 */
	public String getTechnicalMsg() {
		return technicalMsg;
	}

	/**
	 * @param technicalMsg
	 *            the technicalMsg to set
	 */
	public void setTechnicalMsg(String technicalMsg) {
		this.technicalMsg = technicalMsg;
	}

	/**
	 * @return the throwable
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * @param throwable
	 *            the throwable to set
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

}
