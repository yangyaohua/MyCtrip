package com.example.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class FileHelper {
	public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myCtrip/";

	public static void downloadData(String fileName, String data) {
		File file = new File(DOWNLOAD_PATH + fileName);
		File dir = new File(DOWNLOAD_PATH);
		FileOutputStream fos = null;
		try {
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			byte[] bytes = data.getBytes();
			fos.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
