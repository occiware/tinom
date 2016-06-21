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

import org.occiware.tinom.model.Aggregator;
import org.occiware.tinom.model.Collector;
import org.occiware.tinom.model.Metric;

/**
 * Aggregator that takes as inputs all metrics of a collector.
 * @author Pierre-Yves Gibello - Linagora
 *
 */
public abstract class CollectorAggregator extends Aggregator {

	/**
	 * Creates a new CollectorAggregator.
	 * @param name This aggregator's name
	 * @param collector The collector that provides metrics to aggregate
	 */
	public CollectorAggregator(String name, Collector collector) {
		super(name);
		for(Metric metric : collector.getMetrics()) {
			for(String channelName : metric.getOutputNames()) {
				this.withInput(metric, channelName);
			}
		}
	}

}
