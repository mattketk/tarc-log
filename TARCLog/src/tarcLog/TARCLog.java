package tarcLog;

import java.util.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.*;

public class TARCLog {
	private static final String DATASHEETS_DIR = "cache/datasheets/";
	private static final String SAVEFILES_DIR = "cache/savefiles/";
	private static final String ERROR_MSG_0 = "Incorrect input. Please try again.";
	private static final String ERROR_MSG_1 = "Invalid keyword. Please try again.";

	public static void main(String[] args) throws FileNotFoundException, ParseException {
		Scanner console = new Scanner(System.in);
		File outputCacheFile;
		File outputDataSheet;

		PrintStream dataSheet;
		PrintStream saveFile;

		DataSheet sheet;
		Flight flight;

		String[] savedFlights;
		DataSheet[] loadedDataSheets;

		boolean hasQuit = false;
		boolean hasQuitDataSheet = false;

		while (!hasQuit) {
			System.out.println("Loading flight data...");

			System.out.println("Welcome to TARC Log. Start off with these commands:");
			System.out.println();
			System.out.println("\t0:\tNew Data Sheet");
			System.out.println("\t1:\tEdit Data Sheet");
			System.out.println("\t2:\tPrint Data Sheet");
			System.out.println("\t3:\tDelete Data Sheet");
			System.out.println("\t4:\tAnalyze Data Sheet (Not yet Implemented)"); // WIP
			System.out.println("\t5:\tExit");

			switch (numberChoice(console, 5)) {
			case 0:
				// make new sheet
				// start new flight data input prompt
				// when finished, ask to start new input prompt
				// if no, go back
				sheet = new DataSheet();
				outputCacheFile = new File(
						SAVEFILES_DIR + "save_" + new SimpleDateFormat("MM.dd.yyyy_HH.mm.ss").format(sheet.getDate()));
				saveFile = new PrintStream(outputCacheFile);
				while (!hasQuitDataSheet) {
					sheet.addFlight(new Flight());
					inputDataPhase(console, sheet, sheet.getFlightAmount() - 1, saveFile);
					System.out.print("Enter another flight? (yes, y/no, n): ");
					if (!yesNoChoice(console)) {
						hasQuitDataSheet = true;
					}
				}
				sheet.saveSheet(saveFile);
				break;
			case 1:
				int counter = 0;
				savedFlights = new File(SAVEFILES_DIR).list();
				if (savedFlights.length > 0) {
					loadedDataSheets = new DataSheet[savedFlights.length];
					for (int i = 0; i < savedFlights.length; i++) {
						loadedDataSheets[i] = new DataSheet();
						loadedDataSheets[i].loadSheet(new Scanner(new File(SAVEFILES_DIR + savedFlights[i])));
						
					}
					System.out.println("Type a number corresponding to a flight to start editing it:\n");
					for (int i = 0; i < loadedDataSheets.length; i++) {
						System.out.println(
								"Date: " + new SimpleDateFormat("MM/dd/yyyy").format(loadedDataSheets[i].getDate()));
						for (int j = 0; j < loadedDataSheets[i].getFlightAmount(); j++) {
							System.out.print(counter + ": ");
							if (loadedDataSheets[i].getFlight(j).isComplete()) {
								System.out.println("COMPLETE");
							} else {
								System.out.println("INCOMPLETE");
							}
							counter++;
						}
					}
					int choice = numberChoice(console, counter);

					int i = 0;
					while (loadedDataSheets[i].getFlightAmount() < choice) {
						choice -= loadedDataSheets[i].getFlightAmount();
						i++;
					}
					outputCacheFile = new File(SAVEFILES_DIR + "save_"
							+ new SimpleDateFormat("MM.dd.yyyy_HH.mm.ss").format(loadedDataSheets[i].getDate()));
					saveFile = new PrintStream(outputCacheFile);
					inputDataPhase(console, loadedDataSheets[i], choice - 1, saveFile);
				} else {
					System.out.println("No saves found. Returning to main menu...");
				}
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				System.out.println("Exiting...");
				hasQuit = true;
				break;
			}
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

	private static void keywordPhase_start(Scanner console, DataSheet s, int flightNum, PrintStream output, int phase,
			String input) {
		if (evaluateKeyword(input) == 0)
			inputDataPhase(console, s, flightNum, output, phase + 1);
		else if (evaluateKeyword(input) == 1) {
			System.out.println(ERROR_MSG_1);
			inputDataPhase(console, s, flightNum, output, phase);
		} else if (evaluateKeyword(input) == 2) {
			System.out.println("Saving sheet...");
			s.saveSheet(output);
			System.out.print("Sheet saved. ");
			inputDataPhase(console, s, flightNum, output, phase);
		} else if (evaluateKeyword(input) == 3) {
			System.out.print("Do you want to save?: (yes, y/no, n)");
			if (yesNoChoice(console)) {
				System.out.println("Saving sheet...");
				s.saveSheet(output);
				System.out.print("Sheet saved. ");
				inputDataPhase(console, s, flightNum, output, -1);
			}
			output.close();
			System.out.println("Exiting...");
		} else if (evaluateKeyword(input) == 4) {
			// Implement help method
			inputDataPhase(console, s, flightNum, output, phase);
		}
	}

	private static void keywordPhase(Scanner console, DataSheet s, int flightNum, PrintStream output, int phase,
			String input) {
		if (evaluateKeyword(input) == 0)
			inputDataPhase(console, s, flightNum, output, phase + 1);
		else if (evaluateKeyword(input) == 1) {
			inputDataPhase(console, s, flightNum, output, phase - 1);
		} else if (evaluateKeyword(input) == 2) {
			System.out.println("Saving sheet...");
			s.saveSheet(output);
			System.out.print("Sheet saved. ");
			inputDataPhase(console, s, flightNum, output, phase);
		} else if (evaluateKeyword(input) == 3) {
			System.out.print("Do you want to save?: (yes, y/no, n)");
			if (yesNoChoice(console)) {
				System.out.println("Saving sheet...");
				s.saveSheet(output);
				System.out.print("Sheet saved. ");
				inputDataPhase(console, s, flightNum, output, -1);
			}
			output.close();
			System.out.println("Exiting...");
		} else if (evaluateKeyword(input) == 4) {
			// Implement help method
			inputDataPhase(console, s, flightNum, output, phase);
		}
	}

	private static void keywordPhase_end(Scanner console, DataSheet s, int flightNum, PrintStream output, int phase,
			String input) {
		if (evaluateKeyword(input) == 0) {
			System.out.println(ERROR_MSG_1);
			inputDataPhase(console, s, flightNum, output, phase);
		} else if (evaluateKeyword(input) == 1) {
			inputDataPhase(console, s, flightNum, output, phase - 1);
		} else if (evaluateKeyword(input) == 2) {
			System.out.println("Saving sheet...");
			s.saveSheet(output);
			System.out.print("Sheet saved. ");
			inputDataPhase(console, s, flightNum, output, phase);
		} else if (evaluateKeyword(input) == 3) {
			System.out.print("Do you want to save?: (yes, y/no, n)");
			if (yesNoChoice(console)) {
				System.out.println("Saving sheet...");
				s.saveSheet(output);
				System.out.print("Sheet saved. ");
				inputDataPhase(console, s, flightNum, output, -1);
			}
			output.close();
			System.out.println("Exiting...");
		} else if (evaluateKeyword(input) == 4) {
			// Implement help method
			inputDataPhase(console, s, flightNum, output, phase);
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

	private static void inputDataPhase(Scanner console, DataSheet s, int flightNum, PrintStream output, int phase) {
		String input = "";
		String[] commandParams;
		boolean isDone = false;
		switch (phase) {
		case 0:

			input = inputDataLoopNumber(console, "Temperature (F): ");
			if (evaluateInput(input) == 0) {
				keywordPhase_start(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setTemperature(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 1:
			input = inputDataLoopNumber(console, "Wind Speed (MPH): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setWindSpeed(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 2:
			input = inputDataLoopNumber(console, "Humidity (%): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setHumidity(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 3:
			input = inputDataLoopString(console, "Payload Name: ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 2) {
				s.getFlight(flightNum).setPayload(input);
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 4:
			input = inputDataLoopString(console, "Booster Name: ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 2) {
				s.getFlight(flightNum).setBooster(input);
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 5:
			input = inputDataLoopString(console, "Motor Name: ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 2) {
				s.getFlight(flightNum).setMotor(input);
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 6:
			input = inputDataLoopNumber(console, "Motor Delay (s): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setDelay((int) Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 7:
			input = inputDataLoopString(console, "Parachute Name: ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 2) {
				s.getFlight(flightNum).setParachute(input);
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 8:
			input = inputDataLoopNumber(console, "Payload Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setPayloadMass(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 9:
			input = inputDataLoopNumber(console, "Booster Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setBoosterMass(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 10:
			String[] eggMasses;
			boolean isValid = false;
			while (!isValid) {
				System.out.print("Enter egg masses (separated by space): ");
				eggMasses = console.nextLine().split(" ");
				isValid = true;
				if (eggMasses.length > 0 && evaluateInput(eggMasses[0]) == 0) {
					keywordPhase(console, s, flightNum, output, phase, eggMasses[0]);
				} else {
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
						s.getFlight(flightNum).setEggAmount(eggMasses.length);
						for (int i = 0; i < eggMasses.length; i++) {
							s.getFlight(flightNum).setEggMass(i, Double.parseDouble(eggMasses[i]));
						}
						inputDataPhase(console, s, flightNum, output, phase + 1);
					} else {

						System.out.println(ERROR_MSG_0);
					}
				}
			}
			break;
		case 11:
			input = inputDataLoopNumber(console, "Parachute Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setParachuteMass(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 12:
			input = inputDataLoopNumber(console, "Nomex Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setNomexMass(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 13:
			input = inputDataLoopNumber(console, "Insulation Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setInsulationMass(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 14:
			input = inputDataLoopNumber(console, "Casing Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setCasingMass(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 15:
			input = inputDataLoopNumber(console, "Motor Mass (g): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setMotorMass(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 16:
			input = inputDataLoopNumber(console, "Altitude (ft): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setAltitude((int) Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 17:
			input = inputDataLoopNumber(console, "Time (s): ");
			if (evaluateInput(input) == 0) {
				keywordPhase(console, s, flightNum, output, phase, input);
			} else if (evaluateInput(input) == 1) {
				s.getFlight(flightNum).setTime(Double.parseDouble(input));
				inputDataPhase(console, s, flightNum, output, phase + 1);
			}
			break;
		case 18:
			// keywords:
			// /del, /d
			System.out.println("Enter Modification notes: ");
			System.out.println(
					"NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");

			while (!isDone) {
				input = console.nextLine().trim();
				if (evaluateInput(input) == 0) {
					keywordPhase(console, s, flightNum, output, phase, input);
				} else if (input.equalsIgnoreCase("/view") || input.equalsIgnoreCase("/v")) {
					if (s.getFlight(flightNum).getNumModifications() > 0) {
						for (int i = 0; i < s.getFlight(flightNum).getNumModifications(); i++) {
							System.out.println(i + ": " + s.getFlight(flightNum).getModification(i));
						}
					} else {
						System.out.println(ERROR_MSG_1);
					}
				} else if (input.equalsIgnoreCase("/del") || input.equalsIgnoreCase("/d")) {
					commandParams = input.split(" ");
					if (commandParams.length > 1) {
						if (checkForInteger(commandParams[1])) {
							if (Integer.parseInt(commandParams[1]) < s.getFlight(flightNum).getNumModifications()) {
								s.getFlight(flightNum).removeModification(Integer.parseInt(commandParams[1]));
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
					inputDataPhase(console, s, flightNum, output, phase + 1);
				} else {
					s.getFlight(flightNum).addModification(input);
				}
			}
			break;
		case 19:
			System.out.println("Enter Damage notes: ");
			System.out.println(
					"NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");

			while (!isDone) {
				input = console.nextLine().trim();
				if (evaluateInput(input) == 0) {
					keywordPhase(console, s, flightNum, output, phase, input);
				} else if (input.equalsIgnoreCase("/view") || input.equalsIgnoreCase("/v")) {
					if (s.getFlight(flightNum).getNumDamages() > 0) {
						for (int i = 0; i < s.getFlight(flightNum).getNumDamages(); i++) {
							System.out.println(i + ": " + s.getFlight(flightNum).getDamage(i));
						}
					} else {
						System.out.println(ERROR_MSG_1);
					}
				} else if (input.equalsIgnoreCase("/del") || input.equalsIgnoreCase("/d")) {
					commandParams = input.split(" ");
					if (commandParams.length > 1) {
						if (checkForInteger(commandParams[1])) {
							if (Integer.parseInt(commandParams[1]) < s.getFlight(flightNum).getNumDamages()) {
								s.getFlight(flightNum).removeDamage(Integer.parseInt(commandParams[1]));
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
					inputDataPhase(console, s, flightNum, output, phase + 1);
				} else {
					s.getFlight(flightNum).addDamage(input);
				}
			}
			break;
		case 20:
			System.out.println("Enter Characteristic notes: ");
			System.out.println(
					"NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");

			while (!isDone) {
				input = console.nextLine().trim();
				if (evaluateInput(input) == 0) {
					keywordPhase(console, s, flightNum, output, phase, input);
				} else if (input.equalsIgnoreCase("/view") || input.equalsIgnoreCase("/v")) {
					if (s.getFlight(flightNum).getNumCharacteristics() > 0) {
						for (int i = 0; i < s.getFlight(flightNum).getNumCharacteristics(); i++) {
							System.out.println(i + ": " + s.getFlight(flightNum).getCharacteristic(i));
						}
					} else {
						System.out.println(ERROR_MSG_1);
					}
				} else if (input.equalsIgnoreCase("/del") || input.equalsIgnoreCase("/d")) {
					commandParams = input.split(" ");
					if (commandParams.length > 1) {
						if (checkForInteger(commandParams[1])) {
							if (Integer.parseInt(commandParams[1]) < s.getFlight(flightNum).getNumCharacteristics()) {
								s.getFlight(flightNum).removeCharacteristic(Integer.parseInt(commandParams[1]));
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
					inputDataPhase(console, s, flightNum, output, phase + 1);
				} else {
					s.getFlight(flightNum).addCharacteristic(input);
				}
			}
			break;
		case 21:
			System.out.println("Enter Consideration notes: ");
			System.out.println(
					"NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");

			while (!isDone) {
				input = console.nextLine().trim();
				if (evaluateInput(input) == 0) {
					keywordPhase_end(console, s, flightNum, output, phase, input);
				} else if (input.equalsIgnoreCase("/view") || input.equalsIgnoreCase("/v")) {
					if (s.getFlight(flightNum).getNumConsiderations() > 0) {
						for (int i = 0; i < s.getFlight(flightNum).getNumConsiderations(); i++) {
							System.out.println(i + ": " + s.getFlight(flightNum).getConsideration(i));
						}
					} else {
						System.out.println(ERROR_MSG_1);
					}
				} else if (input.equalsIgnoreCase("/del") || input.equalsIgnoreCase("/d")) {
					commandParams = input.split(" ");
					if (commandParams.length > 1) {
						if (checkForInteger(commandParams[1])) {
							if (Integer.parseInt(commandParams[1]) < s.getFlight(flightNum).getNumConsiderations()) {
								s.getFlight(flightNum).removeConsideration(Integer.parseInt(commandParams[1]));
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
					inputDataPhase(console, s, flightNum, output, phase + 1);
				} else {
					s.getFlight(flightNum).addConsideration(input);
				}
			}
			keywordPhase_end(console, s, flightNum, output, phase, "/finish");
			break;
		default:
			break;
		}
	}

	private static void inputDataPhase(Scanner console, DataSheet s, int flightNum, PrintStream output) {
		inputDataPhase(console, s, flightNum, output, 0);
	}
}
