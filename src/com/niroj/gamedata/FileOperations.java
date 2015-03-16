package com.niroj.gamedata;

import com.niroj.marriagepointcollector.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.os.Environment;

public class FileOperations {
	
	private static FileOperations mOperations = null;
	private String mPath;
	private ObjectInput mObjInput;
	private ObjectOutputStream mObjOutputData;

	//Protocol to write in the files after game completion
	//--- Date ---- Name of each Players -----  
	private FileOperations() {
		
		 String state = Environment.getExternalStorageState();
	        if (Environment.MEDIA_MOUNTED.equals(state) ) {
	            ZSystem.LogD("It's available");
	        } else
	        	ZSystem.LogD("It's not available");
		mPath = new String();
		mPath = "";
		mPath += Environment.getExternalStorageDirectory();
		mPath += FINAL_STRING_FILE_LOCATION;
		ZSystem.LogD(" File Path = "+ mPath);
		File file = new File(mPath);
		
		if(!file.exists()) {
			if( file.mkdirs() == true) 
				ZSystem.LogD("Successfully created directories");
			else
				ZSystem.LogD("FailedToCreateDirectories");
		}
		else
			ZSystem.LogD("File exists");
	}
	
	public static FileOperations getInstance() {
		if( mOperations == null ) {
			synchronized(FileOperations.class) {
				if( mOperations == null ) {
					ZSystem.LogD("File created");
					mOperations = new FileOperations();
				}
			}
		}
		return mOperations;
	}
	
	public ArrayList<DataType> readDataFromFile() {
		ArrayList<DataType> listGames;

		String path = mPath +  FINAL_STRING_FILE_NAME;
		File file = new File(path);
		
		if(!file.exists()) {
			ZSystem.LogD("File has not been created yet!!!");
			
			return null;
		}
		try {
			InputStream fis = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(fis);
			mObjInput = new ObjectInputStream(buffer);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ZSystem.LogD("FileNotFoundException");
			
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ZSystem.LogD("StreamCorruptedException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ZSystem.LogD("IOException");
		}
		if( mObjInput == null ) {
			ZSystem.LogE("Obj Input Stream is null");
			
			return null;
		}
		
		Object obj = null;
		listGames = new ArrayList<DataType>();
		try {
			obj = mObjInput.readObject();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ZSystem.LogD("ClassNotFoundException");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ZSystem.LogD("IOException");
		}
		while(true) {
			DataType data = (DataType) obj;
			ZSystem.LogD(" Name = " +data.mGameName );
			if( listGames.size() == 0)
				listGames.add(data);
			else if( !listGames.get(listGames.size()-1).mGameName.equals( data.mGameName ) )
				listGames.add(data);
			try {
				obj = mObjInput.readObject();
			} catch (EOFException e) {
	            // If there are no more objects to read, return what we have.
				ZSystem.LogD("EoF exception");
	            break;
	        }catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				ZSystem.LogD("ClassNotFoundException");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ZSystem.LogD("IOException");
				if( obj == null ) {
					ZSystem.LogD("obj is null after exception");
				}
				e.printStackTrace();
			}
			
		}
		try {
			mObjInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listGames;
	}
	
	public void writeDataInFile( DataType data) {
		String path = mPath +  FINAL_STRING_FILE_NAME;
		File file = new File(path);
		boolean bFirstTime = true;

		OutputStream fis = null;
		OutputStream buffer = null;
		if( file.exists() )
			bFirstTime = false;
			
		try {
			fis = new FileOutputStream(path, true);
			buffer = new BufferedOutputStream(fis);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			ZSystem.LogD("File Not Found: FileNotFoundException");
		}
		
		if(bFirstTime) {
			ZSystem.LogD("File has not been created yet!!!");
			try {
				mObjOutputData = new ObjectOutputStream(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ZSystem.LogD("IO Exception: Writing First time");
				e.printStackTrace();
			}
		} else {
			try {
				mObjOutputData = new AppendingObjectOutputStream(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ZSystem.LogD("IO Exception: Appending File");
				e.printStackTrace();
			}
		}
		writeToFile( mObjOutputData, data );
		file = new File(path);

		if( !file.exists() ) {
			ZSystem.LogD("File Not Created yet");
		}
		
	}
	
	public void writeToFile(ObjectOutputStream outStream, DataType data ) {
		
		try {
			outStream.writeObject(data);
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ZSystem.LogE("Error writing to file: IO Exception");
			e.printStackTrace();
		}
		ZSystem.LogD("Written to file succesfully");
	}
	
	public boolean deleteHistory() {
		String path = mPath + FINAL_STRING_FILE_NAME;
		File file = new File(path);
		
		if( file.exists()){
			return file.delete();
		}
		
		return false;
	}
	
	private class AppendingObjectOutputStream extends ObjectOutputStream {

		protected AppendingObjectOutputStream(OutputStream out)
				throws IOException {
			super(out);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void writeStreamHeader() throws IOException {
			// do not write a header, but reset:
			// this line added after another question
			// showed a problem with the original
			reset();
		}

	}
	
	private static final String FINAL_STRING_FILE_LOCATION = "/MPoint";
	private static final String FINAL_STRING_FILE_NAME="/Game.txt";
}
