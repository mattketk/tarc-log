package tarcLog;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.io.*;

public class TARCLog {
	private static final String DATASHEETS_DIR = "cache/datasheets/";
	private static final String SAVEFILES_DIR = "cache/savefiles/";
	private static final String ERROR_MSG_0 = "Incorrect input. Please try again.";
	private static final String ERROR_MSG_1 = "Invalid keyword. Please try again.";

	public static void main(String[] args) throws FileNotFoundException {
		Scanner console = new Scanner(System.in);
		File inputCacheFile;
		File outputCacheFile;
		File outputDataSheet;

		PrintStream dataSheet;
		PrintStream saveFile;

		DataSheet sheet;
		Flight flight;

		System.out.println("Welcome to TARC Log. Start off with these commands:");
		System.out.println();
		System.out.println("\t0:\tNew Data Sheet");
		System.out.println("\t1:\tEdit Data Sheet");
		System.out.println("\t2:\tDelete Data Sheet");
		System.out.println("\t3:\tAnalyze Data Sheet (Not yet Implemented)"); // WIP

		switch (numberChoice(console, 3)) {
		case 0:
			sheet = new DataSheet();
			flight = new Flight();
			outputCacheFile = new File(SAVEFILES_DIR + "save_" + new SimpleDateFormat("MM.dd.yyyy").format(new Date()));
			saveFile = new PrintStream(outputCacheFile);
			// prompt user for input data
			// save flight in sheet
			// save
			// inputDataPhase(console, flight );
			
			inputDataPhase(console, flight, saveFile);
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
		String input = "";
		int choice = -1;
		while (choice < 0 || choice > maxOptionNum) {
			input = console.nextLine();
			if (checkForInteger(input)) {
				choice = Integer.parseInt(input);
				if (choice < 0 || choice > maxOptionNum) {
					System.out.println(ERROR_MSG_0);
					choice = -1;
				}
			} else {
				System.out.println(ERROR_MSG_0);
			}
		}
		return choice;
	}

	private static boolean yesNoChoice(Scanner console) {
		String input = "";
		boolean isYes = false;
		while (!(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y") || input.equalsIgnoreCase("no")
				|| input.equalsIgnoreCase("n"))) {
			input = console.nextLine();
			if (!(input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y") || input.equalsIgnoreCase("no")
					|| input.equalsIgnoreCase("n"))) {
				System.out.println(ERROR_MSG_0);
			}
		}
		if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y")) {
			isYes = true;
		} else if (input.equalsIgnoreCase("no") || input.equalsIgnoreCase("n")) {
			isYes = false;
		}
		return isYes;
	}

	/*
	 * private static double inputNumberData(Scanner console) { String input = "";
	 * double n = 0.0; while (!checkForNumber(input)) { input = console.nextLine();
	 * if (checkForNumber(input)) n = Double.parseDouble(input); else
	 * System.out.println(ERROR_MSG_0); } return n; }
	 */

	private static boolean checkKeyword(String input) {
		// keywords; '/' is appended before
		// pass, p
		// back, b
		// finish, f
		// save, s
		// help, h
		boolean isKeyword = input.equalsIgnoreCase("/pass") || input.equalsIgnoreCase("/p");
		isKeyword = isKeyword || (input.equalsIgnoreCase("/back") || input.equalsIgnoreCase("/b"));
		isKeyword = isKeyword || (input.equalsIgnoreCase("/finish") || input.equalsIgnoreCase("/f"));
		isKeyword = isKeyword || (input.equalsIgnoreCase("/save") || input.equalsIgnoreCase("/s"));
		isKeyword = isKeyword || (input.equalsIgnoreCase("/help") || input.equalsIgnoreCase("/h"));
		return isKeyword;
	}

	private static int evaluateKeyword(String input) {
		int result = -1;
		if (input.equalsIgnoreCase("/pass") || input.equalsIgnoreCase("/p"))
			result = 0;
		else if (input.equalsIgnoreCase("/back") || input.equalsIgnoreCase("/b"))
			result = 1;
		else if (input.equalsIgnoreCase("/save") || input.equalsIgnoreCase("/s"))
			result = 2;
		else if (input.equalsIgnoreCase("/finish") || input.equalsIgnoreCase("/f"))
			result = 3;
		else if (input.equalsIgnoreCase("/help") || input.equalsIgnoreCase("/h"))
			result = 4;
		return result;
	}

	private static int evaluateInput(String input) {
		int result = -1;
		// result codes
		// 0: is keyword
		// 1: is number
		// 2: is string

		if (checkKeyword(input))
			result = 0;
		else if (checkForNumber(input))
			result = 1;
		else
			result = 2;

		return result;
	}

	private static void keywordPhase_start(Scanner console, Flight f, PrintStream output, int phase, String input) {
		if (evaluateKeyword(input) == 0)
			inputDataPhase(console, f, output, phase + 1);
		else if (evaluateKeyword(input) == 1) {
			System.out.println(ERROR_MSG_1);
			inputDataPhase(console, f, output, phase);
		} else if (evaluateKeyword(input) == 2) {
			System.out.println("Saving flight...");
			f.saveFlight(output);
			System.out.print("Flight saved. ");
			inputDataPhase(console, f, output, phase);
		} else if (evaluateKeyword(input) == 3) {
			System.out.print("Do you want to save?: (yes, y/no, n)");
			if (yesNoChoice(console)) {
				System.out.println("Saving flight...");
				f.saveFlight(output);
				System.out.print("Flight saved. ");
				inputDataPhase(console, f, output, -1);
			}
			System.out.println("Exiting...");
		} else if (evaluateKeyword(input) == 4) {
			// Implement help method
			inputDataPhase(console, f, output, phase);
		}
	}

	private static void keywordPhase(Scanner console, Flight f, PrintStream output, int phase, String input) {
		if (evaluateKeyword(input) == 0)
			inputDataPhase(console, f, output, phase + 1);
		else if (evaluateKeyword(input) == 1) {
			inputDataPhase(console, f, output, phase - 1);
		} else if (evaluateKeyword(input) == 2) {
			f.saveFlight(output);
			inputDataPhase(console, f, output, phase);
		} else if (evaluateKeyword(input) == 3) {
			System.out.print("Do you want to save?: (yes, y/no, n)");
			if (yesNoChoice(console)) {
				System.out.println("Saving flight...");
				f.saveFlight(output);
				System.out.print("Flight saved. ");
				inputDataPhase(console, f, output, -1);
			}
			System.out.println("Exiting...");
		} else if (evaluateKeyword(input) == 4) {
			// Implement help method
			inputDataPhase(console, f, output, phase);
		}
	}

	private static void keywordPhase_end(Scanner console, Flight f, PrintStream output, int phase, String input) {
		if (evaluateKeyword(input) == 0) {
			System.out.println(ERROR_MSG_1);
			inputDataPhase(console, f, output, phase);
		} else if (evaluateKeyword(input) == 1) {
			inputDataPhase(console, f, output, phase - 1);
		} else if (evaluateKeyword(input) == 2) {
			f.saveFlight(output);
			inputDataPhase(console, f, output, phase);
		} else if (evaluateKeyword(input) == 3) {
			System.out.print("Do you want to save?: (yes, y/no, n)");
			if (yesNoChoice(console)) {
				System.out.println("Saving flight...");
				f.saveFlight(output);
				System.out.print("Flight saved. ");
				inputDataPhase(console, f, output, -1);
			}
			System.out.println("Exiting...");
		} else if (evaluateKeyword(input) == 4) {
			// Implement help method
			inputDataPhase(console, f, output, phase);
		}
	}

	private static String inputDataLoopNumber(Scanner console, String prompt) {
		String input = "";
		while (!(evaluateInput(input) == 0 || evaluateInput(input) == 1)) {
			System.out.print(prompt);
			input = console.nextLine();
			// System.out.println(evaluateInput(input));
		}

		return input;
	}

	private static String inputDataLoopString(Scanner console, String prompt) {
		String input = "0.0";
		while (!(evaluateInput(input) == 0 || evaluateInput(input) == 2)) {
			System.out.print(prompt);
			input = console.nextLine();
		}

		return input;
	}

	private static void inputDataPhase(Scanner console, Flight f, PrintStream output, int phase) {
		String input = "";
		String[] commandParams;
		boolean isDone = false;
		switch (phase) {
		case 0:
			input = inputDataLoopNumber(console, "Temperature (F): ");
			if (evaluateInput(input) == 0) {
				keywordPhase_start(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setTemperature(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 1:
			input = inputDataLoopNumber(console, "Wind Speed (MPH): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setWindSpeed(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 2:
			input = inputDataLoopNumber(console, "Humidity (%): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setHumidity(Double.parseDouble(input));
				System.out.println(phase + 1);
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 3:
			System.out.println(phase);
			input = inputDataLoopString(console, "Payload Name: ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 2) {
				f.setPayload(input);
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 4:
			input = inputDataLoopString(console, "Booster Name: ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 2) {
				f.setBooster(input);
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 5:
			input = inputDataLoopString(console, "Motor Name: ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 2) {
				f.setMotor(input);
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 6:
			System.out.println(phase);
			input = inputDataLoopNumber(console, "Motor Delay (s): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setDelay((int) Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 7:
			input = inputDataLoopString(console, "Parachute Name: ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 2) {
				f.setParachute(input);
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 8:
			input = inputDataLoopNumber(console, "Payload Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setPayloadMass(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 9:
			input = inputDataLoopNumber(console, "Booster Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setBoosterMass(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 10:
			input = inputDataLoopNumber(console, "Booster Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setBoosterMass(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 11:
			String[] eggMasses;
			boolean isValid = false;
			while (!isValid) {
				System.out.print("Enter egg masses (separated by space): ");
				eggMasses = console.nextLine().split(" ");
				isValid = true;
				if (eggMasses.length < 1)
					isValid = false;
				else {
					for (int i = 0; i < eggMasses.length; i++) {
						if (!checkForNumber(eggMasses[i].trim())) {
							isValid = false;
						}
					}
				}
				if (isValid) {
					f.setEggAmount(eggMasses.length);
					for (int i = 0; i < eggMasses.length; i++) {
						f.setEggMass(i, Double.parseDouble(eggMasses[i]));
					}
				} else {
					System.out.println(ERROR_MSG_0);
				}
			}
			break;
		case 12:
			input = inputDataLoopNumber(console, "Parachute Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setParachuteMass(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 13:
			input = inputDataLoopNumber(console, "Nomex Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setNomexMass(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 14:
			input = inputDataLoopNumber(console, "Insulation Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setInsulationMass(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 15:
			input = inputDataLoopNumber(console, "Casing Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setCasingMass(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 16:
			input = inputDataLoopNumber(console, "Motor Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setMotorMass(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 17:
			input = inputDataLoopNumber(console, "Altitude (ft): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setAltitude((int) Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 18:
			input = inputDataLoopNumber(console, "Time (s): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				f.setTime(Double.parseDouble(input));
				inputDataPhase(console, f, output, phase + 1);
			}
			break;
		case 19:
			// keywords:
			// /del, /d
			System.out.println("Enter Modification notes: ");
			System.out.println("NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");
			
			while (!isDone) {
				input = console.nextLine().trim();
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (input.equalsIgnoreCase("/view") || input.equalsIgnoreCase("/v")) {
					if (f.getNumModifications() > 0) {
						for (int i = 0; i < f.getNumModifications(); i++) {
							System.out.println(i + ": " + f.getModification(i));
						}
					} else {
						System.out.println(ERROR_MSG_1);
					}
				} else if (input.equalsIgnoreCase("/del") || input.equalsIgnoreCase("/d")) {
					commandParams = input.split(" ");
					if (commandParams.length > 1) {
						if (checkForInteger(commandParams[1])) {
							if (Integer.parseInt(commandParams[1]) < f.getNumModifications()) {
								f.removeModification(Integer.parseInt(commandParams[1]));
							} else {
								 System.out.println(ERROR_MSG_0);
							}
						} else {
							System.out.println(ERROR_MSG_0);
						}
					} else {
						System.out.println(ERROR_MSG_0);
					}
				} else if (input.equalsIgnoreCase("/end") || input.equalsIgnoreCase("/e")) {
					inputDataPhase(console, f, output, phase + 1);
				} else {
					f.addModification(input);
				}
			}
			break;
		case 20:
			System.out.println("Enter Damage notes: ");
			System.out.println("NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");
			
			while (!isDone) {
				input = console.nextLine().trim();
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (input.equalsIgnoreCase("/view") || input.equalsIgnoreCase("/v")) {
					if (f.getNumDamages() > 0) {
						for (int i = 0; i < f.getNumDamages(); i++) {
							System.out.println(i + ": " + f.getDamage(i));
						}
					} else {
						System.out.println(ERROR_MSG_1);
					}
				} else if (input.equalsIgnoreCase("/del") || input.equalsIgnoreCase("/d")) {
					commandParams = input.split(" ");
					if (commandParams.length > 1) {
						if (checkForInteger(commandParams[1])) {
							if (Integer.parseInt(commandParams[1]) < f.getNumDamages()) {
								f.removeDamage(Integer.parseInt(commandParams[1]));
							} else {
								 System.out.println(ERROR_MSG_0);
							}
						} else {
							System.out.println(ERROR_MSG_0);
						}
					} else {
						System.out.println(ERROR_MSG_0);
					}
				} else if (input.equalsIgnoreCase("/end") || input.equalsIgnoreCase("/e")) {
					inputDataPhase(console, f, output, phase + 1);
				} else {
					f.addDamage(input);
				}
			}
			break;
		case 21:
			System.out.println("Enter Characteristic notes: ");
			System.out.println("NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");
			
			while (!isDone) {
				input = console.nextLine().trim();
			if (evaluateInput(input) == 0) {
				keywordPhase(console, f, output, phase, input);
			} else if (input.equalsIgnoreCase("/view") || input.equalsIgnoreCase("/v")) {
					if (f.getNumCharacteristics() > 0) {
						for (int i = 0; i < f.getNumCharacteristics(); i++) {
							System.out.println(i + ": " + f.getCharacteristic(i));
						}
					} else {
						System.out.println(ERROR_MSG_1);
					}
				} else if (input.equalsIgnoreCase("/del") || input.equalsIgnoreCase("/d")) {
					commandParams = input.split(" ");
					if (commandParams.length > 1) {
						if (checkForInteger(commandParams[1])) {
							if (Integer.parseInt(commandParams[1]) < f.getNumCharacteristics()) {
								f.removeCharacteristic(Integer.parseInt(commandParams[1]));
							} else {
								 System.out.println(ERROR_MSG_0);
							}
						} else {
							System.out.println(ERROR_MSG_0);
						}
					} else {
						System.out.println(ERROR_MSG_0);
					}
				} else if (input.equalsIgnoreCase("/end") || input.equalsIgnoreCase("/e")) {
					inputDataPhase(console, f, output, phase + 1);
				} else {
					f.addCharacteristic(input);
				}
			}
			break;
		case 22:
			System.out.println("Enter Consideration notes: ");
			System.out.println("NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");
			
			while (!isDone) {
				input = console.nextLine().trim();
			if (evaluateInput(input) == 0) {
				keywordPhase_end(console, f, output, phase, input);
			} else if (input.equalsIgnoreCase("/view") || input.equalsIgnoreCase("/v")) {
					if (f.getNumConsiderations() > 0) {
						for (int i = 0; i < f.getNumConsiderations(); i++) {
							System.out.println(i + ": " + f.getConsideration(i));
						}
					} else {
						System.out.println(ERROR_MSG_1);
					}
				} else if (input.equalsIgnoreCase("/del") || input.equalsIgnoreCase("/d")) {
					commandParams = input.split(" ");
					if (commandParams.length > 1) {
						if (checkForInteger(commandParams[1])) {
							if (Integer.parseInt(commandParams[1]) < f.getNumConsiderations()) {
								f.removeConsideration(Integer.parseInt(commandParams[1]));
							} else {
								 System.out.println(ERROR_MSG_0);
							}
						} else {
							System.out.println(ERROR_MSG_0);
						}
					} else {
						System.out.println(ERROR_MSG_0);
					}
				} else if (input.equalsIgnoreCase("/end") || input.equalsIgnoreCase("/e")) {
					inputDataPhase(console, f, output, phase + 1);
				} else {
					f.addConsideration(input);
				}
			}
			keywordPhase_end(console, f, output, phase, "/finish");
			break;
		default:
			break;
		}
	}
	
	public static void inputDataPhase(Scanner console, Flight f, PrintStream output) {
		inputDataPhase(console, f, output, 0);
	}
}
