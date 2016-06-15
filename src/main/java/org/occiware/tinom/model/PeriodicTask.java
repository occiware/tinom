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

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Pierre-Yves Gibello - Linagora
 */
public abstract class PeriodicTask extends TinomObject implements Runnable {

	private int period;
	private PeriodicTaskManager manager;

	public PeriodicTask(String name, int period) {
		super(name);
		this.period = period;
		this.manager = new PeriodicTaskManager(period, this);
	}

	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
	
	public void start() {
		this.manager.start();
	}
	
	public void stop() {
		this.manager.stop();
	}
}

/**
 * @author Pierre-Yves Gibello - Linagora
 */
class PeriodicTaskManager extends TimerTask {

	private int period;
	private Runnable task;
	private Timer timer;

	public PeriodicTaskManager(int period, Runnable task) {
		this.period = period;
		this.task = task;
	}

	public int getPeriod() {
		return this.period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
	
	public void start() {
		if(this.timer != null) {
			this.timer.cancel();
		}
		this.timer = new Timer(true);
		this.timer.schedule(this, 0, period * 1000);
	}
	
	public void stop() {
		if(this.timer != null) {
			this.timer.cancel();
		}
		this.timer = null;
	}

	@Override
	public void run() {
		task.run();
	}

}
