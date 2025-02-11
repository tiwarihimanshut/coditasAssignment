/**
 * 
 */
package com.himanshu.DaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.himanshu.daoInterface.ParkingDao;
import com.himanshu.model.NearestFirstParkingStrategy;
import com.himanshu.model.ParkingStrategy;
import com.himanshu.model.Vehicle;

/**
 * @author Himanshu Tiwari
 *
 */
public class ParkingDaoImpl<T extends Vehicle> implements ParkingDao<T> {
	

	private Map<Integer, ParkingDataDaoImpl<T>> levelParkingMap;
	
	@SuppressWarnings("rawtypes")
	private static ParkingDaoImpl instance = null;
	
	@SuppressWarnings("unchecked")
	public static <T extends Vehicle> ParkingDaoImpl<T> getInstance(List<Integer> parkingLevels,
			List<Integer> capacityList, List<ParkingStrategy> parkingStrategies)
	{
		// Make sure the each of the lists are of equal size
		if (instance == null)
		{
			synchronized (ParkingDaoImpl.class)
			{
				if (instance == null)
				{
					instance = new ParkingDaoImpl<T>(parkingLevels, capacityList, parkingStrategies);
				}
			}
		}
		return instance;
	}
	
	private ParkingDaoImpl(List<Integer> parkingLevels, List<Integer> capacityList,
			List<ParkingStrategy> parkingStrategies)
	{
		if (levelParkingMap == null)
			levelParkingMap = new HashMap<>();
		for (int i = 0; i < parkingLevels.size(); i++)
		{
			levelParkingMap.put(parkingLevels.get(i), ParkingDataDaoImpl.getInstance(parkingLevels.get(i),
					capacityList.get(i), new NearestFirstParkingStrategy()));
			
		}
	}
	
	@Override
	public int parkCar(int level, T vehicle)
	{
		return levelParkingMap.get(level).parkCar(vehicle);
	}
	
	@Override
	public boolean leaveCar(int level, int slotNumber)
	{
		return levelParkingMap.get(level).leaveCar(slotNumber);
	}
	
	@Override
	public List<String> getStatus(int level)
	{
		return levelParkingMap.get(level).getStatus();
	}
	
	public int getAvailableSlotsCount(int level)
	{
		return levelParkingMap.get(level).getAvailableSlotsCount();
	}
	
	@Override
	public List<String> getRegNumberForColor(int level, String color)
	{
		return levelParkingMap.get(level).getRegNumberForColor(color);
	}
	
	@Override
	public List<Integer> getSlotNumbersFromColor(int level, String color)
	{
		return levelParkingMap.get(level).getSlotNumbersFromColor(color);
	}
	
	@Override
	public int getSlotNoFromRegistrationNo(int level, String registrationNo)
	{
		return levelParkingMap.get(level).getSlotNoFromRegistrationNo(registrationNo);
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	public void doCleanup()
	{
		for (ParkingDataDaoImpl<T> levelDataManager : levelParkingMap.values())
		{
			levelDataManager.doCleanUp();
		}
		levelParkingMap = null;
		instance = null;
	}


}
