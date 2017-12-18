package com.starschina.sdk.demo.common;

import android.app.Application;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;

public class SdkDemoApp extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		//LeakCanary.install(this);

		Printer filePrinter = new FilePrinter                      // Printer that print the log to the file system
				.Builder("/sdcard/xlog/")                              // Specify the path to save log file
				.fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
				.backupStrategy(new NeverBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
				//.logFlattener(new MyFlattener())                       // Default: DefaultFlattener
				.build();

		XLog.init(new LogConfiguration.Builder()
				.logLevel(LogLevel.ALL)
				.t()
				.st(1)
				.b().build(),
				filePrinter);


	}
}
