/**
 * 
 */
package com.himanshu.process;

import com.himanshu.costants.UserInputCommand;
import com.himanshu.exception_Error.ParkingException;
import com.himanshu.serviceInterface.ParkingService;

/**
 * @author Himanshu Tiwari
 *
 */
public interface AbstractParkingProcess {
	public void setService(ParkingService service);

	public void execute(String action) throws ParkingException;

	public default boolean validate(String inputString) {
		// Split the input string to validate command and input value
		boolean valid = true;
		try {
			String[] inputs = inputString.split(" ");
			int param = UserInputCommand.getInputParameter().get(inputs[0]);
			switch (inputs.length) {
			case 1:
				if (param != 0) // e.g status -> inputs = 1
					valid = false;
				break;
			case 2:
				if (param != 1) // create_parking_lot 6 -> inputs = 2
					valid = false;
				break;
			case 3:
				if (param != 2) // park KA-01-P-333 White -> inputs = 3
					valid = false;
				break;
			default:
				valid = false;
			}
		} catch (Exception e) {
			valid = false;
		}
		return valid;
	}
}
