import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class SampleSPPClient implements DiscoveryListener {

    //object used for waiting
    private static Object lock = new Object();
    private static Object lock2 = new Object();

    //vector containing the devices discovered
    private static Vector vecDevices = new Vector();

    private static String connectionURL = null;

    public static void main0(String[] args) throws IOException {

        SampleSPPClient client = new SampleSPPClient();

        //display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());

        //find devices
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();

        System.out.println("Starting device inquiry...");
        //todo: почитать про коды(?)
        agent.startInquiry(DiscoveryAgent.GIAC, client);

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Device Inquiry Completed. ");

        //print all devices in vecDevices
        int deviceCount = vecDevices.size();

        if (deviceCount <= 0) {
            System.out.println("No Devices Found .");
            System.exit(0);
        } else {
            //print bluetooth device addresses and names in the format [ No. address (name) ]
            System.out.println("Bluetooth Devices: ");
            for (int i = 0; i < deviceCount; i++) {
                RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " (" + remoteDevice.getFriendlyName(true) + ")");
            }
        }

        System.out.print("Choose Device index: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        String chosenIndex = bReader.readLine();
        int index = Integer.parseInt(chosenIndex.trim());

        //check for spp service
        RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(index - 1);
        UUID[] uuidSet = new UUID[1];
        uuidSet[0] = new UUID("446118f08b1e11e29e960800200c9a66", false);

        System.out.println("\nSearching for service...");
        agent.searchServices(null, uuidSet, remoteDevice, client);

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (connectionURL == null) {
            System.out.println("Device does not support Simple SPP Service.");
            System.exit(0);
        }
//        StreamConnectionNotifier streamConnectionNotifier=Connector.open()
//        DataOutputStream dataOutputStream=Connector.openDataOutputStream(connectionURL);
//        dataOutputStream.writeChars("\"Hello world!\"");
//        dataOutputStream.flush();
//        dataOutputStream.close();
        //connect to the server and send a line of text
        StreamConnection streamConnection = (StreamConnection) Connector.open(connectionURL);


        //send string
        OutputStream outStream = streamConnection.openOutputStream();
        PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
        pWriter.write("Test String from SPP Client\r\n");
        pWriter.flush();
        pWriter.close();
        outStream.close();
//        outStream.close();
        System.out.println("sent...");
//        streamConnection.close();


        //read response
        InputStream inStream = streamConnection.openInputStream();
        BufferedReader bReader2 = new BufferedReader(new InputStreamReader(inStream));
        String lineRead = bReader2.readLine();
        System.out.println(lineRead);
        streamConnection.close();
        /*
        DataInputStream mmInStream = new DataInputStream(inputStream);
        String newLine = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line; boolean flag = false;
        while ((line = reader.readLine()) != null) {
            result.append(flag? newLine: "").append(line);
            flag = true;
        }
        */

//        tmpIn.close();

    }//main

    //methods of DiscoveryListener
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        //add the device to the vector
        if (!vecDevices.contains(btDevice)) {
            vecDevices.addElement(btDevice);
        }
    }

    //implement this method since services are not being discovered
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if (servRecord != null && servRecord.length > 0) {
            connectionURL = servRecord[0].getConnectionURL(0, false);
        }
        synchronized (lock) {
            lock.notify();
        }
    }

    //implement this method since services are not being discovered
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (lock) {
            lock.notify();
        }
    }


    public void inquiryCompleted(int discType) {
        synchronized (lock) {
            lock.notify();
        }

    }//end method

    public ObservableList<String> getDevicesList() throws IOException {
        SampleSPPClient client = new SampleSPPClient();

        //display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());

        //find devices
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();

        System.out.println("Starting device inquiry...");
        //todo: почитать про коды(?)
        agent.startInquiry(DiscoveryAgent.GIAC, client);

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> res = new LinkedList<String>();
        RemoteDevice[] devices = agent.retrieveDevices(0);
        for (RemoteDevice remoteDevice : devices) {
            String name = remoteDevice.getFriendlyName(false);
            if (!(name == null)) {
                if (!(name.equals(""))) {
                   res.add("Name: " + name + "; address: " + remoteDevice.getBluetoothAddress());
                    continue;
                }
            }
            res.add("Name:(no name); address: " + remoteDevice.getBluetoothAddress());
        }

        return FXCollections.observableArrayList(res);
    }

    public static void main(String[] args) throws IOException {
        SampleSPPClient sampleSPPClient = new SampleSPPClient();
        ObservableList<String> devices = sampleSPPClient.getDevicesList();
        NewGraphics newGraphics = new NewGraphics();
        newGraphics.launch(devices);
//Runtime.getRuntime().exec("rundll32 user32.dll,LockWorkStation");

    }

}