# tinom 
[![Build Status](http://travis-ci.org/occiware/tinom.png?branch=master)](http://travis-ci.org/occiware/tinom/builds)
[![Coverage Status](https://coveralls.io/repos/occiware/tinom/badge.svg?branch=master&service=github)](https://coveralls.io/github/occiware/tinom?branch=master)
[![License](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)

**tinom** is a Java library that aims at managing monitoring configurations of various tools.  
Said differently, it tries to abstract and provide a unique entry-point to configure monitoring tools.
This approach is based on the principles of [OCCI Monitoring](http://fr.slideshare.net/AugustoCiuffoletti/2013-03-occimonitoring-26159563).
[OCCI](http://occi-wg.org/) by itself is a set of open specifications with a focus to standardize cloud APIs management.
OCCI Monitoring is the declination of OCCI for Software supervision.

Even if originally, OCCI is about cloud computing, the fact is that OCCI Monitoring perfectly fits casual issues related to
monitoring, be it in the cloud or with usual infrastructures.

**tinom** is the anagram of **monit** and stands for *Tinom Is Not OCCI Monitoring*.

## API documentation

The detailed API doc can be found [here](monit-api.md).

## Examples

The examples below are provided in a sample package alongside with the Tinom code (org.occiware.tinom.extensions.sample.*).

### Polling JMX

Package: org.occiware.tinom.extensions.sample.jmx

Polls local JMX server to collect localhost's CPU percent use every 10 seconds,
then display it on the standard output console.

Tinom code follows:

```
public static void main(String[] args) {
		Sensor sensor = new Sensor("sample", 10);
		sensor
			.withCollector(
				(new Collector("SystemCollector"))
					.withMetric(new CpuMetric("CpuMetric")))
			.withPublisher(
				(new PrintStreamPublisher("log", System.out))
					.withInputNames(new String[] { "SystemCollector.CpuPercent", "SystemCollector.AvailableProcessors" }))
			.start();
		
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
```

The only specific implementation class is CpuMetric, provided in the sample package:

```
public class CpuMetric extends Metric {

	public CpuMetric(String name) {
		super(name);
		setOutputNames(new String[] { "CpuPercent", "AvailableProcessors" });
	}

	@Override
	public String get(String channelName) throws NoSuchFieldException {
		if("CpuPercent".equalsIgnoreCase(channelName)) {
			return Double.toString(
				ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
		} else if("AvailableProcessors".equalsIgnoreCase(channelName)) {
			return Double.toString(
				ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors());
		} else {
			throw new NoSuchFieldException("Unknown field: " + channelName);
		}
	}
}
```


### Configuring Nagios

Package: org.occiware.tinom.extensions.sample.nagios

Generates a nagios hosts.cfg file, that can be loaded in the Nagios configuration.

The example generates a nagios.cfg file with 2 hosts, called "mail01" and "localhost", as follows:

```
define host {
use template-hosts
host_name mail01
address 192.167.0.1
alias mail server
}
define host {
use template-hosts
host_name localhost
address 127.0.0.1
}
```
The file content is displayed on the standard output console.

Tinom code follows:

```
public static void main(String[] args) {
		Sensor sensor = new Sensor("sample");
		sensor
			.withCollector(
				(new NagiosHostCollector("localhost"))
					.withTemplate("template-hosts").withIpAddress("127.0.0.1"))
			.withCollector(
				(new NagiosHostCollector("mail01"))
					.withTemplate("template-hosts").withIpAddress("192.167.0.1").withAlias("mail server"))
			.withPublisher(
				(new NagiosHostsPublisher("NagiosHosts")));
		
		sensor.publishAll();
	}
```

Only NagiosHostCollector and NagiosHostPublisher are specific implementation classes, included in the sample.



### Configuring Cloud Watch for a set of VMs

Cloud Watch is the monitoring solution of Amazon Web Services.

*todo*


### Getting the Nagios Configuration for an Apache Web Server

*todo*
