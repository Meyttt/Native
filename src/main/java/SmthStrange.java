import javax.bluetooth.*;

import java.rmi.server.ExportException;

/**
 * Created by Meyttt on 01.05.2017.
 */

public class SmthStrange {
    private static Object lock = new Object();
    public static void main(String[] args) throws BluetoothStateException {
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        agent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
                String name;
                try {
                    name=remoteDevice.getFriendlyName(false);
                }catch (Exception e){
                    name = remoteDevice.getBluetoothAddress();
                }
                System.out.println("device found: "+name);
            }

            public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

            }

            public void serviceSearchCompleted(int i, int i1) {

            }

            public void inquiryCompleted(int i) {
                synchronized (lock) {
                    lock.notify();
                }
            }
        });
        try {
            synchronized (lock){
                lock.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Device Inquiry Completed.");

    }
}
