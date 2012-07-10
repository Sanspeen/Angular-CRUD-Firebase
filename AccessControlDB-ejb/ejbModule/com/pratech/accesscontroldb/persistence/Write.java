package com.pratech.accesscontroldb.persistence;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.pratech.accesscontroldb.DTO.RequestDTO;
import com.pratech.accesscontroldb.common.ACConfig;
import com.pratech.accesscontroldb.core.sqlresponse.SqlResultSet;

public class Write {

	/**
	 * Exporta los datos como un archivo de Excel.
	 * 
	 * @param rs
	 * @param requestDTO
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public String writeExcel(ResultSet rs, RequestDTO requestDTO)
			throws SQLException, Exception {

		//Modificado el 2012-05-24 por Juan
		int rowMax=Integer.parseInt(ACConfig.getValue("maxRowsExport"));
		
		//Nombre archivo Excel
		String fileName = "";
		//Se crea el libro
		Workbook libro = new SXSSFWorkbook(1000);
		// Se crea una hoja dentro del libro
		Sheet hoja = libro.createSheet();
		Row row;
		Cell cell;
		RichTextString texto;
		int rowIndex = 1;
		
			boolean more;
			String[] rowTitles;
			String[] rowData;
			
			if (requestDTO.getExportData() == 2) {
				more = rs.absolute(1);
			} else {
				more = rs.absolute(requestDTO.getStart());
			}
			if (more) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();

				rowTitles = SqlResultSet.readTitles(rsmd, colCount);
				
				//Escribir en Excel: títulos
				row = hoja.createRow(0);
				for (int i = 0; i < colCount + 1; i++) {
					cell = row.createCell((short) i);
					texto = new HSSFRichTextString(rowTitles[i]);
				cell.setCellValue(texto);
			}

				int countAdds = 0;

				while (more) {
					rowData = SqlResultSet.readRow(rs, requestDTO, colCount);
					
					//Escribir en Excel: fila de datos
					row = hoja.createRow(rowIndex);
					for (int i = 0; i < colCount + 1; i++) {
						cell = row.createCell((short) i);
						texto = new HSSFRichTextString(rowData[i]);
						cell.setCellValue(texto);
		}		

					if (requestDTO.getExportData() != 2) {
						if (++countAdds == requestDTO.getNumRow()) {
							break;
						}
					}
					more = rs.next();
					
				//Modificado el 2012-05-24 por Juan
				if (rowIndex == rowMax) {
						break;
					}
					rowIndex++;
				}
			}

		// Se salva el libro.
			Date time = new Date();
		long millis = time.getTime();

		fileName = "ACDB" + millis + ".xlsx";

		String filePath = "..//" + ACConfig.getValue("appConta")
				+ "//applications//AccessControlDB//AccessControlDB-war//"
				+ fileName.trim();
			FileOutputStream theFile = new FileOutputStream(filePath);   			
			libro.write(theFile);

		return fileName;
	}
	
}
