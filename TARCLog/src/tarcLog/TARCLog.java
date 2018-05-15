package tarcLog;

import java.util.*;
import java.io.*;

public class TARCLog {
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
	}
	
	public static boolean checkNumber(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean checkKeyword(String input) {
		// keywords
		// pass, p
		// dq
		// back, b
		return true;
	}
}
