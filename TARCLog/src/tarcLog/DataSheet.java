package tarcLog;

import java.text.*;
import java.util.*;
import java.io.*;

/*
 * NOTE: At least for now, a number of empty entries will sometimes be -1 or 0 
 *
 */

public class DataSheet {
	private static final String IND_0 = "\t";
	private static final String IND_1 = "\t\t";
	
	private String date;
	private ArrayList<Flight> flights;
	
	public DataSheet() {
		this.date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		this.flights = new ArrayList<Flight>();
	}
	
	public void addFlight(Flight f) {
		flights.add(f);
	}
	
	public Flight removeFlight(int index) {
		return flights.remove(index);
	}
	
	public void saveSheet(PrintStream output) {
		output.println(date);
		for (int i = 0; i < flights.size(); i++) {
			flights.get(i).saveFlight(output);
			output.println();
		}
	}
	
	public void loadSheet(Scanner cache) {
		Flight temp;
		this.date = cache.nextLine();
		while (!cache.hasNextLine()) {	// TODO: watch out here
			temp = new Flight();
			temp.loadFlight(cache);
			flights.add(temp);
		}
	}
	
	public void printSheet(PrintStream output) {
		Flight flight;
		
		output.println("Date:\t" + date);
		output.println();
		
		for (int i = 0; i < flights.size(); i++) {
			flight = flights.get(i);
			output.println("Flight No.\t" + (i + 1));
			
			// weather
			output.println(IND_0 + "Weather:");
			output.printf(IND_1 + "Temperature (F):\t%d\n", flight.getTemperature());
			output.printf(IND_1 + "Wind Speed (MPH):\t%d\n", flight.getWindSpeed());
			output.printf(IND_1 + "Relative Humidity (%):\t%d\n", flight.getHumidity());
			output.println();
			
			// specifications
			output.println(IND_0 + "Specifications:");
			output.println(IND_1 + "Payload:\t\"" + flight.getPayload() + "\"");
			output.println(IND_1 + "Booster:\t\"" + flight.getBooster() + "\"");
			output.println(IND_1 + "Motor Model:\t" + flight.getMotor());
			output.println(IND_1 + "Delay (s):\t" + flight.getDelay());
			output.println(IND_1 + "Parachute:\t" + flight.getParachute());
			output.println();
			
			// masses
			output.println(IND_0 + "Masses:");
			output.printf(IND_1 + "Payload (g):\t%d\n", flight.getPayloadMass());
			output.printf(IND_1 + "Booster (g):\t%d\n", flight.getBoosterMass());
			
			// egg masses
			output.print(IND_1 + "Eggs (g):\t");
			for (int j = 0; i < flight.getNumEggs() - 1; i++) {
				output.printf("%d, ", flight.getEggMass(j));
			}
			output.printf("%d", flight.getEggMass(flight.getNumEggs() - 1));
			
			output.printf(IND_1 + "Parachute (g):\t%d\n", flight.getParachuteMass());
			output.printf(IND_1 + "Nomex (g):\t%d\n", flight.getNomexMass());
			output.printf(IND_1 + "Insulation (g):\t%d\n", flight.getInsulationMass());
			output.printf(IND_1 + "Ballast (g):\t%d\n", flight.getBallastMass());
			output.printf(IND_1 + "Casing (g):\t%d\n", flight.getCasingMass());
			output.printf(IND_1 + "Motor (g):\t%d\n", flight.getMotorMass());
			output.printf(IND_1 + "Total (g):\t%d\n", flight.getTotalMass());
			output.println();
			
			// results
			output.println(IND_0 + "Results:");
			output.println(IND_1 + "Altitude (ft):\t" + flight.getAltitude());
			output.printf(IND_1 + "Time (s):\t%.2f\n", flight.getTime());
			
			// flight score
			if (((int) flight.getScore()) != -1)  
				output.printf(IND_1 + "Flight Score:\t%.2f\n", flight.getScore());
			else
				output.println(IND_1 + "Flight Score:\tN/A");
			output.println();
			
			// notes
			output.println(IND_0 + "Notes:");
			
			// modifications
			output.println(IND_1 + "Modifications:");
			for (int j = 0; i < flight.getNumModifications(); i++) {
				output.println(IND_1 + "-\t" + flight.getModification(j));
			}
			output.println();
			
			// damages
			output.println(IND_1 + "Damages:");
			for (int j = 0; i < flight.getNumDamages(); i++) {
				output.println(IND_1 + "-\t" + flight.getDamage(j));
			}
			output.println();
			
			// characteristics
			output.println(IND_1 + "Characteristics:");
			for (int j = 0; i < flight.getNumCharacteristics(); i++) {
				output.println(IND_1 + "-\t" + flight.getCharacteristic(j));
			}
			output.println();
			
			// considerations
			output.println(IND_1 + "Considerations:");
			for (int j = 0; i < flight.getNumConsiderations(); i++) {
				output.println(IND_1 + "-\t" + flight.getConsideration(j));
			}
			output.println("\n");
		}
	}
}