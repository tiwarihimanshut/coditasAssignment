package com.himanshu.parkingApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.himanshu.exception_Error.ErrorCode;
import com.himanshu.exception_Error.ParkingException;
import com.himanshu.process.AbstractParkingProcess;
import com.himanshu.process.RequestProcessor;
import com.himanshu.serviceImpl.ParkingSeviceImpl;

@SpringBootApplication
public class ParkingLotApplication {

	public static void main(String[] args) {

		AbstractParkingProcess processor = new RequestProcessor();
		processor.setService(new ParkingSeviceImpl());
		BufferedReader bufferReader = null;
		String input = null;
		try {
			System.out.println("      Parking creation is in Progress ===>==>==>==>==>===>");
			printUsage();
			switch (args.length) {
			case 0: // taking user input from command line
			{
				System.out.println("Please Enter 'Q' to end Execution");
				System.out.println("Input:");
				while (true) {
					try {
						bufferReader = new BufferedReader(new InputStreamReader(System.in));
						input = bufferReader.readLine().trim();
						if (input.equalsIgnoreCase("Q")) {
							break;
						} else {
							if (processor.validate(input)) {
								try {
									processor.execute(input.trim());
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
							} else {
								printUsage();
							}
						}
					} catch (Exception e) {
						throw new ParkingException(ErrorCode.INVALID_REQUEST.getMessage(), e);
					}
				}
				break;
			}
			case 1: //Passing the input from
			{
				File inputFile = new File(args[0]);
				try {
					bufferReader = new BufferedReader(new FileReader(inputFile));
					int lineNo = 1;
					while ((input = bufferReader.readLine()) != null) {
						input = input.trim();
						if (processor.validate(input)) {
							try {
								processor.execute(input);
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
						} else
							System.out.println("Incorrect Command Found at line: " + lineNo + " ,Input: " + input);
						lineNo++;
					}
				} catch (Exception e) {
					throw new ParkingException(ErrorCode.INVALID_FILE.getMessage(), e);
				}
				break;
			}
			default:
				System.out.println("Invalid input. Usage Style: java -jar <jar_file_path> <input_file_path>");
			}
		} catch (ParkingException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			try {
				if (bufferReader != null)
					bufferReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e);
			}
		}

	}

	private static void printUsage() {
		StringBuffer buffer = new StringBuffer();
		buffer = buffer.append(
				"--------------Please Enter one of the below commands. {variable} to be replaced -----------------------")
				.append("\n");
		buffer = buffer.append("1. For creating parking lot of size n               ---> createParking {capacity}")
				.append("\n");
		buffer = buffer
				.append("2. To park a car                                    ---> park <<car_number>> {car_clour}")
				.append("\n");
		buffer = buffer.append("3. Remove(Unpark) car from parking                  ---> leave {slot_number}")
				.append("\n");
		buffer = buffer.append("4. Print status of parking slot                     ---> status").append("\n");
		buffer = buffer.append(
				"5. cars registration no for the given car color ---> vchicle_number_with_colour {car_color}")
				.append("\n");
		buffer = buffer.append(
				"6. find slot numbers for the given car color         ---> vchicle_slot_number_with_colour {car_color}")
				.append("\n");
		buffer = buffer.append(
				"7. find slot number for the given car number         ---> slot_number_for_registration_number {car_number}")
				.append("\n");
		System.out.println(buffer.toString());
	}

}
