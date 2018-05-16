package tarcLog;

import java.util.*;
import java.io.*;

public class TARCLog {
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		File inputCacheFile;
		File outputCacheFile;
		File outputDataSheet;
		
		System.out.println("Welcome to TARC Log. Start off with these commands:");
		System.out.println();
		System.out.println("\t0:\tNew Data Sheet");
		System.out.println("\t1:\tEdit Data Sheet");
		System.out.println("\t2:\tDelete Data Sheet");
		System.out.println("\t3:\tAnalyze Data Sheet (Not yet Implemented)"); // WIP
		
		switch(numberChoice(console, 3)) {
			case 0:
				
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
		}
		
		console.close();
	}
	
	private static boolean checkForNumber(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private static boolean checkForInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private static int numberChoice(Scanner console, int maxOptionNum) {
		String input;
		int choice = -1;
		do {
			input = console.nextLine();
			if (checkForInteger(input)) {
				choice = Integer.parseInt(input);
				if (choice >= 0 && choice <= maxOptionNum)
					return choice;
			} else {
				System.out.println("Incorrect input. Please try again.");
			}
		} while (!checkForInteger(input));
		return -1;
	}
	
	private static boolean checkKeyword(String input) {
		// keywords
		// pass, p
		// back, b
		// finish, f
		// save, s
		// help, h
		return true;
	}
	
	private static void inputDataPhase(Scanner console) {
		
	}
}
