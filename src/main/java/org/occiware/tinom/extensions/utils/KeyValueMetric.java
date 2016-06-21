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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.occiware.tinom.model.Metric;

/**
 * A metric whose channels are key/value pairs.
 * @author Pierre-Yves Gibello - Linagora
 *
 */
public class KeyValueMetric extends Metric {

	private Map<String, String> channelMap = new HashMap<String, String>();

	/**
	 * Creates a new key/value metric.
	 * @param name The metric's name
	 */
	public KeyValueMetric(String name) {
		super(name);
	}

	/**
	 * Creates a new key/value metric.
	 * @param name The metric's name
	 * @param key The key = a channel name
	 * @param value The channel value
	 */
	public KeyValueMetric(String name, String key, String value) {
		this(name);
		this.channelMap.put(key, value);
		refreshOutputNames();
	}

	/**
	 * Creates a new key/value metric, with a name equal to the key.
	 * @param key The key = a channel name
	 * @param value The channel value
	 */
	public KeyValueMetric(String key, String value) {
		this(key, key, value);
	}
	
	/**
	 * Add a key/value pair.
	 * @return The channels map
	 */
	public void put(String key, String value) {
		this.channelMap.put(key, value);
		refreshOutputNames();
	}

	@Override
	public String get(String channelName) throws NoSuchFieldException {
		String value = null;
		if((value = this.channelMap.get(channelName)) != null) {
			return value;
		} else {
			throw new NoSuchFieldException("Unknown channel: " + channelName);
		}
	}

	private void refreshOutputNames() {
		Set<String> keys = this.channelMap.keySet();
		this.setOutputNames(keys.toArray(new String[keys.size()]));
	}
}
