package org.occiware.tinom.extensions.ssh;

import java.io.File;

public class ToRunByHand {

	public static void main(String[] args) {

		SSHconnectionSensor sshConnection = new SSHconnectionSensor("Connexion ssh");

		File prop = new File("/home/diarraa/git/tinom/toto.properties");
		File script = new File("/home/diarraa/git/tinom/script");
		//String configFilePath = "etc/org.apache.karaf.decanter.appender.elasticsearch.rest.cfg";

		//tinom.updateElasticSearch(prop,configFilePath);
		try {
			sshConnection.sendAndExecuteScript(prop, script, "lamp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
