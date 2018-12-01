package com.tltt.lib;

import org.apache.commons.lang3.time.StopWatch;

public class SuperStopWatch {

	private StopWatch stopWatch;

	public SuperStopWatch() {
		stopWatch = new StopWatch();
		stopWatch.start();
	}
	
	public long getMsElaspedAndRestart() {
		long elasped = stopWatch.getTime();
		stopWatch.reset();
		stopWatch.start();
		return elasped;
	}
}
