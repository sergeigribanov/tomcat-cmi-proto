package com.cmi.filetransfering;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import javax.ws.rs.core.StreamingOutput;

public class IOFStreamer {
    public static StreamingOutput outputStream(final String hostFilePath) {
	StreamingOutput fileStream = new StreamingOutput() {
		@Override
		public void write(java.io.OutputStream output) throws IOException {
		    try {
			java.nio.file.Path path = Paths.get(hostFilePath);
			byte[] data = Files.readAllBytes(path);
			output.write(data);
			output.flush();
		    } catch (Exception e) {
			// TO DO // return null or throw execption
		    }
		}
	    };
	return fileStream;
    }
    public static void uploadFile(final String hostFilePath,
				  InputStream fileInputStream) {
	 try {
	     int read = 0;
	     byte[] bytes = new byte[1024];
	     
	     OutputStream out = new FileOutputStream(new File(hostFilePath));
	     while ((read = fileInputStream.read(bytes)) != -1) 
		 {
		     out.write(bytes, 0, read);
		 }
	     out.flush();
	     out.close();
	 } catch (IOException e) {
	     // TO DO : exception
	 }
    }
    
}
