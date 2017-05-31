import javax.bluetooth.*;

/**
 * Created by Meyttt on 31.05.2017.
 */
public class SmthVERYStrange {
    public static void main(String[] args) throws BluetoothStateException {
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        DiscoveryAgent discoveryAgent=localDevice.getDiscoveryAgent();
        boolean complete = discoveryAgent.startInquiry(0, new DiscoveryListener() {
            public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {

            }

            public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {

            }

            public void serviceSearchCompleted(int i, int i1) {

            }

            public void inquiryCompleted(int i) {

            }
        });
    }
}
