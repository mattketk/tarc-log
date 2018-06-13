package tarcLog;

import java.text.*;
import java.util.*;
import java.io.*;

/*
 * NOTE: At least for now, a number of empty entries will sometimes be -1 or 0 
 *
 */

public class DataSheet implements Serializable {
	private static final String IND_0 = "\t";
	private static final String IND_1 = "\t\t";
	
	private Date date;
	private ArrayList<Flight> flights;
	
	public DataSheet() {
		this.date = new Date();
		this.flights = new ArrayList<Flight>();
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public void addFlight(Flight f) {
		flights.add(f);
	}
	
	public Flight removeFlight(int index) {
		return flights.remove(index);
	}
	
	public Flight getFlight(int index) {
		return flights.get(index);
	}
	
	public int getFlightAmount() {
		return flights.size();
	}
	
	public void printSheet(PrintStream output) {
		Flight flight;
		
		output.println("Date:\t" + new SimpleDateFormat("MM/dd/yyyy").format(this.date));
		output.println();
		
		for (int i = 0; i < flights.size(); i++) {
			flight = flights.get(i);
			output.println("Flight No.\t" + (i + 1));
			
			// weather
			output.println(IND_0 + "Weather:");
			output.printf(IND_1 + "Temperature (F):\t%.1f\n", flight.getTemperature());
			output.printf(IND_1 + "Wind Speed (MPH):\t%.1f\n", flight.getWindSpeed());
			output.printf(IND_1 + "R. Humidity (%%):\t%.1f\n", flight.getHumidity());
			output.println();
			
			// specifications
			output.println(IND_0 + "Specifications:");
			output.println(IND_1 + "Payload:\t\"" + flight.getPayload() + "\"");
			output.println(IND_1 + "Booster:\t\"" + flight.getBooster() + "\"");
			output.println(IND_1 + "Motor:\t" + flight.getMotor());
			output.println(IND_1 + "Delay (s):\t" + flight.getDelay());
			output.println(IND_1 + "Parachute:\t" + flight.getParachute());
			output.println();
			
			// masses
			output.println(IND_0 + "Masses:");
			output.printf(IND_1 + "Payload (g):\t%.1f\n", flight.getPayloadMass());
			output.printf(IND_1 + "Booster (g):\t%.1f\n", flight.getBoosterMass());
			
			// egg masses
			output.print(IND_1 + "Eggs (g):\t\t");
			for (int j = 0; i < flight.getNumEggs() - 1; i++) {
				output.printf("%.0f, ", flight.getEggMass(j));
			}
			output.printf("%.0f\n", flight.getEggMass(flight.getNumEggs() - 1));
			
			output.printf(IND_1 + "Parachute (g):\t%.1f\n", flight.getParachuteMass());
			output.printf(IND_1 + "Nomex (g):\t\t%.1f\n", flight.getNomexMass());
			output.printf(IND_1 + "Insulation (g):\t%.1f\n", flight.getInsulationMass());
			output.printf(IND_1 + "Ballast (g):\t%.1f\n", flight.getBallastMass());
			output.printf(IND_1 + "Casing (g):\t\t%.1f\n", flight.getCasingMass());
			output.printf(IND_1 + "Motor (g):\t\t%.1f\n", flight.getMotorMass());
			output.printf(IND_1 + "Total (g):\t\t%.1f\n", flight.getTotalMass());
			output.println();
			
			// results
			output.println(IND_0 + "Results:");
			output.println(IND_1 + "Altitude (ft):\t" + flight.getAltitude());
			output.printf(IND_1 + "Time (s):\t\t%.2f\n", flight.getTime());
			
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
			for (int j = 0; j < flight.getNumModifications(); j++) {
				output.println(IND_1 + "-\t" + flight.getModification(j));
			}
			output.println();
			
			// damages
			output.println(IND_1 + "Damages:");
			for (int j = 0; j < flight.getNumDamages(); j++) {
				output.println(IND_1 + "-\t" + flight.getDamage(j));
			}
			output.println();
			
			// characteristics
			output.println(IND_1 + "Characteristics:");
			for (int j = 0; j < flight.getNumCharacteristics(); j++) {
				output.println(IND_1 + "-\t" + flight.getCharacteristic(j));
			}
			output.println();
			
			// considerations
			output.println(IND_1 + "Considerations:");
			for (int j = 0; j < flight.getNumConsiderations(); j++) {
				output.println(IND_1 + "-\t" + flight.getConsideration(j));
			}
			output.println("\n");
		}
	}
}