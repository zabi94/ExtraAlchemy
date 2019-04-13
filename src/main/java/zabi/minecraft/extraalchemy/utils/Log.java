package zabi.minecraft.extraalchemy.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
	
	private static final Logger logger = LogManager.getLogger(LibMod.MOD_NAME);
	private static boolean debug = "true".equals(System.getProperty("debug"));

	public static void i(String s) {
		logger.info(s);
	}

	public static void w(String s) {
		logger.warn(s);
	}

	public static void e(String s) {
		logger.error(s);
	}

	public static void i(Object s) {
		logger.info(s);
	}

	public static void w(Object s) {
		logger.warn(s);
	}

	public static void e(Object s) {
		logger.error(s);
	}

	public static void d(String s) {
		if (debug) {
			i("[DEBUG] -- " + s);
		} else {
			logger.debug(s);
		}
	}
}
