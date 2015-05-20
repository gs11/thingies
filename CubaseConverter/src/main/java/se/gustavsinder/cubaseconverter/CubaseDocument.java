package se.gustavsinder.cubaseconverter;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CubaseDocument {

	private File fileName;
	private byte[] fileBytes;
	private int identifierPosition;
	private String fileType;
	private String fileVersion;

	public static final String SX_IDENTIFIER = "Cubase SX";
	public static final String AI_IDENTIFIER = "Cubase AI";
	
	private static final byte[] NUL_CHARS = {0x00, 0x00, 0x00, 0x00, 0x0E};
	private static final String VERSION_TEXT = "Version";
	private static final int VERSION_LENGTH = 5; // Format X.Y.Z

	public File getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public String getFileVersion() {
		return fileVersion;
	}
	
	public CubaseDocument(String filename) {
		this.fileName = new File(filename);
		this.readFile();
		this.identify();
		if(this.fileType == null) {
			//TODO file not identified. Can the object be nullified?
		}
	}

	private void readFile() {
		fileBytes = new byte[(int) fileName.length()];

		//TODO handle too big files?

		try {
			FileInputStream fis = new FileInputStream(fileName);
			DataInputStream dataInputStream = new DataInputStream(fis);
			dataInputStream.readFully(fileBytes);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void identify() {
		int identiferPos;
		StringBuilder sb = new StringBuilder();

		identiferPos = KMPMatch.indexOf(fileBytes, getBytesForIdentifier(SX_IDENTIFIER));
		if (identiferPos != -1) {
			this.identifierPosition = identiferPos;
			this.fileType = SX_IDENTIFIER;
			
			int versionStartPos = identiferPos + getBytesForIdentifier(SX_IDENTIFIER).length + 1;			
			for (int i = versionStartPos; i < versionStartPos + VERSION_LENGTH; i++) {
				
				sb.append((char) fileBytes[i]);
			}
			this.fileVersion = sb.toString();
			
			return;
		}
		// TODO: Rewrite and optimize code
		identiferPos = KMPMatch.indexOf(fileBytes, getBytesForIdentifier(AI_IDENTIFIER));
		if (identiferPos != -1) {
			this.identifierPosition = identiferPos;
			this.fileType = AI_IDENTIFIER;
			
			int versionStartPos = identiferPos + getBytesForIdentifier(AI_IDENTIFIER).length + 1;			
			for (int i = versionStartPos; i < versionStartPos + VERSION_LENGTH; i++) {
				
				sb.append((char) fileBytes[i]);
			}
			this.fileVersion = sb.toString();
			
			return;
		}
	}

	private byte[] getBytesForIdentifier (String identifier) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(identifier.getBytes());
			baos.write(NUL_CHARS);
			baos.write(VERSION_TEXT.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public void saveAsNewFile (String newFileType) {
		
		int fileLastDotPosition = fileName.getName().lastIndexOf(".");
		String fileStart = fileName.getName().substring(0, fileLastDotPosition);
		String fileEnding = fileName.getName().substring(fileLastDotPosition, fileName.getName().length());
		
		File outputFile = new File(fileName.getParent() + "/" + fileStart + " (" + newFileType + " Converted)" + fileEnding);
		
		//TODO: check if file exists
		
		try {	
			FileOutputStream fos = new FileOutputStream(outputFile);
			DataOutputStream dos = new DataOutputStream(fos);
			
			dos.write(fileBytes, 0, identifierPosition); // Write bytes up until the identifier
			dos.write(newFileType.getBytes(), 0, newFileType.length()); // Writes the new identifier
			dos.write(fileBytes, identifierPosition + newFileType.length(), 
					fileBytes.length - identifierPosition - newFileType.length()); // Writes the remaining bytes after the identifier			
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();	
		} 
	}
}
