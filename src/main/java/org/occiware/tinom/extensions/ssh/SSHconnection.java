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
import java.io.IOException;
import java.util.List;
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
public class SSHconnection extends Sensor {

	/**
	 * The constructor.
	 * */
	public SSHconnection(String name) {
		super(name);
	}


	static final String KNOWN_HOSTS = "known.hosts";
	static final String PRIVATE_KEY = "private.key";
	static final String USERNAME = "username";
	static final String PASSWORD = "password";
	static final String RBCF_IPS_FILE = "rbcf.ips.file";
	static final String ADDRESS = "address";
	static final String SSH_PORT = "ssh.port";
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
	public void connectAndExecuteCommand(JSch jsch, String host, String username, String password, String command, int port) throws JSchException {

		// Session configuration
		Session session = jsch.getSession(username, host, port);
		session.setPassword(password);
		session.connect();

		// Command execution
		Channel channel = session.openChannel("exec");
		((ChannelExec)channel).setCommand(command);
		channel.connect();

		session.disconnect();
		channel.disconnect();
	}


	/**
	 * Updates Elasticsearch IP adress in decanter configuration file.
	 * @param properties a properties file which indicates the agents IP location file
	 * @param configFilePath the path to configuration file which be updated on remote host
	 * @throws IOException
	 * @throws JSchException
	 * */
	public void updateElasticSearch(File properties, String configFilePath) throws IOException, JSchException {

		Properties prop = Utils.readPropertiesFile(properties);
		File agentsIP = new File((String) prop.get(RBCF_IPS_FILE));

		String elasticSearchIP = (String) prop.get(ADDRESS);
		String knownHosts = (String) prop.get(KNOWN_HOSTS);
		String privKey = (String) prop.get(PRIVATE_KEY);
		String username = (String) prop.get(USERNAME);
		String password = (String) prop.get(PASSWORD);
		int port = (int) prop.get(SSH_PORT);

		List<String> ips = Utils.readFileByLine(agentsIP);


		// Prepare the command to execute on each agent
		String command = "sed -i \'s/address = localhost/address = "+elasticSearchIP+"/g\' "+configFilePath;

		// Configure ssh
		JSch jsch = new JSch();
		jsch.setKnownHosts(knownHosts);
		jsch.addIdentity(privKey);

		for( String ip : ips) {
			connectAndExecuteCommand(jsch, ip, username, password, command, port);
		}
	}


	/**
	 * Sends a script file to agents in ssh and executes it.
	 * @param properties a properties file which indicates the agents IP location file
	 * @param script a script to execute
	 * @param appName an application name (useful when Tinom will integrate to Roboconf)
	 * @throws IOException
	 * @throws JSchException
	 * */
	public void sendAndExecuteScript(File properties, File script, String appName) throws IOException, JSchException {

		Properties prop = Utils.readPropertiesFile(properties);
		File agentsIP = new File((String) prop.get(RBCF_IPS_FILE));

		String knownHosts = (String) prop.get(KNOWN_HOSTS);
		String privKey = (String) prop.get(PRIVATE_KEY);
		String username = (String) prop.get(USERNAME);
		String password = (String) prop.get(PASSWORD);
		int port = Integer.parseInt( (String) prop.get(SSH_PORT) );

		List<String> ips = Utils.readFileByLine(agentsIP);

		// prepare the command to execute
		StringBuilder sb = new StringBuilder();
		String scriptContent = Utils.readFileContent(script);

		sb.append("bash <<ENDOFSCRIPT ");
		sb.append(scriptContent);
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
