/**
 * 
 */
package com.himanshu.DaoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.himanshu.costants.Constant;
import com.himanshu.daoInterface.ParkingDataDao;
import com.himanshu.model.NearestFirstParkingStrategy;
import com.himanshu.model.ParkingStrategy;
import com.himanshu.model.Vehicle;

/**
 * @author Himanshu Tiwari
 *
 */
public class ParkingDataDaoImpl<T extends Vehicle> implements ParkingDataDao<T> {
	

	// For Multilevel Parking lot - 0 -> Ground floor 1 -> First Floor etc
	private AtomicInteger	level			= new AtomicInteger(0);
	private AtomicInteger	capacity		= new AtomicInteger();
	private AtomicInteger	availability	= new AtomicInteger();
	// Allocation Strategy for parking
	private ParkingStrategy parkingStrategy;
	// this is per level - slot - vehicle
	private Map<Integer, Optional<T>> slotVehicleMap;
	
	@SuppressWarnings("rawtypes")
	private static ParkingDataDaoImpl instance = null;
	
	@SuppressWarnings("unchecked")
	public static <T extends Vehicle> ParkingDataDaoImpl<T> getInstance(int level, int capacity,
			ParkingStrategy parkingStrategy)
	{
		if (instance == null)
		{
			synchronized (ParkingDataDaoImpl.class)
			{
				if (instance == null)
				{
					instance = new ParkingDataDaoImpl<T>(level, capacity, parkingStrategy);
				}
			}
		}
		return instance;
	}
	
	private ParkingDataDaoImpl(int level, int capacity, ParkingStrategy parkingStrategy)
	{
		this.level.set(level);
		this.capacity.set(capacity);
		this.availability.set(capacity);
		this.parkingStrategy = parkingStrategy;
		if (parkingStrategy == null)
			parkingStrategy = new NearestFirstParkingStrategy();
		slotVehicleMap = new ConcurrentHashMap<>();
		for (int i = 1; i <= capacity; i++)
		{
			slotVehicleMap.put(i, Optional.empty());
			parkingStrategy.add(i);
		}
	}
	
	@Override
	public int parkCar(T vehicle)
	{
		int availableSlot;
		if (availability.get() == 0)
		{
			return Constant.NOT_AVAILABLE;
		}
		else
		{
			availableSlot = parkingStrategy.getSlot();
			if (slotVehicleMap.containsValue(Optional.of(vehicle)))
				return Constant.VEHICLE_ALREADY_EXIST;
			
			slotVehicleMap.put(availableSlot, Optional.of(vehicle));
			availability.decrementAndGet();
			parkingStrategy.removeSlot(availableSlot);
		}
		return availableSlot;
	}
	
	@Override
	public boolean leaveCar(int slotNumber)
	{
		if (!slotVehicleMap.get(slotNumber).isPresent()) // Slot already empty
			return false;
		availability.incrementAndGet();
		parkingStrategy.add(slotNumber);
		slotVehicleMap.put(slotNumber, Optional.empty());
		return true;
	}
	
	@Override
	public List<String> getStatus()
	{
		List<String> statusList = new ArrayList<>();
		for (int i = 1; i <= capacity.get(); i++)
		{
			Optional<T> vehicle = slotVehicleMap.get(i);
			if (vehicle.isPresent())
			{
				statusList.add(i + "\t\t" + vehicle.get().getRegistrationNo() + "\t\t" + vehicle.get().getColor());
			}
		}
		return statusList;
	}
	
	public int getAvailableSlotsCount()
	{
		return availability.get();
	}
	
	@Override
	public List<String> getRegNumberForColor(String color)
	{
		List<String> statusList = new ArrayList<>();
		for (int i = 1; i <= capacity.get(); i++)
		{
			Optional<T> vehicle = slotVehicleMap.get(i);
			if (vehicle.isPresent() && color.equalsIgnoreCase(vehicle.get().getColor()))
			{
				statusList.add(vehicle.get().getRegistrationNo());
			}
		}
		return statusList;
	}
	
	@Override
	public List<Integer> getSlotNumbersFromColor(String colour)
	{
		List<Integer> slotList = new ArrayList<>();
		for (int i = 1; i <= capacity.get(); i++)
		{
			Optional<T> vehicle = slotVehicleMap.get(i);
			if (vehicle.isPresent() && colour.equalsIgnoreCase(vehicle.get().getColor()))
			{
				slotList.add(i);
			}
		}
		return slotList;
	}
	
	@Override
	public int getSlotNoFromRegistrationNo(String registrationNo)
	{
		int result = Constant.NOT_FOUND;
		for (int i = 1; i <= capacity.get(); i++)
		{
			Optional<T> vehicle = slotVehicleMap.get(i);
			if (vehicle.isPresent() && registrationNo.equalsIgnoreCase(vehicle.get().getRegistrationNo()))
			{
				result = i;
			}
		}
		return result;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	@Override
	public void doCleanUp()
	{
		this.level = new AtomicInteger();
		this.capacity = new AtomicInteger();
		this.availability = new AtomicInteger();
		this.parkingStrategy = null;
		slotVehicleMap = null;
		instance = null;
	}


}
