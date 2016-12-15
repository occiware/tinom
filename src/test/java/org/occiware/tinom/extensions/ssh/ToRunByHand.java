package org.occiware.tinom.extensions.ssh;

import java.io.File;
import java.io.IOException;

import com.jcraft.jsch.JSchException;

public class ToRunByHand {

	public static void main(String[] args) {

		SSHconnection sshConnection = new SSHconnection("Connexion ssh");

		File prop = new File("/home/diarraa/git/tinom/toto.properties");
		File script = new File("/home/diarraa/git/tinom/script");
		//String configFilePath = "etc/org.apache.karaf.decanter.appender.elasticsearch.rest.cfg";

		//tinom.updateElasticSearch(prop,configFilePath);
		try {
			sshConnection.sendAndExecuteScript(prop, script, "lamp");
		} catch (IOException | JSchException e) {
			e.printStackTrace();
		}
	}
}
