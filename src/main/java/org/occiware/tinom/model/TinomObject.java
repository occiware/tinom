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

/**
 * @author Pierre-Yves Gibello - Linagora
 */
public class TinomObject {
	public String name;

	/**
	 * Any object in Tinom model has a unique name.
	 * @param name The object's name
	 */
	public TinomObject(String name) {
		this.name = name;
	}

	/**
	 * Computes and returns the object ID.
	 * @return The object ID
	 */
	public String getId() {
		return "urn:uuid:" + this.name;
	}
	
	/**
	 * Retrieves this object's name.
	 * @return This object's name
	 */
	public String getName() {
		return this.name;
	}
}
