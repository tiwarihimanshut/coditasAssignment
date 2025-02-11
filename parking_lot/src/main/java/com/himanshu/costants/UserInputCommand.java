/**
 * 
 */
package com.himanshu.costants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Himanshu Tiwari
 *
 */
public class UserInputCommand {
	private static volatile Map<String, Integer> inputPrameter  = new HashMap<String, Integer>();
	static{
		inputPrameter.put(Constant.CREATE_PARKING_SIZE, 1);
		inputPrameter.put(Constant.PARK, 2);
		inputPrameter.put(Constant.LEAVE, 1);
		inputPrameter.put(Constant.STATUS, 0);
		inputPrameter.put(Constant.VCHICLE_NUMBER_WITH_COLOUR, 1);
		inputPrameter.put(Constant.VEHICLE_SLOT_NUMBER_WITH_COLOR, 1);
		inputPrameter.put(Constant.SLOTS_NUMBER_FOR_REG_NUMBER, 1);
	}
	
	
	
	public static Map<String, Integer> getInputParameter(){
		return inputPrameter;
	}
	
	public static void addCommand(String command, int parameterCount)
	{
		inputPrameter.put(command, parameterCount);
	}
	
}

