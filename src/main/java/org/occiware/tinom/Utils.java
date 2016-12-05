/**
 * Copyright 2016 Linagora, Université Grenoble-Alpes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.occiware.tinom;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Contains utilities functions.
 * @author Amadou Diarra - UGA
 */
public final class Utils {

	/**
	 * Private empty constructor.
	 */
	private Utils() {
		// nothing
	}


	/**
	 * Closes a stream quietly.
	 * @param in an input stream (can be null)
	 */
	public static void closeQuietly( InputStream in ) {
		if( in != null ) {
			try {
				in.close();
			} catch( IOException e ) {
				// nothing
			}
		}
	}


	/**
	 * Closes a stream quietly.
	 * @param out an output stream (can be null)
	 */
	public static void closeQuietly( OutputStream out ) {
		if( out != null ) {
			try {
				out.close();
			} catch( IOException e ) {
				// nothing
			}
		}
	}


	/**
	 * Closes a reader quietly.
	 * @param reader a reader (can be null)
	 */
	public static void closeQuietly( Reader reader ) {
		if( reader != null ) {
			try {
				reader.close();
			} catch( IOException e ) {
				// nothing
			}
		}
	}


	/**
	 * Closes a writer quietly.
	 * @param writer a writer (can be null)
	 */
	public static void closeQuietly( Writer writer ) {
		if( writer != null ) {
			try {
				writer.close();
			} catch( IOException e ) {
				// nothing
			}
		}
	}


	/**
	 * Copies the content from in into os.
	 * <p>
	 * Neither <i>in</i> nor <i>os</i> are closed by this method.<br>
	 * They must be explicitly closed after this method is called.
	 * </p>
	 * <p>
	 * Be careful, this method should be avoided when possible.
	 * It was responsible for memory leaks. See #489.
	 * </p>
	 *
	 * @param in an input stream (not null)
	 * @param os an output stream (not null)
	 * @throws IOException if an error occurred
	 */
	public static void copyStreamUnsafelyUseWithCaution( InputStream in, OutputStream os ) throws IOException {

		byte[] buf = new byte[ 1024 ];
		int len;
		while((len = in.read( buf )) > 0) {
			os.write( buf, 0, len );
		}
	}


	/**
	 * Copies the content from in into os.
	 * <p>
	 * This method closes the input stream.
	 * <i>os</i> does not need to be closed.
	 * </p>
	 *
	 * @param in an input stream (not null)
	 * @param os an output stream (not null)
	 * @throws IOException if an error occurred
	 */
	public static void copyStreamSafely( InputStream in, ByteArrayOutputStream os ) throws IOException {

		try {
			copyStreamUnsafelyUseWithCaution( in, os );

		} finally {
			in.close();
		}
	}


	/**
	 * Copies the content from in into outputFile.
	 * <p>
	 * <i>in</i> is not closed by this method.<br>
	 * It must be explicitly closed after this method is called.
	 * </p>
	 *
	 * @param in an input stream (not null)
	 * @param outputFile will be created if it does not exist
	 * @throws IOException if the file could not be created
	 */
	public static void copyStream( InputStream in, File outputFile ) throws IOException {
		OutputStream os = new FileOutputStream( outputFile );
		try {
			copyStreamUnsafelyUseWithCaution( in, os );
		} finally {
			os.close ();
		}
	}


	/**
	 * Copies the content from inputFile into outputFile.
	 *
	 * @param inputFile an input file (must be a file and exist)
	 * @param outputFile will be created if it does not exist
	 * @throws IOException if something went wrong
	 */
	public static void copyStream( File inputFile, File outputFile ) throws IOException {
		InputStream is = new FileInputStream( inputFile );
		try {
			copyStream( is, outputFile );
		} finally {
			is.close();
		}
	}


	/**
	 * Copies the content from inputFile into an output stream.
	 *
	 * @param inputFile an input file (must be a file and exist)
	 * @param os the output stream
	 * @throws IOException if something went wrong
	 */
	public static void copyStream( File inputFile, OutputStream os ) throws IOException {
		InputStream is = new FileInputStream( inputFile );
		try {
			copyStreamUnsafelyUseWithCaution( is, os );
		} finally {
			is.close();
		}
	}


	/**
	 * Reads properties from a file.
	 * @param file a properties file
	 * @return a {@link Properties} instance
	 * @throws IOException if reading failed
	 */
	public static Properties readPropertiesFile( File file ) throws IOException {

		Properties result = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream( file );
			result.load( in );

		} finally {
			closeQuietly( in );
		}

		return result;
	}

	/**
	 * Writes a string into a file.
	 *
	 * @param s the string to write
	 * @param outputFile the file to write into
	 * @throws IOException if something went wrong
	 */
	public static void writeStringInto( String s, File outputFile ) throws IOException {
		InputStream in = new ByteArrayInputStream( s.getBytes( "UTF-8" ));
		copyStream( in, outputFile );
	}


	/**
	 * Reads a text file content and returns it as a string.
	 * <p>
	 * The file is tried to be read with UTF-8 encoding.
	 * If it fails, the default system encoding is used.
	 * </p>
	 *
	 * @param file the file whose content must be loaded
	 * @return the file content
	 * @throws IOException if the file content could not be read
	 */
	public static String readFileContent( File file ) throws IOException {

		String result = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Utils.copyStream( file, os );
		result = os.toString( "UTF-8" );

		return result;
	}


	/**
	 * Reads a text file line by line and stocks the result in a list
	 * @param file the file whose content must be read
	 * @throws IOException
	 * */
	public static List<String> readFileByLine(File file) throws IOException {
		List<String> result = new ArrayList<String> ();
		BufferedReader brd = null;
		String line;

		try {
			brd = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while( (line=brd.readLine()) != null ) {
			result.add(line);
		}
		brd.close();
		return result;

	}
}