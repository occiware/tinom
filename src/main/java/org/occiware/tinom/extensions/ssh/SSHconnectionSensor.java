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

package org.occiware.tinom.extensions.ssh;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.occiware.tinom.Utils;
import org.occiware.tinom.model.Sensor;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * The class to use SSH connection with Tinom.
 * @author Amadou Diarra - UGA
 * */
public class SSHconnectionSensor extends Sensor {

	/**
	 * The constructor.
	 * */
	public SSHconnectionSensor(String name) {
		super(name);
	}


	private static final String KNOWN_HOSTS = "known.hosts";
	private static final String PRIVATE_KEY = "private.key";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String IPS_FILE = "ips.file";
	private static final String SSH_PORT = "ssh.port";


	/**
	 * Validates a properties file content.
	 * @param properties the properties
	 * @throws Exception if an error occurred during the validation
	 * */
	public static void validate( Map<String,String> properties ) throws Exception {

		Utils.checkKey(KNOWN_HOSTS, properties);
		Utils.checkKey(PRIVATE_KEY, properties);
		Utils.checkKey(USERNAME, properties);
		Utils.checkKey(PASSWORD, properties);
		Utils.checkKey(IPS_FILE, properties);
		Utils.checkKey(SSH_PORT, properties);
	}


	/**
	 * Connects to a remote host in ssh and executes a command.
	 *
	 * @param jsch used for ssh connection
	 * @param host the name or the IP of the remote host
	 * @param username a user to access to remote host
	 * @param password the password associated to user
	 * @param command a shell command to execute on remote host
	 * @param port ssh port
	 * @throws JSchException
	 * */
	public void connectAndExecuteCommand(JSch jsch, String host, String username, String password, String command, int port) {

		// Session configuration
		Session session = null;
		Channel channel = null;
		try {

			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			session.connect();

			// Command execution
			channel = session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);
			channel.connect();

		} catch (JSchException e) {

			e.printStackTrace();
		} finally {

			session.disconnect();
			channel.disconnect();
		}
	}


	/**
	 * Sends a script file to agents in ssh and executes it.
	 * @param properties a properties file which indicates the agents IP location file
	 * @param script a script to execute
	 * @param appName an application name (useful when Tinom will integrate to Roboconf)
	 * @throws Exception
	 * */
	public void sendAndExecuteScript(File properties, File script, String appName) throws Exception {

		Properties prop = Utils.readPropertiesFile(properties);

		// Validate properties file
		Map<String,String> propMap = Utils.propertiesFileToMap(prop);
		validate(propMap);

		File agentsIP = new File((String) prop.get(IPS_FILE));
		String knownHosts = (String) prop.get(KNOWN_HOSTS);
		String privKey = (String) prop.get(PRIVATE_KEY);
		String username = (String) prop.get(USERNAME);
		String password = (String) prop.get(PASSWORD);
		int port = Integer.parseInt( (String) prop.get(SSH_PORT) );

		List<String> ips = Utils.readFileByLine(agentsIP);

		// Prepare the command to execute
		StringBuilder sb = new StringBuilder();
		String scriptContent = Utils.readFileContent(script);

		sb.append("bash <<ENDOFSCRIPT\n");
		sb.append(scriptContent);
		sb.append("\n");
		sb.append("ENDOFSCRIPT");
		String command = sb.toString();

		// Configure ssh
		JSch jsch = new JSch();
		jsch.setKnownHosts(knownHosts);
		jsch.addIdentity(privKey);

		for( String ip : ips) {
			connectAndExecuteCommand(jsch, ip, username, password, command, port);
		}
	}

}
