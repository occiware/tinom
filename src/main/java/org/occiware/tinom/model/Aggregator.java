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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pierre-Yves Gibello - Linagora
 */
public abstract class Aggregator extends Metric implements OutputInterface {

	private Map<String, Metric> channelMap = new HashMap<String, Metric>();

	/**
	 * Creates a new aggregator.
	 * @param name The aggregator's name
	 */
	public Aggregator(String name) {
		super(name);
	}

	/**
	 * Adds a metric channel to this sensor.
	 * @param metric The metric to add
	 * @param channelName The channel name
	 * @return This aggregator
	 */
	public Aggregator withInput(Metric metric, String channelName) {
		// Add specified channel as input
		this.channelMap.put(channelName, metric);
		return this;
	}
	
	public String[] getOutputNames() {
		return (String[]) channelMap.keySet().toArray();
	}
}
