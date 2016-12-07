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

import java.io.File;
import java.io.IOException;

import org.occiware.tinom.Tinom;
import com.jcraft.jsch.JSchException;

/**
 * Sample application to generate a nagios hosts.cfg configuration file.
 * @author Pierre-Yves Gibello - Linagora
 */
public class SampleApp {

	public static void main(String[] args) throws JSchException, IOException {
		/*Sensor sensor = new Sensor("sample");
		sensor
			.withCollector(
				(new NagiosHostCollector("localhost"))
					.withTemplate("template-hosts").withIpAddress("127.0.0.1"))
			.withCollector(
				(new NagiosHostCollector("mail01"))
					.withTemplate("template-hosts").withIpAddress("192.167.0.1").withAlias("mail server"))
			.withPublisher(
				(new NagiosHostsPublisher("NagiosHosts")));

		sensor.publishAll();*/
		/*JSch jsch=new JSch();
		jsch.setKnownHosts("/home/diarraa/.ssh/known_hosts");

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		config.put("-n", "touch toto.txt");
		Map<Object, Object> data = config;
		System.out.println("taille = "+data.get("-n"));

		Session session=jsch.getSession("diarra", "ligone", 22);
		session.setConfig(config);
		session.setPassword("");
		session.connect(3000);
		//session.setX11Host("ligone");
		Channel channel=session.openChannel("exec");
		//InputStream stream = new ByteArrayInputStream("touch toto.txt".getBytes(StandardCharsets.UTF_8));
		//channel.setInputStream(stream);
		String command = "mkdir toto ; cd toto ; echo Bonjour le monde cruel > toto.txt";
		((ChannelExec)channel).setCommand(command);
	    //channel.setInputStream(System.in);
	    //channel.setOutputStream(System.out);
	    channel.connect();

	    session.disconnect();
	    channel.disconnect();*/

		Tinom tinom = new Tinom();
		File prop = new File("/home/diarraa/git/tinom/toto.properties");
		File script = new File("/home/diarraa/git/tinom/script");
		//String configFilePath = "etc/org.apache.karaf.decanter.appender.elasticsearch.rest.cfg";

		//tinom.updateElasticSearch(prop,configFilePath);
		tinom.sendAndExecuteScript(prop, script, "lamp");

	}

}
