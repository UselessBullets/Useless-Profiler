package useless.profiler;

import net.fabricmc.api.ModInitializer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


public class Profiler implements ModInitializer {
    public static final String MOD_ID = "profiler";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean doProfiling = true;
	public static int poolDelayTicks = 40;
	public static String MOD_ID_TO_MEASURE = "Minecraft";
	public static String METHOD_ID_TO_MEASURE = "tick";
	protected static HashMap<String, Long> ellapsedTime = new HashMap<>();
	protected static HashMap<String, Long> startTimes = new HashMap<>();
	protected static HashMap<String, Integer> methodCalls = new HashMap<>();
	protected static HashMap<String, Long> longestTimes = new HashMap<>();
	private static int longestKey = 0;

	/**
	 * Returns the average time that the specified method takes to run in milliseconds
	 */
	public static float getAverageTime(String modID, String methodID){
		return getAverageTime(modID + "-" + methodID);
	}
	/**
	 * Returns the average time that the specified method takes to run in milliseconds
	 */
	protected static float getAverageTime(String id){if (!doProfiling){
		return -1;}
		return (((float) ellapsedTime.get(id)) /methodCalls.get(id))/1000000;
	}
	/**
	 * Marks the start point of a specified method
	 */
	public static void methodStart(String modID, String methodID){
		methodStart(modID + "-" + methodID);
	}
	/**
	 * Marks the start point of a specified method
	 */
	protected static void methodStart(String id){
		if (!doProfiling){
			return;}
		if (id.length() > longestKey){
			longestKey = id.length();
		}
		startTimes.put(id, System.nanoTime());
		ellapsedTime.putIfAbsent(id, 0L);
		if (methodCalls.get(id) == null){
			methodCalls.put(id, 1);
		} else {
			int times = methodCalls.get(id);
			methodCalls.put(id, times + 1);
		}
	}
	/**
	 * Marks the end point of a specified method
	 */
	public static void methodEnd(String modID, String methodID){
		methodEnd(modID + "-" + methodID);
	}
	/**
	 * Marks the end point of a specified method
	 */
	protected static void methodEnd(String id){
		if (!doProfiling){
			return;}
		Long deltaTime = (System.nanoTime() - startTimes.get(id));
		ellapsedTime.put(id, (ellapsedTime.get(id) + deltaTime));
		if (longestTimes.get(id) == null){
			longestTimes.put(id, deltaTime);
		} else {
			Long currentLongest = longestTimes.get(id);
			if (deltaTime > currentLongest){
				longestTimes.put(id, deltaTime);
			}
		}
	}
	public static void printTimes(){
		if (!doProfiling){
			LOGGER.info("Cannot Print, profiling disabled!");
			return;}
		StringBuilder builder = new StringBuilder("Average Time per function\n");
		for (String key: ellapsedTime.keySet()) {
			builder.append(key).append("\t\t\t").append(getAverageTime(key)).append("\t").append(ellapsedTime.get(key)).append("\t").append(methodCalls.get(key)).append("\n");
		}
		LOGGER.info(builder.toString());
	}
	public static void printTimesInRespectToID(String modID, String methodID){
		printTimesInRespectToID(modID + "-" + methodID);
	}
	/**
	 * Prints out each methods times in respect to the method provided
	 */
	protected static void printTimesInRespectToID(String id){
		if (!doProfiling){
			LOGGER.info("Cannot Print, profiling disabled!");
			return;}
		if (ellapsedTime.size() == 0) {return;}
		if (ellapsedTime.get(id) == null){
			LOGGER.info("ID currently returns Null");
			return;}
		long totalKeyTime = ellapsedTime.get(id);
		StringBuilder builder = new StringBuilder("Function Times\n")
			.append(StringUtils.rightPad("ID", longestKey)).append(" | ")
			.append(StringUtils.rightPad("Percentage", 12)).append(" | ")
			.append(StringUtils.rightPad("Average", 9)).append(" | ")
			.append(StringUtils.rightPad("Elapsed", 12)).append(" | ")
			.append(StringUtils.rightPad("Times Called", 12)).append(" | ")
			.append(StringUtils.rightPad("Longest Time", 12)).append("\n");
		for (String key: ellapsedTime.keySet()) {
			double percentage = (ellapsedTime.get(key) * 100d) /totalKeyTime;
			builder
				.append(StringUtils.rightPad(key, longestKey)).append(" | ")
				.append(StringUtils.rightPad(String.format("%3.3f", percentage) +"%", 12)).append(" | ")
				.append(StringUtils.rightPad(String.format("%.3f", getAverageTime(key)), 9)).append(" | ")
				.append(StringUtils.rightPad(String.valueOf(String.format("%.3f", ellapsedTime.get(key)/1000000f)), 12)).append(" | ")
				.append(StringUtils.rightPad(String.valueOf(methodCalls.get(key)), 12)).append(" | ")
				.append(StringUtils.rightPad(String.valueOf(String.format("%.3f", longestTimes.get(key)/1000000f)), 12)).append("\n");
		}
		LOGGER.info(builder.toString());
	}
	/**
	 * Resets the Profiler's data
	 */
	public static void clearTimes(){
		ellapsedTime.clear();
		startTimes.clear();
		methodCalls.clear();
		longestTimes.clear();
		longestKey = 0;
	}
	public static HashMap<String, Long> getLongestTimes(){
		return longestTimes;
	}
	public static HashMap<String, Integer> getMethodCalls(){
		return methodCalls;
	}
	public static HashMap<String, Long> getEllapsedTime(){
		return ellapsedTime;
	}
	public static HashMap<String, Long> getStartTimes(){
		return startTimes;
	}
    @Override
    public void onInitialize() {
		String s;
		if (doProfiling){
			s = "Profiling Enabled";
		} else {
			s = "Profiling Disabled";
		}
        LOGGER.info("Profiler initialized. " + s);
    }
}
