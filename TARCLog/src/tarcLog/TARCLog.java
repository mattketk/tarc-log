package tarcLog;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class TARCLog {
	private static final String DATASHEETS_DIR = "cache/datasheets/";
	private static final String SAVEFILES_DIR = "cache/savefiles/";
	private static final String DATE_FORMAT = "MM.dd.yyyy_HHmmss";
	private static final String ERROR_MSG_0 = "Incorrect input. Please try again.";
	private static final String ERROR_MSG_1 = "Invalid keyword. Please try again.";
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Scanner console = new Scanner(System.in);
		FileInputStream saveFileIn;
		ObjectInputStream saveFileObject;
		PrintStream dataSheetPrint;
		
		String[] saveFilesList;
		DataSheet[] loadedDataSheets;
		
		boolean hasQuit = false;
		boolean hasQuitDataInput = false;
		int counter;
		
		while (!hasQuit) {
			System.out.println("Welcome to TARC Log. Start off with these commands:");
			System.out.println();
			System.out.println("\t0:\tNew Data Sheet");
			System.out.println("\t1:\tEdit Data Sheet");
			System.out.println("\t2:\tPrint Data Sheet");
			System.out.println("\t3:\tDelete Data Sheet");
			System.out.println("\t4:\tAnalyze Data Sheet (Not yet Implemented)"); // WIP
			System.out.println("\t5:\tExit");
			
			switch(inputInteger(console, 5)) {
				case 0:
					// create new DataSheet, new empty serial file
					// add one flight to DataSheet
					// if inputDataPhase finishes and flight is not saved
					// DataSheet and empty serial file deletes
					// 
					DataSheet dataSheet = new DataSheet();
					String fileName = SAVEFILES_DIR + "savefile_" + new SimpleDateFormat(DATE_FORMAT).format(dataSheet.getDate());
					while (!hasQuitDataInput) {
						dataSheet.addFlight(new Flight());
						inputDataPhase(console, dataSheet, fileName, dataSheet.getFlightAmount() - 1, false);
						System.out.println("Do you want to add another flight? (yes, y/no, n): ");
						if (!yesNoChoice(console)) {
							hasQuitDataInput = true;
						}
					}
					if (dataSheet.getFlightAmount() < 1) {
						(new File(fileName)).delete();
					}
					hasQuitDataInput = false;
					break;
				case 1:
					counter = 0;
					saveFilesList = (new File(SAVEFILES_DIR)).list();
					if (saveFilesList.length > 0) {
						loadedDataSheets = new DataSheet[saveFilesList.length];
						for (int i = 0; i < saveFilesList.length; i++) {
							saveFileIn = new FileInputStream(SAVEFILES_DIR + saveFilesList[i]);
							saveFileObject = new ObjectInputStream(saveFileIn);
							loadedDataSheets[i] = (DataSheet) saveFileObject.readObject();
							saveFileObject.close();
							saveFileIn.close();
						}
						System.out.println();
						for (int i = 0; i < loadedDataSheets.length; i++) {
							System.out.println(
									"Date: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(loadedDataSheets[i].getDate()));
							for (int j = 0; j < loadedDataSheets[i].getFlightAmount(); j++) {
								System.out.print(counter + ": ");
								if (loadedDataSheets[i].getFlight(j).isComplete()) {
									System.out.println("COMPLETE");
								} else {
									System.out.println("INCOMPLETE");
								}
								counter++;
							}
							System.out.println();
						}
						System.out.println(counter);
						String userInput;
						boolean hasChosen = false;
						
						
						while (!hasChosen) {
							userInput = inputNumber(console, "Type a number corresponding to a flight to start editing it (/p to exit): ");
							switch (evaluateKeyword(userInput)) {
								case -1: 
									int choice = (int) Double.parseDouble(userInput);
									if (choice >= 0 && choice < counter) {
										hasChosen = true;
										int i = 0;
										while (choice >= loadedDataSheets[i].getFlightAmount()) {
											choice -= loadedDataSheets[i].getFlightAmount();
											i++;
										}
										inputDataPhase(console, loadedDataSheets[i], (SAVEFILES_DIR + saveFilesList[i]), 
												loadedDataSheets[i].getFlightAmount() - (1 + choice), true);
									} else {
										System.out.println(ERROR_MSG_0);
									}
									break;
								case 0:
									hasChosen = true;
									break;
								default:
									System.out.println(ERROR_MSG_0);
									break;
							}
						}
					} else {
						System.out.println("No saves found. Returning to main menu...");
					}
					break;
				case 2:
					saveFilesList = (new File(SAVEFILES_DIR)).list();
					if (saveFilesList.length > 0) {
						loadedDataSheets = new DataSheet[saveFilesList.length];
						for (int i = 0; i < saveFilesList.length; i++) {
							saveFileIn = new FileInputStream(SAVEFILES_DIR + saveFilesList[i]);
							saveFileObject = new ObjectInputStream(saveFileIn);
							loadedDataSheets[i] = (DataSheet) saveFileObject.readObject();
							saveFileObject.close();
							saveFileIn.close();
						}
						System.out.println();
						for (int i = 0; i < loadedDataSheets.length; i++) {
							System.out.println(
									i + ":\tDate: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(loadedDataSheets[i].getDate()));
						}
						String userInput;
						boolean hasChosen = false;
						
		
						while (!hasChosen) {
							userInput = inputNumber(console, "Type a number corresponding to a data sheet to print it (/p to exit): ");
							switch (evaluateKeyword(userInput)) {
								case -1: 
									int choice = (int) Double.parseDouble(userInput);
									if (choice >= 0 && choice < loadedDataSheets.length) {
										String sheetName = DATASHEETS_DIR + "datasheet_" + 
												new SimpleDateFormat("MM.dd.yyyy_HH.mm.ss").format(loadedDataSheets[choice].getDate());
										dataSheetPrint = new PrintStream(new File(sheetName));
										System.out.println("Printing sheet...");
										loadedDataSheets[choice].printSheet(dataSheetPrint);
										hasChosen = true;
									} else {
										System.out.println(ERROR_MSG_0);
									}
									break;
								case 0:
									hasChosen = true;
									break;
								default:
									System.out.println(ERROR_MSG_0);
									break;
							}
						}
					} else {
						System.out.println("No saves found. Returning to main menu...");
					}
					break;
				case 3:
					counter = 0;
					saveFilesList = (new File(SAVEFILES_DIR)).list();
					if (saveFilesList.length > 0) {
						loadedDataSheets = new DataSheet[saveFilesList.length];
						for (int i = 0; i < saveFilesList.length; i++) {
							saveFileIn = new FileInputStream(SAVEFILES_DIR + saveFilesList[i]);
							saveFileObject = new ObjectInputStream(saveFileIn);
							loadedDataSheets[i] = (DataSheet) saveFileObject.readObject();
							saveFileObject.close();
							saveFileIn.close();
						}
						System.out.println();
						for (int i = 0; i < loadedDataSheets.length; i++) {
							System.out.println(
									"Date: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(loadedDataSheets[i].getDate()));
							for (int j = 0; j < loadedDataSheets[i].getFlightAmount(); j++) {
								System.out.print(counter + ": ");
								if (loadedDataSheets[i].getFlight(j).isComplete()) {
									System.out.println("COMPLETE");
								} else {
									System.out.println("INCOMPLETE");
								}
								counter++;
							}
							System.out.println();
						}
						String userInput;
						boolean hasChosen = false;
						
						int i = 0;
						while (!hasChosen) {
							userInput = inputNumber(console, "Type a number corresponding to a flight to delete  (/p to exit): ");
							switch (evaluateKeyword(userInput)) {
								case -1: 
									int choice = (int) Double.parseDouble(userInput);
									if (!(choice < 0 || choice >= counter)) {
										hasChosen = true;
										while (loadedDataSheets[i].getFlightAmount() < choice) {
											choice -= loadedDataSheets[i].getFlightAmount();
											i++;
										}
										loadedDataSheets[i].removeFlight(loadedDataSheets[i].getFlightAmount() - (1 + choice));
										saveSheet(loadedDataSheets[i], (SAVEFILES_DIR + saveFilesList[i]));
										if (loadedDataSheets[i].getFlightAmount() < 1) {
											(new File(SAVEFILES_DIR + saveFilesList[i])).delete();
										}
									}
									break;
								case 0:
									hasChosen = true;
									break;
								default:
									System.out.println(ERROR_MSG_0);
									break;
							}
						}
					} else {
						System.out.println("No saves found. Returning to main menu...");
					}
					break;
				case 4:
					break;
				case 5:
					System.out.print("Are you sure you want to exit? (yes, y/no, n): ");
					if (yesNoChoice(console)) {
						System.out.println("Exiting...");
						hasQuit = true;
					}
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

		if (evaluateKeyword(input) != -1)
			result = 0;
		else if (checkForNumber(input))
			result = 1;
		else
			result = 2;

		return result;
	}
	
	private static int inputInteger(Scanner console, int maxOptionNum) {
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
	
	private static String inputNumber(Scanner console, String prompt) {
		String input = "";
		while (!(evaluateInput(input) == 0 || evaluateInput(input) == 1)) {
			System.out.print(prompt);
			input = console.nextLine();
			// System.out.println(evaluateInput(input));
		}

		return input;
	}

	private static String inputString(Scanner console, String prompt) {
		String input = "0.0";
		while (!(evaluateInput(input) == 0 || evaluateInput(input) == 2)) {
			System.out.print(prompt);
			input = console.nextLine();
		}

		return input;
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
	
	private static void saveSheet(DataSheet sheet, String fileName) throws IOException {
		FileOutputStream saveFile = new FileOutputStream(fileName);
		ObjectOutputStream fileStream = new ObjectOutputStream(saveFile);
		System.out.println("Saving sheet...");
		try {
			fileStream.writeObject(sheet);
			System.out.println("Sheet saved.");
		} catch (Exception e) {
			System.out.println("Sheet save failed!");
			System.out.println("Error: " + e);
		}
		fileStream.close();
		saveFile.close();
	}
	
	private static int keywordPhase(Scanner console, DataSheet sheet, String fileName, int flightNum, int phase, String userInput) throws IOException {
		switch (evaluateKeyword(userInput)) {
			case 0:
				if (phase < 21) {
					phase++;
				} else {
					System.out.println(ERROR_MSG_1);
				}
				break;
			case 1:
				if (phase > 0) {
					phase--;
				} else {
					System.out.println(ERROR_MSG_1);
				}
				break;
			case 2:
				saveSheet(sheet, fileName);
				break;
			case 3:
				
				phase = -1;
				break;
			case 4:
				// TODO: implement help
				String ind = "\t";
				System.out.println();
				System.out.println("Keywords:");
				System.out.println(ind + "/pass, /p:\tSkips the current field to the next one.");
				System.out.println(ind + "/back, /b:\tGoes back to the previous field.");
				System.out.println(ind + "/save, /s:\tSaves the flight onto the data sheet.");
				System.out.println(ind + "/finish, /f:\tSkips the whole flight input and asks to save or not.");
				System.out.println(ind + "/help, /h:\tBrings up this dialog.");
				System.out.println();
				break;
		}
		return phase;
	}
	
	private static void inputDataPhase(Scanner console, DataSheet sheet, String fileName, int flightNum, boolean showPrevData) throws IOException {
		String userInput;
		String[] commandParams;
		boolean isDone = false;
		boolean hasSaved = false;
		int phase = 0;
		Flight flight = sheet.getFlight(flightNum);
		while (phase >= -1 && phase <= 22) {
			switch (phase) {
				case 0:
					if (showPrevData)
						System.out.println("Current Temperature (F): " + flight.getTemperature());
					userInput = inputNumber(console, "Temperature (F): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setTemperature(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 1:
					if (showPrevData)
						System.out.println("Current Wind Speed (MPH): " + flight.getWindSpeed());
					userInput = inputNumber(console, "Wind Speed (MPH): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setWindSpeed(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 2:
					if (showPrevData)
						System.out.println("Current Humidity (%): " + flight.getHumidity());
					userInput = inputNumber(console, "Humidity (%): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setHumidity(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 3:
					if (showPrevData)
						System.out.println("Current Payload Name: " + flight.getPayload());
					userInput = inputString(console, "Payload Name: ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 2) {
						flight.setPayload(userInput);
						phase++;
					}
					System.out.println();
					break;
				case 4:
					if (showPrevData)
						System.out.println("Current Booster Name: " + flight.getBooster());
					userInput = inputString(console, "Booster Name: ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 2) {
						flight.setBooster(userInput);
						phase++;
					}
					System.out.println();
					break;
				case 5:
					if (showPrevData)
						System.out.println("Current Motor Name: " + flight.getMotor());
					userInput = inputString(console, "Motor Name: ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 2) {
						flight.setMotor(userInput);
						phase++;
					}
					System.out.println();
					break;
				case 6:
					if (showPrevData)
						System.out.println("Current Motor Delay (s): " + flight.getDelay());
					userInput = inputNumber(console, "Motor Delay (s): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setDelay((int) Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 7:
					if (showPrevData)
						System.out.println("Current Parachute Name: " + flight.getParachute());
					userInput = inputString(console, "Parachute Name: ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 2) {
						flight.setParachute(userInput);
						phase++;
					}
					System.out.println();
					break;
				case 8:
					if (showPrevData)
						System.out.println("Current Payload Mass (g): " + flight.getPayloadMass());
					userInput = inputNumber(console, "Payload Mass (g): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setPayloadMass(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 9:
					if (showPrevData)
						System.out.println("Current Booster Mass (g): " + flight.getBoosterMass());
					userInput = inputNumber(console, "Booster Mass (g): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setBoosterMass(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 10:
					while (!isDone) {
						String[] eggMasses;
						if (showPrevData) {
							System.out.print("Current Egg Masses (g): ");
							for (int i = 0; i < flight.getNumEggs(); i++) {
								System.out.print(flight.getEggMass(i) + " ");
							}
							System.out.println();
						}
						System.out.print("Enter egg masses (g; separated by space): ");
						eggMasses = console.nextLine().split(" ");
						isDone = true;
						if (eggMasses.length > 0 && evaluateInput(eggMasses[0]) == 0) {
							hasSaved = hasSaved || (evaluateKeyword(eggMasses[0]) == 2);
							phase = keywordPhase(console, sheet, fileName, flightNum, phase, eggMasses[0]);
						} else {
							if (eggMasses.length < 1)
								isDone = false;
							else {

								for (int i = 0; i < eggMasses.length; i++) {
									if (!checkForNumber(eggMasses[i].trim())) {
										isDone = false;
									}
								}
							}
							if (isDone) {
								flight.setEggAmount(eggMasses.length);
								for (int i = 0; i < eggMasses.length; i++) {
									flight.setEggMass(i, Double.parseDouble(eggMasses[i]));
								}
								phase++;
							} else {

								System.out.println(ERROR_MSG_0);
							}
						}
					}
					isDone = false;
					System.out.println();
					break;
				case 11:
					if (showPrevData)
						System.out.println("Current Parachute Mass (g): " + flight.getParachuteMass());
					userInput = inputNumber(console, "Parachute Mass (g): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setParachuteMass(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 12:
					if (showPrevData)
						System.out.println("Current Nomex Mass (g): " + flight.getNomexMass());
					userInput = inputNumber(console, "Nomex Mass (g): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setNomexMass(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 13:
					if (showPrevData)
						System.out.println("Current Insulation Mass (g): " + flight.getInsulationMass());
					userInput = inputNumber(console, "Insulation Mass (g): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setInsulationMass(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 14:
					if (showPrevData)
						System.out.println("Current Ballast Mass (g): " + flight.getBallastMass());
					userInput = inputNumber(console, "Ballast Mass (g): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setBallastMass(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 15:
					if (showPrevData)
						System.out.println("Current Casing Mass (g): " + flight.getCasingMass());
					userInput = inputNumber(console, "Casing Mass (g): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setCasingMass(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 16:
					if (showPrevData)
						System.out.println("Current Motor Mass (g): " + flight.getMotorMass());
					userInput = inputNumber(console, "Motor Mass (g): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setMotorMass(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 17:
					if (showPrevData)
						System.out.println("Current Altitude (ft): " + flight.getAltitude());
					userInput = inputNumber(console, "Altitude (ft): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setAltitude((int) Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 18:
					if (showPrevData)
						System.out.println("Current Time (s): " + flight.getTime());
					userInput = inputNumber(console, "Time (s): ");
					if (evaluateInput(userInput) == 0) {
						hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
						phase = keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
					} else if (evaluateInput(userInput) == 1) {
						flight.setTime(Double.parseDouble(userInput));
						phase++;
					}
					System.out.println();
					break;
				case 19:
					isDone = false;
					if (showPrevData) {
						System.out.println("Current Modification notes:");
						for (int i = 0; i < flight.getNumModifications(); i++) {
							System.out.println("\t- " + flight.getModification(i));
						}
					}
					System.out.println("Enter Modification notes: ");
					System.out.println(
							"NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");
					while (!isDone) {
						userInput = console.nextLine().trim();
						if (evaluateInput(userInput) == 0) {
							isDone = true;
							hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
							if (!(evaluateKeyword(userInput) == 0)) {
								keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
							} else {
								System.out.println(ERROR_MSG_1);
							}
						} else if (userInput.equalsIgnoreCase("/view") || userInput.equalsIgnoreCase("/v")) {
							if (flight.getNumModifications() > 0) {
								for (int i = 0; i < flight.getNumModifications(); i++) {
									System.out.println(i + ": " + flight.getModification(i));
								}
							} else {
								System.out.println(ERROR_MSG_1);
							}
						} else if (userInput.contains("/del") || userInput.contains("/d")) {
							commandParams = userInput.split(" ");
							if (commandParams.length > 1) {
								if (checkForInteger(commandParams[1])) {
									if (Integer.parseInt(commandParams[1]) < flight.getNumModifications()) {
										flight.removeModification(Integer.parseInt(commandParams[1]));
									} else {
										System.out.println(ERROR_MSG_0);
									}
								} else {
									System.out.println(ERROR_MSG_0);
								}
							} else {
								System.out.println(ERROR_MSG_0);
							}
						} else if (userInput.equalsIgnoreCase("/end") || userInput.equalsIgnoreCase("/e")) {
							isDone = true;
							phase++;
						} else {
							flight.addModification(userInput);
						}
					}
					isDone = false;
					System.out.println();
					break;
				case 20:
					isDone = false;
					if (showPrevData) {
						System.out.println("Current Damage notes:");
						for (int i = 0; i < flight.getNumDamages(); i++) {
							System.out.println("\t- " + flight.getDamage(i));
						}
					}
					System.out.println("Enter Damage notes: ");
					System.out.println(
							"NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");
					while (!isDone) {
						userInput = console.nextLine().trim();
						if (evaluateInput(userInput) == 0) {
							isDone = true;
							hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
							if (!(evaluateKeyword(userInput) == 0)) {
								keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
							} else {
								System.out.println(ERROR_MSG_1);
							}
						} else if (userInput.equalsIgnoreCase("/view") || userInput.equalsIgnoreCase("/v")) {
							if (flight.getNumDamages() > 0) {
								for (int i = 0; i < flight.getNumDamages(); i++) {
									System.out.println(i + ": " + flight.getDamage(i));
								}
							} else {
								System.out.println(ERROR_MSG_1);
							}
						} else if (userInput.contains("/del") || userInput.contains("/d")) {
							commandParams = userInput.split(" ");
							if (commandParams.length > 1) {
								if (checkForInteger(commandParams[1])) {
									if (Integer.parseInt(commandParams[1]) < flight.getNumDamages()) {
										flight.removeDamage(Integer.parseInt(commandParams[1]));
									} else {
										System.out.println(ERROR_MSG_0);
									}
								} else {
									System.out.println(ERROR_MSG_0);
								}
							} else {
								System.out.println(ERROR_MSG_0);
							}
						} else if (userInput.equalsIgnoreCase("/end") || userInput.equalsIgnoreCase("/e")) {
							isDone = true;
							phase++;
						} else {
							flight.addDamage(userInput);
						}
					}
					isDone = false;
					System.out.println();
					break;
				case 21:
					isDone = false;
					if (showPrevData) {
						System.out.println("Current Characteristic notes:");
						for (int i = 0; i < flight.getNumCharacteristics(); i++) {
							System.out.println("\t- " + flight.getCharacteristic(i));
						}
					}
					System.out.println("Enter Characteristic notes: ");
					System.out.println(
							"NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");
					while (!isDone) {
						userInput = console.nextLine().trim();
						if (evaluateInput(userInput) == 0) {
							isDone = true;
							hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
							if (!(evaluateKeyword(userInput) == 0)) {
								keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
							} else {
								System.out.println(ERROR_MSG_1);
							}
						} else if (userInput.equalsIgnoreCase("/view") || userInput.equalsIgnoreCase("/v")) {
							if (flight.getNumCharacteristics() > 0) {
								for (int i = 0; i < flight.getNumCharacteristics(); i++) {
									System.out.println(i + ": " + flight.getCharacteristic(i));
								}
							} else {
								System.out.println(ERROR_MSG_1);
							}
						} else if (userInput.contains("/del") || userInput.contains("/d")) {
							commandParams = userInput.split(" ");
							if (commandParams.length > 1) {
								if (checkForInteger(commandParams[1])) {
									if (Integer.parseInt(commandParams[1]) < flight.getNumCharacteristics()) {
										flight.removeCharacteristic(Integer.parseInt(commandParams[1]));
									} else {
										System.out.println(ERROR_MSG_0);
									}
								} else {
									System.out.println(ERROR_MSG_0);
								}
							} else {
								System.out.println(ERROR_MSG_0);
							}
						} else if (userInput.equalsIgnoreCase("/end") || userInput.equalsIgnoreCase("/e")) {
							isDone = true;
							phase++;
						} else {
							flight.addCharacteristic(userInput);
						}
					}
					isDone = false;
					System.out.println();
					break;
				case 22:
					isDone = false;
					if (showPrevData) {
						System.out.println("Current Consideration notes:");
						for (int i = 0; i < flight.getNumConsiderations(); i++) {
							System.out.println("\t- " + flight.getConsideration(i));
						}
					}
					System.out.println("Enter Consideration notes: ");
					System.out.println(
							"NOTE: enter /view, /v to view past comments; enter /del <index> or /d <index> to remove a comment; enter /end, /e to stop adding messages");
					while (!isDone) {
						userInput = console.nextLine().trim();
						if (evaluateInput(userInput) == 0) {
							isDone = true;
							hasSaved = hasSaved || (evaluateKeyword(userInput) == 2);
							if (!(evaluateKeyword(userInput) == 0)) {
								keywordPhase(console, sheet, fileName, flightNum, phase, userInput);
							} else {
								System.out.println(ERROR_MSG_1);
							}
						} else if (userInput.contains("/view") || userInput.contains("/v")) {
							if (flight.getNumConsiderations() > 0) {
								for (int i = 0; i < flight.getNumConsiderations(); i++) {
									System.out.println(i + ": " + flight.getConsideration(i));
								}
							} else {
								System.out.println(ERROR_MSG_1);
							}
						} else if (userInput.contains("/del") || userInput.contains("/d")) {
							commandParams = userInput.split(" ");
							if (commandParams.length > 1) {
								if (checkForInteger(commandParams[1])) {
									if (Integer.parseInt(commandParams[1]) < flight.getNumConsiderations()) {
										flight.removeConsideration(Integer.parseInt(commandParams[1]));
									} else {
										System.out.println(ERROR_MSG_0);
									}
								} else {
									System.out.println(ERROR_MSG_0);
								}
							} else {
								System.out.println(ERROR_MSG_0);
							}
						} else if (userInput.equalsIgnoreCase("/end") || userInput.equalsIgnoreCase("/e")) {
							isDone = true;
							phase = -1;
						} else {
							flight.addConsideration(userInput);
						}
					}
					isDone = false;
					System.out.println();
					break;
				default:
					System.out.print("Do you want to save? (yes, y/no, n): ");
					if (yesNoChoice(console)) {
						saveSheet(sheet, fileName);
					} else if (!hasSaved) {
						sheet.removeFlight(flightNum);
					}
					System.out.println("Exiting...");
					phase = -2;
					System.out.println();
					break;
			}
		}
	}
}