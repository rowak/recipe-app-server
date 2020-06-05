package io.github.rowak.recipesappserver;

import java.io.IOException;

import io.github.rowak.recipesappserver.net.Server;

public class Main {
	static int DEFAULT_PORT = 1967;
	
	public static void main(String[] args) {
		int port = getIntArg("--port", "-p", DEFAULT_PORT, args);
		boolean help = hasArg("--help", null, false, args);
		boolean version = hasArg("--version", null, false, args);
		if (help) {
			System.out.println("Usage: recipes-app-server [-p port]");
			System.out.println("Companion server for RecipeApp that acts"
					+ "as the medium between the client and the Heroku database.");
			System.out.println("\nOptions:");
			System.out.println("  -p, --port            specifies the port for the server to run on");
			System.out.println("      --help            display this help and exit");
			System.out.println("      --version         output version information and exit");
			System.out.println("\nExit status:");
			System.out.println("1  failed to start the server");
			System.exit(0);
		} else if (version) {
			System.out.println("recipes-app-server " + Server.VERSION);
			System.out.println("https://github.com/rowak/recipes-app-server");
			System.exit(0);
		}
		try {
			Server server = new Server(port);
		} catch (IOException ioe) {
			System.out.println("ERROR: Failed to start the server");
			ioe.printStackTrace();
			System.exit(3);
		}
	}
	
	private static int getIntArg(String arg, String shortArg,
			int defaultArg, String[] args) {
		for (int i = 0; i < args.length; i++) {
			if ((arg != null && args[i].equals(arg)) || args[i].equals(shortArg)) {
				if (i < args.length-1) {
					try {
						int argInt = Integer.parseInt(args[i+1]);
						return argInt;
					} catch (NumberFormatException nfe) {
						return defaultArg;
					}
				}
			}
		}
		return defaultArg;
	}
	
	private static boolean hasArg(String arg, String shortArg,
			boolean defaultArg, String[] args) {
		for (String a : args) {
			if ((arg != null && a.equals(arg)) || a.equals(shortArg)) {
				return true;
			}
		}
		return defaultArg;
	}
}
