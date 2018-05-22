package Model;

import Tools.UniqueID;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    private DatagramSocket socket;
    private List<User> users = new ArrayList<>();
    private boolean running = false;

    public Server(int port) {
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Thread serverRun = new Thread(() -> {
            System.out.println("Server start");
            running = true;
            manage();
            receive();
        });
        serverRun.start();
    }

    private void manage() {
        new Thread(() -> {
            while (running) {

            }
        }).start();
    }

    private void receive() {
        new Thread(() -> {
            while (running) {
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                try {
                    socket.receive(packet);
                    System.out.println("Receive packet from " + packet.getAddress().toString() + ":" + packet.getPort());
                    process(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void process(DatagramPacket packet) throws IOException {
        String data;
        Message message = null;
        String nickname;
        String str = new String(packet.getData());
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        System.out.println("Receive: " + str);
        String command = str.substring(0, 3);
        switch (command) {
            case "/t/":
                data = "Connection successful";
                sendTo(address, port, new Message(0, data, ""));
                break;
            case "/c/":
                nickname = str.substring(3, str.length()).trim();
                if (isNewUserByNickname(nickname)) {
                    int id = UniqueID.getID();
                    users.add(new User(id, nickname, address, port));
                } else {
                    getUserByNickname(nickname).setPort(port);
                }
                data = String.format("Login successful:%d",getIDbyNickname(nickname));
                sendTo(address, port, new Message(0, data, nickname));
                sendAll(new Message(0, String.format("Hello, %s!", nickname), ""));
                break;
            default:
                try {
                    message = convertByteToMessage(packet.getData());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                assert message != null;
                byte[] textMessage = String.format("<h2>%s:</h2> %s", getUserById(message.getAuthorID()).getNickname().trim(), message.convert()).getBytes();
                String destination = message.getDestination();
                if (destination.equals("")) {
                    System.out.println(textMessage);
                    sendAll(message);
                } else {
                    User user = getUserByNickname(destination);
                    if (user != null) {
                        sendTo(user, message);
                    }
                    sendTo(getUserById(message.getAuthorID()), message);
                }
        }
    }

    private Message convertByteToMessage(byte[] messageByte) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(messageByte);
        ObjectInputStream is = new ObjectInputStream(in);
        return (Message) is.readObject();
    }

    private void sendTo(InetAddress address, int port, Message message) throws IOException {
        byte[] textMessage;
        if (message.getAuthorID() == 0) {
            textMessage = message.getMessage().getBytes();
        } else {
            textMessage = String.format("<h2>%s:</h2> %s", getUserById(message.getAuthorID()).getNickname().trim(), message.convert()).getBytes();
        }
        DatagramPacket response = new DatagramPacket(textMessage, textMessage.length, address, port);
        socket.send(response);
        System.out.println(String.format("Send to %s:%d: %s", address.getHostAddress(), port, new String(textMessage)));
    }

    private void sendTo(User user, Message message) throws IOException {
        String color = user.getId() == message.getAuthorID() ? "red" : "blue";
        byte[] textMessage;
        if (message.getAuthorID() == 0) {
            textMessage = message.getMessage().getBytes();
        } else {
            textMessage = String.format("<h2 color='%s'>%s:</h2> %s", color, getUserById(message.getAuthorID()).getNickname().trim(), message.convert()).getBytes();
        }
        DatagramPacket response = new DatagramPacket(textMessage, textMessage.length, user.getAddress(), user.getPort());
        socket.send(response);
        System.out.println(String.format("Send to %s:%d: %s", user.getAddress().getHostAddress(), user.getPort(), new String(textMessage)));
    }

    private void sendAll (Message message) throws IOException {
        for (User user : users) {
            sendTo(user, message);
        }
    }

    private User getUserByNickname(String nickname) {
        User result = null;
        String userNickname;
        for (User user : users) {
            userNickname = user.getNickname();
            if (userNickname.equals(nickname)) {
                result = user;
            }
        }
        return result;
    }

    private User getUserById(int id) {
        User result = null;
        for (User user : users) {
            if (user.getId() == id) {
                result = user;
            }
        }
        return result;
    }

    private boolean isNewUserByNickname(String nickname) {
        return getUserByNickname(nickname) == null;
    }

    private int getIDbyNickname(String nickname) {
        return getUserByNickname(nickname).getId();
    }
}