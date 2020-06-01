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
public abstract class Publisher extends TinomObject implements Runnable {

	private String[] inputNames;
	private Sensor sensor;

	/**
	 * Creates a new Publisher.
	 * @param name The publisher's name
	 */
	public Publisher(String name) {
		super(name);
	}
	
	protected void setSensor(Sensor sensor) {
		this.sensor = sensor;
		// Default full input list, except if already filtered
		if(this.inputNames == null) this.inputNames = sensor.getInputNames();
	}
	
	public Publisher withInputNames(String[] channelNames) {
		this.inputNames = channelNames;
		return this;
	}

	public String[] getInputNames() {
		return this.inputNames;
	}

	//TODO NPE ??
	public String get(String channelName) throws NoSuchFieldException {
		return sensor.get(channelName);
	}
}
