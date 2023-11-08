package xyz.necrozma.device;

import java.util.ArrayList;
import java.util.UUID;

public class HardwareConnection {
    // TODO: Implement this class, for now returns sample data

    public static String getDeviceStatus(String deviceID) {
        return "Device status for " + deviceID;
    }

    public static ArrayList<String> getDeviceList() {
        ArrayList<String> deviceList = new ArrayList<String>();
        deviceList.add("Device 1");
        deviceList.add("Device 2");
        deviceList.add("Device 3");
        deviceList.add("Device 4");
        return deviceList;
    }
}
