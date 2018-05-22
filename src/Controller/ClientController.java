package Controller;

import Model.User;
import View.*;

import java.net.*;


public class ClientController {

    private ClientController controller;
    private User user;
    private boolean isConnected = false;

    public void setPort(int port) {
        this.user.setPort(port);
    }

    public void setAddress(InetAddress address) {
        this.user.setAddress(address);
    }

    public void setNickname(String nickname) {
        this.user.setNickname(nickname);
    }

    public void setId(int id) {
        this.user.setId(id);
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean getConnected() {
        return this.isConnected;
    }

    public User getUser() {
        return this.user;
    }

    public void connect() {
        ConnectView connectView = new ConnectView(controller);
        connectView.setVisible(true);
    }

    public void login() {
        LoginView loginView = new LoginView(controller);
        loginView.setVisible(true);
    }

    public void client() {
            ClientView clientView = new ClientView(controller);
            clientView.setVisible(true);
    }

    public ClientController(User user) {
        this.user = user;
        this.controller = this;
    }

    public static void main(String[] args) {
        try
        {
            ClientController controller = new ClientController(new User());
            System.out.println("Create controller");
            controller.connect();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }
}
