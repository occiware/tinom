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

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A publisher that performs patter-matching to select channels by name.
 * @author Pierre-Yves Gibello - Linagora
 *
 */
public abstract class PatternMatchingPublisher extends PrintStreamPublisher {

	private Pattern pattern;

	/**
	 * Creates a new pattern matching publisher.
	 * @param name The publisher's name
	 * @param pattern The regexp to match
	 */
	public PatternMatchingPublisher(String name, String pattern, PrintStream out) {
		super(name, out);
		this.pattern = Pattern.compile(pattern);
	}

	@Override
	public void run() {
		for(String input : getInputNames()) {
			Matcher matcher = this.pattern.matcher(input);
			if(matcher.matches()) { // input matches pattern ?
				try {
					doPublish(input, get(input));
				} catch(NoSuchFieldException e) {
					doPublish(input, e);
				}
			}
		}

	}

}
