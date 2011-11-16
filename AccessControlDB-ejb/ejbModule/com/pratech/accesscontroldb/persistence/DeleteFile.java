package com.pratech.accesscontroldb.persistence;

import java.io.File;

import com.pratech.accesscontroldb.common.ACConfig;

public class DeleteFile {

	public void deleteFileExcel(String nameFile) {
		File file = new File("..//" + ACConfig.getValue("appConta")
				+ "//applications//AccessControlDB//AccessControlDB-war//"
				+ nameFile);
		if (file.exists()) {
			file.delete();
		}

	}

}
