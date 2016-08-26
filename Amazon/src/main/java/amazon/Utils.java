package amazon;

public class Utils {

	public static boolean isWindows() {
		String os = System.getenv("os").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		String os = System.getenv("os").toLowerCase();
		return (os.indexOf("mac") >= 0);
	}
}
