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

package org.occiware.tinom.sample;

import org.occiware.tinom.extensions.sample.nagiosconfig.NagiosHostCollector;
import org.occiware.tinom.extensions.sample.nagiosconfig.NagiosHostsPublisher;
import org.occiware.tinom.model.Sensor;

/**
 * Sample application to generate a nagios hosts.cfg configuration file.
 * @author Pierre-Yves Gibello - Linagora
 */
public class SampleApp {

	public static void main(String[] args) {
		Sensor sensor = new Sensor("sample");
		sensor
			.withCollector(
				(new NagiosHostCollector("localhost"))
					.withTemplate("template-hosts").withIpAddress("127.0.0.1"))
			.withCollector(
				(new NagiosHostCollector("mail01"))
					.withTemplate("template-hosts").withIpAddress("192.167.0.1").withAlias("mail server"))
			.withPublisher(
				(new NagiosHostsPublisher("NagiosHosts")));

		sensor.publishAll();

	}

}
