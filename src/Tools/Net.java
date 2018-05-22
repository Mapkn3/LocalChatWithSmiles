package Tools;

import java.io.IOException;
import java.net.*;

public class Net {
    private DatagramSocket socket = null;
    private InetAddress ip = null;
    private int port;

    public Net(int port) {
        this.port = port;
    }

    public boolean openConnection(String address) {
        try {
            socket = new DatagramSocket();
            ip = InetAddress.getByName(address);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String receive() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(packet.getData()).trim();
    }

    public void send(final byte[] data) {
        Thread send = new Thread(() -> {
            DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        send.start();
    }
}
