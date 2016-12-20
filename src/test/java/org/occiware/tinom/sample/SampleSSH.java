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

import java.io.File;

import org.occiware.tinom.extensions.ssh.SSHconnectionSensor;

/**
 * A class to test sensors configuration with SSH.
 * @author Amadou Diarra - UGA
 * */
public class SampleSSH {

	public static void main(String[] args) {

		SSHconnectionSensor sshConnection = new SSHconnectionSensor("Connexion ssh");

		File prop = new File("/home/diarraa/git/tinom/ipsList.properties");
		File script = new File("/home/diarraa/git/tinom/script");

		try {
			sshConnection.sendAndExecuteScript(prop, script, "lamp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
