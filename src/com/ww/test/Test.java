package com.ww.test;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) {

		System.out.println(new Date());

		System.out.println(System.currentTimeMillis());

		File f = new File("wwww");
		f.mkdirs();
		SimpleDateFormat sdf = new SimpleDateFormat("yMd-Hms");
		String formatDate = sdf.format(new Date());
		System.out.println(formatDate);

		String host = "127.0.0.1";

		int port = 8080;

		StringBuffer sbuf = new StringBuffer();
		sbuf.append(host);
		sbuf.append("=");
		sbuf.append(port);
		sbuf.append("_");
		sbuf.append("客户端1");
		sbuf.append("_");
		sbuf.append(formatDate);
		sbuf.append(".CVS");
		System.out.println(sbuf.toString());
		File file = new File(f, sbuf.toString());

		try {
			file.createNewFile();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bufw = new BufferedWriter(fw);
			bufw.write("时间,时间2,耗时\r\n");
			bufw.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {

		}

	}
}
