package no.minecraft.serverstatus;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class PerformanceMonitor {
	public double memory = (double)((long)(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / 1000000L) / 1000.0D);
	
	// The following fields are set to 20 by default. This will make the plugin report 100% tickrate until a true average can be calculated
	public int lowestTPS = 20;
	public int highestTPS = 20;
	public float avgTPSlastHour = 20;
	public float avgTPSlastDay = 20;
	
	public double impact = 0; // Number of milliseconds it took for the plugin to calculate last server performance
	
	private List<Integer> ticks = new ArrayList<Integer>();
	private int tickCount = 0;
	private long time = System.nanoTime();
	
	private int oneHour = 3600;
	private int oneDay = 86400;
	
	public void tick() {
		long duration = System.nanoTime();
		
		tickCount++;
		
		if(System.nanoTime() - time >= 1000000000) { // 1 second
			while(ticks.size() > oneDay) {
				// Maximum size reached. Pop the oldest element
				ticks.remove(0);
			}
			
			// Tick rates higher than 20/s are possible, but should not be reported as such
			if(tickCount > 20) {
				tickCount = 20;
			}
			
			ticks.add(tickCount);
			
			if(highestTPS < 20 && tickCount > highestTPS) {
				highestTPS = tickCount;
				
				if(highestTPS > 20) {
					highestTPS = 20;
				}
			}
			
			if(lowestTPS > 0 && tickCount < lowestTPS) {
				lowestTPS = tickCount;
			}
			
			calculateAverages();
			
			tickCount = 0;
			time = System.nanoTime();
		}
		
		impact = (double)(((long)(System.nanoTime() - duration) / 1000000) / 1000.0);
	}
	
	private void calculateAverages() {
		float sum = 0;
		float count = 0;
		int index = ticks.size() - 1;
		
		for(; index >= 0; index--) {
			sum += ticks.get(index);
			count++;
			
			if(count >= oneHour) { // 1 hour
				avgTPSlastHour = round(sum / count);
			}
		}
		
		if(count >= oneDay) {
			avgTPSlastDay = round(sum / count);
		}
	}
	
	private float round(float number) {
		return (float)((int)(number * 100) / 100F);
	}
}