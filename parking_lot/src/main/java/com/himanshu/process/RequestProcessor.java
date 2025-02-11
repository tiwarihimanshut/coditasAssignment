package com.himanshu.process;
/**
 * @author Himanshu Tiwari
 *
 */
import com.himanshu.costants.Constant;
import com.himanshu.exception_Error.ErrorCode;
import com.himanshu.exception_Error.ParkingException;
import com.himanshu.model.Car;
import com.himanshu.serviceImpl.ParkingSeviceImpl;
import com.himanshu.serviceInterface.ParkingService;

public class RequestProcessor implements AbstractParkingProcess {
	private ParkingService parkingService;

	public void setParkingService(ParkingService parkingService) throws ParkingException {
		this.parkingService = parkingService;
	}

	@Override
	public void execute(String input) throws ParkingException {
		int level = 1;
		String[] inputs = input.split(" ");
		String key = inputs[0];
		switch (key) {
		case Constant.CREATE_PARKING_SIZE:
			try {
				int capacity = Integer.parseInt(inputs[1]);
				parkingService.createParkingLot(level, capacity);
			} catch (NumberFormatException e) {
				throw new ParkingException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "capacity"));
			}
			break;
		case Constant.PARK:
			parkingService.park(level, new Car(inputs[1], inputs[2]));
			break;
		case Constant.LEAVE:
			try {
				int slotNumber = Integer.parseInt(inputs[1]);
				parkingService.unPark(level, slotNumber);
			} catch (NumberFormatException e) {
				throw new ParkingException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"));
			}
			break;
		case Constant.STATUS:
			parkingService.getStatus(level);
			break;
		case Constant.VCHICLE_NUMBER_WITH_COLOUR:
			parkingService.getRegNumberForColor(level, inputs[1]);
			break;
		case Constant.VEHICLE_SLOT_NUMBER_WITH_COLOR:
			parkingService.getSlotNumbersFromColor(level, inputs[1]);
			break;
		case Constant.SLOTS_NUMBER_FOR_REG_NUMBER:
			parkingService.getSlotNoFromRegistrationNo(level, inputs[1]);
			break;
		default:
			break;
		}
	}

	@Override
	public void setService(ParkingService service) {
		this.parkingService = (ParkingSeviceImpl) service;
	}
}