package tarcLog;

import java.util.*;
import java.io.*;

public class TARCLog {
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		
		System.out.println("Welcome to TARC Log. Start off with these commands:");
		System.out.println();
		System.out.println("\t0:\tNew Data Sheet");
		System.out.println("\t1:\tEdit Data Sheet");
		System.out.println("\t2:\tDelete Data Sheet");
		System.out.println("\t3:\tAnalyze Data Sheet (Not yet Implemented)"); // WIP
		
		
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
	
	private static int numberChoice(Scanner console, int maxOptionNum) {
		String input = console.nextLine();
		int choice = -1;
		if (checkForNumber(input.trim())) {
			choice = Integer.parseInt(input);
			if (choice > -1 && choice <= maxOptionNum)
				return choice;
		}
		System.out.println("Incorrect input. Please try again.");
		numberChoice(console, maxOptionNum);
		return choice;
	}
	
	private static boolean checkKeyword(String input) {
		// keywords
		// pass, p
		// dq
		// back, b
		// finish, f
		return true;
	}
}
