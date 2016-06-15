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

package org.occiware.tinom.extensions.sample.jmx;

import java.lang.management.ManagementFactory;

import org.occiware.tinom.model.Metric;

/**
 * @author Pierre-Yves Gibello - Linagora
 */
public class CpuMetric extends Metric {

	public CpuMetric(String name) {
		super(name);
		setOutputNames(new String[] { "CpuPercent", "AvailableProcessors" });
	}

	@Override
	public String get(String channelName) throws NoSuchFieldException {
		if("CpuPercent".equalsIgnoreCase(channelName)) {
			return Double.toString(
				ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
		} else if("AvailableProcessors".equalsIgnoreCase(channelName)) {
			return Double.toString(
				ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors());
		} else {
			throw new NoSuchFieldException("Unknown field: " + channelName);
		}
	}
}
