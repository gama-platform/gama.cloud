package com.jogamp.opengl;

import java.io.PrintStream;

public interface GLAnimatorControl {

	public interface UncaughtExceptionHandler {

	}

	void setUpdateFPSFrames(int frames, PrintStream out);

	void resetFPSCounter();

	int getUpdateFPSFrames();

	long getFPSStartTime();

	long getLastFPSUpdateTime();

	long getLastFPSPeriod();

	float getLastFPS();

	int getTotalFPSFrames();

	long getTotalFPSDuration();

	float getTotalFPS();

	boolean isStarted();

	boolean isAnimating();

	boolean isPaused();

	Thread getThread();

	boolean start();

	boolean stop();

	boolean pause();

	boolean resume();

	void add(GLAutoDrawable drawable);

	void remove(GLAutoDrawable drawable);

	UncaughtExceptionHandler getUncaughtExceptionHandler();

	void setUncaughtExceptionHandler(UncaughtExceptionHandler handler);

	void uncaughtException(GLAnimatorControl animator, GLAutoDrawable drawable, Throwable cause);

}
