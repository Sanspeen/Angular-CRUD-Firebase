package com.pratech.accesscontroldb.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pratech.accesscontroldb.common.ACConfig;

/**
 * Servlet que envía el archivo de Excel.
 * 
 * @since 2012-03-23
 * @author Juan Esteban Jaramillo
 * @email jjaramillo@pratech.com.co
 * @Company PRATECH S.A.S.
 * 
 */
public class ExcelSender extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * Constructor.
     */
    public ExcelSender() {
        super();
    }

	/** 
	 * Realiza el envio del archivo de Excel como un adjunto.
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = request.getParameter("filename");
		
		if (filename.endsWith(".xlsx"))
		{
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
		                     "attachment;filename=" + filename);
			
			String filePath = "..//"
				+ ACConfig.getValue("appConta")
				+ "//applications//AccessControlDB//AccessControlDB-war//"
				+ filename;
			
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
			
			byte[] data = new byte[2048];
			
			int numBytes = -1;
			while ((numBytes = bis.read(data)) != -1)
			{
				bos.write(data, 0, numBytes);
			}	
			bos.flush();
			bos.close();
			
			bis.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
