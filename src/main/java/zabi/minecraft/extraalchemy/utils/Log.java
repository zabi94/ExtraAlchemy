package zabi.minecraft.extraalchemy.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StringFormattedMessage;

public class Log {
	
	private static final Logger logger = LogManager.getLogger(LibMod.MOD_NAME, new ModMessageFactory(LibMod.MOD_NAME));
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
	
	public static void d(String s) {
		if (debug) {
			i("[DEBUG] -- " + s);
		} else {
			logger.debug(s);
		}
	}
	
	public static void i(String s, Object... params) {
		logger.info(String.format(s, params));
	}

	public static void w(String s, Object... params) {
		logger.warn(String.format(s, params));
	}

	public static void e(String s, Object... params) {
		logger.error(String.format(s, params));
	}
	
	public static void d(String s, Object... params) {
		if (debug) {
			i("[DEBUG] -- " + String.format(s, params));
		} else {
			logger.debug(String.format(s, params));
		}
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

	public static Logger raw() {
		return logger;
	}
	
	public static void printAndPropagate(Throwable t) {
		t.printStackTrace();
		throw new PropagatedException(t);
	}
	
	public static class PropagatedException extends RuntimeException {

		private static final long serialVersionUID = -6966873808580517300L;
		
		public PropagatedException(Throwable t) {
			super(t);
		}
		
	}
	
	private static class ModMessageFactory extends AbstractMessageFactory {

		private static final long serialVersionUID = 4790631706836869571L;
		
		private String prefix = "";
		
		public ModMessageFactory(String modid) {
			prefix = "[" + modid + "] ";
		}

		@Override
		public Message newMessage(Object message) {
			return newMessage(prefix + message.toString());
		}

		@Override
		public Message newMessage(String message) {
			return new StringFormattedMessage(prefix + message.toString());
		}

		@Override
		public Message newMessage(String message, Object... params) {
			return new StringFormattedMessage(prefix + message, params);
		}

	}
}
