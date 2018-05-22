package Model;

import Tools.Net;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private InetAddress address;
    private int port;
    private int id;
    private String nickname;
    private List<Integer> friendsID = new ArrayList<>();
    private Net net;

    public User() throws UnknownHostException {
        this(0, "Mapkn3", InetAddress.getLocalHost(), 0);
    }

    public User(int id, String nickname, InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.nickname = nickname;
        this.id = id;
        this.net = new Net(this.port);
    }

    public void addFriend(int id) {
        this.friendsID.add(id);
    }

    public List<Integer> getFriendsID() {
        return this.friendsID;
    }

    public boolean isFriend(int id) {
        return this.friendsID.contains(id);
    }

    public void setAddress(InetAddress address)
    {
        this.address = address;
    }

    public void setPort(int port)
    {
        this.port = port;
        this.net = new Net(this.port);
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public int getPort()
    {
        return port;
    }

    public int getId()
    {
        return id;
    }

    public String getNickname()
    {
        return nickname;
    }

    public Net getNet() {
        return net;
    }
}
