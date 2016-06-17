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

package org.occiware.tinom.model;

/**
 * @author Pierre-Yves Gibello - Linagora
 */
public abstract class Metric extends TinomObject implements OutputInterface {

	private String[] outputNames;

	/**
	 * Creates a new metric.
	 * @param name The metric's name
	 */
	public Metric(String name) {
		super(name);
	}

	public void setOutputNames(String[] channelNames) {
		this.outputNames = channelNames;
	}

	public String[] getOutputNames() {
		return this.outputNames;
	}

	/**
	 * Retrieves data from a specified channel.
	 * @param channelName The channel name
	 * @return The data available on the channel
	 * @throws NoSuchFieldException
	 */
	public abstract String get(String channelName) throws NoSuchFieldException;
	
	/**
	 * Sets data on a specified channel. Default implementation assumes
	 * the operation is not supported, because the channel is read-only.
	 * @param channelName The channel name
	 * @param value The value to set
	 * @throws UnsupportedOperationException
	 */
	public void set(String channelName, String value) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(channelName + " is read-only");
	}
	
	public void run() {
		// Do nothing
	}
}
