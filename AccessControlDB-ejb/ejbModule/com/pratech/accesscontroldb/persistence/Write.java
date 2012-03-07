package com.pratech.accesscontroldb.persistence;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.pratech.accesscontroldb.common.ACConfig;

public class Write {
	@SuppressWarnings("finally")
	public String writeExcel(List<String[]> lisDat) {
		String nameFile = "";

		// Se crea el libro
		HSSFWorkbook libro = new HSSFWorkbook();

		// Se crea una hoja dentro del libro
		HSSFSheet hoja = libro.createSheet();
		HSSFRow row;
		HSSFCell cell;
		HSSFRichTextString texto;
		boolean ready=false;

		for (int i = 1; i < lisDat.size(); i++) {
			String[] fil = lisDat.get(i);
			row = hoja.createRow(i - 1);
			for (int j = 0; j < fil.length; j++) {
				cell = row.createCell((short) j);
				texto = new HSSFRichTextString(fil[j]);
				cell.setCellValue(texto);
			}
		}		

		
		// Se salva el libro.
		try {
			Date time = new Date();
			nameFile = "ACDB"  + time.getTime() + ".xls";
			String url = "..//"
				+ ACConfig.getValue("appConta")
				+ "//applications//AccessControlDB//AccessControlDB-war//"
				+ nameFile.trim();
			FileOutputStream theFile = new FileOutputStream(url);   			
			libro.write(theFile);
//			FilePermission objFP1 =new FilePermission(url, "read,write,delete");
//			System.out.println("Se creo xls - actio - " + objFP1.getActions());			
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		//TO-DO 
		finally{
		return nameFile;
		}
		

	}
}
