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

package org.occiware.tinom;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * The main class to use Tinom's APIs.
 * @author Vincent Zurczak - Linagora
 */
public class Tinom {

	static final String KNOWN_HOSTS = "/home/diarraa/.ssh/known_hosts";
	static final String PRIVATE_KEY = "/home/diarraa/.ssh/id_dsa";
	static final String DEFAULT_USERNAME = "diarra";
	static final String DEFAULT_PASSWORD = "";
	/**
	 * Connects to a remote host in ssh and executes a command.
	 *
	 *@param jsch used for ssh connection
	 * @param host the name or the IP of the remote host
	 * @param username a user to access to remote host
	 * @param password the password associated to user
	 * @param command a shell command to execute on remote host
	 * @throws JSchException
	 * */
	public void connectAndExecuteCommand(JSch jsch, String host, String username, String password, String command) throws JSchException {
		// Session configuration
		Session session = jsch.getSession(username, host, 22);
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
		File agentsIP = new File((String) prop.get("rbcf.ips.file"));
		List<String> ips = Utils.readFileByLine(agentsIP);
		String elasticSearchIP = (String) prop.get("address");

		// Prepare the command to execute on each agent
		String command = "sed -i \'s/address = localhost/address = "+elasticSearchIP+"/g\' "+configFilePath;

		// Configure ssh
		JSch jsch = new JSch();
		jsch.setKnownHosts(KNOWN_HOSTS);
		jsch.addIdentity(PRIVATE_KEY);

		for( String ip : ips) {
			connectAndExecuteCommand(jsch, ip, DEFAULT_USERNAME, DEFAULT_PASSWORD, command);
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
		File agentsIP = new File((String) prop.get("rbcf.ips.file"));
		List<String> ips = Utils.readFileByLine(agentsIP);

		// prepare the command to execute
		StringBuilder sb = new StringBuilder();
		String scriptContent = Utils.readFileContent(script);
		String scriptName = script.getName();
		sb.append( "echo " + scriptContent + ">" + scriptName );
		sb.append( " ;" + " chmod u+x " + scriptName + " ;" + " ./"+scriptName );
		String command = sb.toString();

		// Configure ssh
		JSch jsch = new JSch();
		jsch.setKnownHosts(KNOWN_HOSTS);
		jsch.addIdentity(PRIVATE_KEY);

		for( String ip : ips) {
			connectAndExecuteCommand(jsch, ip, DEFAULT_USERNAME, DEFAULT_PASSWORD, command);
		}
	}

}
