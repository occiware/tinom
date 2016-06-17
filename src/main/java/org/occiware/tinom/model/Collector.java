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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Pierre-Yves Gibello - Linagora
 */
public class Collector extends PeriodicTask implements OutputInterface {

	private Map<String, Metric> channelMap = new HashMap<String, Metric>();
	private List<Metric> metrics = new LinkedList<Metric>();

	/**
	 * Creates a new collector.
	 * @param name The collector's name
	 * @param period The polling period (0 or less means no polling)
	 */
	public Collector(String name, int period) {
		super(name, period);
	}
	
	/**
	 * Creates a new collector without polling.
	 * @param name The collector's name
	 */
	public Collector(String name) {
		super(name, -1);
	}

	/**
	 * Adds a metric to this collector. All metric's channels are added to
	 * this collector.
	 * @param metric The metric to add
	 * @return This collector
	 */
	public Collector withMetric(Metric metric) {
		for(String channelName : metric.getOutputNames()) {
			this.channelMap.put(channelName, metric);
		}
		metrics.add(metric);
		return this;
	}
	
	
	/**
	 * Retrieves all metrimetriccs in this collector.
	 * @return All metrics in this collector
	 */
	public List<Metric> getMetrics() {
		return this.metrics;
	}

	/**
	 * Adds metrics in this collector.
	 * @param metrics The metrics list to add
	 */
	public void addMetrics(List<Metric> metrics) {
		if(metrics != null) {
			for(Metric metric : metrics) {
				this.withMetric(metric);
			}
		}
	}
	
	public String[] getOutputNames() {
		Set<String> keys = channelMap.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	/**
	 * Retrieves data from a specified channel.
	 * @param channelName The channel name
	 * @return The data available on the channel
	 */
	public String get(String channelName) throws NoSuchFieldException {
		Metric metric = this.channelMap.get(channelName);
		if(metric == null) throw new NoSuchFieldException(this.getName() + "." + channelName);
		else return metric.get(channelName);
	}
	
	public void run() {
		// Default implementation does nothing
	}

}
