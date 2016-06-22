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

package org.occiware.tinom.extensions.utils;

import java.io.PrintStream;

import org.occiware.tinom.model.Publisher;

/**
 * @author Pierre-Yves Gibello - Linagora
 */
public class PrintStreamPublisher extends Publisher {

	PrintStream out;

	public PrintStreamPublisher(String name, PrintStream out) {
		super(name);
		this.out = out;
	}

	@Override
	public void run() {
		String inputNames[] = getInputNames();
		for(String inputName : inputNames) {
			try {
				doPublish(inputName, get(inputName));
			} catch(NoSuchFieldException e) {
				doPublish(inputName, e);
			}
		}
	}
	
	/**
	 * Retrieves this publisher's output stream.
	 * @return The output stream
	 */
	public PrintStream getOutputStream() {
		return out;
	}
	
	/**
	 * Publishes a channel's data.
	 * @param name The channel name
	 * @param value The value read from the channel
	 */
	public void doPublish(String name, String value) {
		this.out.println(name + "=" + value);
	}
	
	/**
	 * Publishes an exception.
	 * @param name The channel name
	 * @param e The exception to publish
	 */
	public void doPublish(String name, Exception e) {
		this.out.println("Error retrieving " + name + ": " + e);
	}
}
