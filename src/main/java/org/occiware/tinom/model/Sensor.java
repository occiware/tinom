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

/**
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
	
	private void addCollectorOrAggregator(OutputInterface collectorOrAggregator) {
		for(String channelName : collectorOrAggregator.getOutputNames()) {
			channelMap.put(channelName, collectorOrAggregator);
		}
		this.collectorsAndAggregators.add(collectorOrAggregator);
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

	public String get(String channelName) throws NoSuchFieldException {
		OutputInterface collectorOrAggregator = channelMap.get(channelName);
		if(collectorOrAggregator == null) throw new NoSuchFieldException("Unknown channel: " + channelName);
		return collectorOrAggregator.get(channelName);
	}

	@Override
	public void run() {
		// Aggregate then publish everything available
		for(OutputInterface collectorOrAggregator : collectorsAndAggregators) {
			if(collectorOrAggregator instanceof Aggregator) collectorOrAggregator.run();
		}
		for(Publisher publisher : publishers) {
			publisher.run();
		}
	}
}
