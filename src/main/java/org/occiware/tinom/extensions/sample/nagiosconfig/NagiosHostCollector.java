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

package org.occiware.tinom.extensions.sample.nagiosconfig;

import org.occiware.tinom.extensions.utils.KeyValueMetric;
import org.occiware.tinom.model.Collector;

/**
 * A collector designed to represent a nagios host configuration.
 * @author Pierre-Yves Gibello - Linagora
 *
 */
public class NagiosHostCollector extends Collector {

	NagiosHostMetric hostMetric = new NagiosHostMetric("nagioshost");

	/**
	 * Creates a new host collector.
	 * @param hostname host name
	 */
	public NagiosHostCollector(String hostname) {
		super(hostname);
		this.withMetric(this.hostMetric);
		hostMetric.put("host_name", hostname);
	}

	public NagiosHostCollector withTemplate(String template) {
		hostMetric.put("template", template);
		return this;
	}

	public NagiosHostCollector withIpAddress(String ip) {
		hostMetric.put("address", ip);
		return this;
	}

	public NagiosHostCollector withAlias(String alias) {
		hostMetric.put("alias", alias);
		return this;
	}
}

/**
 * A metric that represents a nagios host.
 * @author Pierre-Yves Gibello - Linagora
 *
 */
class NagiosHostMetric extends KeyValueMetric {

	public NagiosHostMetric(String name) {
		super(name, "nagioshost", "");
	}
	
	@Override
	public String get(String channelName) throws NoSuchFieldException {
		if("nagioshost".equalsIgnoreCase(channelName)) {
			StringBuffer buf = new StringBuffer("define host {");
			String s;
			if((s = getWithoutError("template")) != null) buf.append("\nuse " + s);
			buf.append("\nhost_name " + super.get("host_name"));
			if((s = getWithoutError("address")) != null) buf.append("\naddress " + s);
			if((s = getWithoutError("alias")) != null) buf.append("\nalias " + s);
			buf.append("\n}");
			return buf.toString();
		} else {
			return super.get(channelName);
		}
	}

}
