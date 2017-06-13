import com.intel.bluetooth.btgoep.Connection;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.activation.MimetypesFileTypeMap;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;


import java.io.*;
import java.rmi.server.ExportException;

/**
 * Created by Meyttt on 01.05.2017.
 */
//TODO: изначально получить устройство установить связь и дальше пытаться искать именно его, посылать запросы и тп?
public class SmthStrange {
    private static Object lock = new Object();
    static String rUrl=null;
    static class  StrangeListener implements DiscoveryListener{

        public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
            String name;
            try {
                name=remoteDevice.getFriendlyName(false);

            }catch (Exception e){
                name = remoteDevice.getBluetoothAddress();
            }
            System.out.println("device found: "+name );
            System.out.println("addres: "+ remoteDevice.getBluetoothAddress());
        }

        public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
//            for(ServiceRecord record:serviceRecords){
//                System.out.println("ID of service "+record.getAttributeIDs());
//                try {
//                    System.out.println(record.getHostDevice().getFriendlyName(false));
//                } catch (IOException e) {
//                    System.out.println(record.getHostDevice().getBluetoothAddress());
//                }
//            }
            for(ServiceRecord serviceRecord:serviceRecords){
                String url = serviceRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT,false);
                if(url==null){continue;}
                DataElement serviceName = serviceRecord.getAttributeValue(0x0100);
                if(serviceName==null){
                    System.out.println("service found "+url);
                    rUrl=url;
                }else{
                    System.out.println("service "+serviceName.getValue()+" found "+ url);
                    rUrl=url;
                    if(serviceName.getValue().equals("OBEX Object Push")){
                        try {
                            sendNewMessage(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    sendNewMessage(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                System.out.println("---");
//                String url2 = serviceRecord.getConnectionURL(ServiceRecord.AUTHENTICATE_NOENCRYPT,false);
//                if(url2==null){continue;}
//                DataElement serviceName2 = serviceRecord.getAttributeValue(0x0100);
//                if(serviceName2==null){
//                    System.out.println("service found "+url2);
//                }else{
//                    System.out.println("service "+serviceName2.getValue()+" found "+ url2);
//                    if(serviceName2.getValue().equals("OBEX Object Push")){
////                        try {
////                            sendNewMessage(url2);
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
//                    }
//                }
//
//                System.out.println("---");
//                String url3 = serviceRecord.getConnectionURL(ServiceRecord.AUTHENTICATE_ENCRYPT,false);
//                if(url3==null){continue;}
//                DataElement serviceName3 = serviceRecord.getAttributeValue(0x0100);
//                if(serviceName3==null){
//                    System.out.println("service found "+url3);
//                }else{
//                    System.out.println("service "+serviceName3.getValue()+" found "+ url3);
//                    if(serviceName3.getValue().equals("OBEX Object Push")){
////                        try {
////                            sendNewMessage(url3);
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
//                    }
//                }

            }
        }

        public void serviceSearchCompleted(int i, int i1) {
            synchronized (lock){
                lock.notify();
            }
        }

        public void inquiryCompleted(int i) {
            synchronized (lock) {
                lock.notify();
            }
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        UUID []uuid = new UUID[1];
        uuid[0]=new UUID(0x1105);
        int[] attrIDs = new int[]{0x0100};
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();
        StrangeListener listener = new StrangeListener();
        agent.startInquiry(DiscoveryAgent.GIAC, listener);
        try {
            synchronized (lock){
                lock.wait();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
            return;
        }
        RemoteDevice remoteDevice = null;
        RemoteDevice[] devices=agent.retrieveDevices(0);
        for(RemoteDevice device:devices){
            if(!(device.getFriendlyName(false) ==null)){
                if(device.getFriendlyName(false).contains("Z00LD")){
                    remoteDevice=device;
                    agent.cancelInquiry(listener);
                }
            }
        }
        if(remoteDevice!=null) {
            agent.searchServices(null, uuid,remoteDevice,new StrangeListener());
            try {
                synchronized (lock){
                    lock.wait();
                }
            }catch (InterruptedException i) {
                i.printStackTrace();
                return;
            }
        }
//        sendNewMessage(rUrl);
        javax.microedition.io.Connection connection=Connector.open(rUrl);
        remoteDevice.authenticate();

        while(true){
            if (remoteDevice.isAuthenticated()) break;
            else {
                System.out.println(remoteDevice.isAuthenticated());
                remoteDevice.authenticate();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Thread.sleep(30000);
        connection.close();
        sendNewMessage(rUrl);
        System.out.println("services-2");
        agent.searchServices(null, uuid,remoteDevice,new StrangeListener());
//        }
        int i=0;
        while (true){
            System.out.println("---->"+i+"<----");
            i++;
            agent.startInquiry(DiscoveryAgent.GIAC,listener);
            Thread.sleep(10000);
            System.out.println("-----------");
            agent.cancelInquiry(listener);
        }
//        localDevice.


    }
    //todo: почитать про http коды
    private static void sendNewMessage(String url) throws IOException {
        System.out.println("Connection to... "+url);
        ClientSession clientSession= (ClientSession) Connector.open(url);
        HeaderSet headerSet = clientSession.connect(null);
        if(headerSet.getResponseCode()!= ResponseCodes.OBEX_HTTP_OK){
            System.out.println("Fail >_<");
            return;
        }
        HeaderSet hsOp = clientSession.createHeaderSet();
        hsOp.setHeader(HeaderSet.NAME,"Hello.txt");
        hsOp.setHeader(HeaderSet.TYPE, "text");
        Operation putOp = clientSession.put(hsOp);
        byte data[] = "Hello World!!!".getBytes("iso-8859-1");
        OutputStream outputStream = putOp.openOutputStream();
        outputStream.write(data);
        outputStream.close();
        putOp.close();
        clientSession.disconnect(null);
        clientSession.close();
    }
    private static String sendMessageCustom(String address12){
        //todo: почему бы не испозовать сокеты из библиотек андроида?
        String address = "";
        for(int i=0; i<address12.length();){
            address+=address12.charAt(i);
            i++;
            address+=address12.charAt(i);
            i++;
            address+=":";
        }
        return address;
//        try {
//            BluetoothSocket btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
//        } catch (IOException e) {
//            AlertBox("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
//        }
//        return null;
    }

}
