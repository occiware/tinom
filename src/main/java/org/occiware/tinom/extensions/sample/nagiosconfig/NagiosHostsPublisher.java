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

import org.occiware.tinom.extensions.utils.PatternMatchingPublisher;

/**
 * A publisher to produce a nagios hosts.cfg config file.
 * @author Pierre-Yves Gibello - Linagora
 *
 */
public class NagiosHostsPublisher extends PatternMatchingPublisher {

	/**
	 * Creates a nagios host publisher.
	 * @param name This publisher's name
	 */
	public NagiosHostsPublisher(String name) {
		super(name, ".*\\.nagioshost", System.out);
	}
	
	@Override
	public void doPublish(String name, String value) {
		getOutputStream().println(value);
	}
	
	@Override
	public void doPublish(String name, Exception e) {
		// Ignore exception
	}

}
