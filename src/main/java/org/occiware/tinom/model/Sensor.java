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
 * A sensor.
 * @author Pierre-Yves Gibello - Linagora
 */
public class Sensor extends PeriodicTask {

	private List<OutputInterface> collectorsAndAggregators = new LinkedList<OutputInterface>();
	private Map<String, OutputInterface> channelMap = new HashMap<String, OutputInterface>();
	private List<Publisher> publishers = new LinkedList<Publisher>();

	/**
	 * Creates a new Sensor.
	 * @param name The sensor's name
	 */
	public Sensor(String name, int period) {
		super(name, period);
	}
	
	/**
	 * Creates a new Sensor without polling.
	 * @param name The sensor's name
	 */
	public Sensor(String name) {
		super(name, -1);
	}

	/**
	 * Adds a collector to this sensor.
	 * @param collector The collector to add
	 * @return This sensor
	 */
	public Sensor withCollector(Collector collector) {
		addCollectorOrAggregator(collector);
		return this;
	}

	/**
	 * Adds an aggregator to this sensor.
	 * @param aggregator The aggregator to add
	 * @return This sensor
	 */
	public Sensor withAggregator(Aggregator aggregator) {
		addCollectorOrAggregator(aggregator);
		return this;
	}
	
	/**
	 * Adds a collector or an aggregator to this sensor.
	 * @param collectorOrAggregator The collector or aggregator to add
	 */
	private void addCollectorOrAggregator(OutputInterface collectorOrAggregator) {
		for(String channelName : collectorOrAggregator.getOutputNames()) {
			channelMap.put(collectorOrAggregator.getName() + "." + channelName, collectorOrAggregator);
		}
		this.collectorsAndAggregators.add(collectorOrAggregator);
	}

	/**
	 * Retrieves the input channel names.
	 * @return The input channel names
	 */
	public String[] getInputNames() {
		Set<String> keys = channelMap.keySet();
		return keys.toArray(new String[keys.size()]);
	}

	/**
	 * Adds a publisher to this sensor.
	 * @param aggregator The publisher to add
	 * @return This sensor
	 */
	public Sensor withPublisher(Publisher publisher) {
		publisher.setSensor(this);
		this.publishers.add(publisher);
		return this;
	}

	/**
	 * Retrieves the value available on a channel.
	 * @param channelName The channel name = <the collector's name>.<the collector's channel name>
	 * @return The channel's value
	 * @throws NoSuchFieldException
	 */
	public String get(String channelName) throws NoSuchFieldException {
		OutputInterface collectorOrAggregator = channelMap.get(channelName);
		if(collectorOrAggregator == null) throw new NoSuchFieldException("Unknown channel: " + channelName);
		String channel = channelName;
		if(channel.startsWith(collectorOrAggregator.getName() + ".")) channel = channelName.substring(collectorOrAggregator.getName().length()+1);
		return collectorOrAggregator.get(channel);
	}

	/**
	 * Run all available publishers.
	 */
	public void publishAll() {
		for(Publisher publisher : publishers) {
			publisher.run();
		}
	}

	@Override
	public void run() {
		// Aggregate then publish everything available
		for(OutputInterface collectorOrAggregator : collectorsAndAggregators) {
			if(collectorOrAggregator instanceof Aggregator) collectorOrAggregator.run();
		}
		publishAll();
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder("Sensor ID: " + getId() + " / name: " + getName() + "\n"
				+ "  |-- Full input list: { ");
		
		boolean first = true;
		for (String inputName: getInputNames()) {
			ret.append((first ? "" : ", ") + inputName);
			first = false;
		}
		ret.append("}\n");
		return ret.toString();
	}
}
