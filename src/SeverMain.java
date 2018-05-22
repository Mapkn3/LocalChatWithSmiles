import Model.Server;

public class SeverMain {

    public static void main(String[] args) {
        int port = 7767;

        Thread mySrv = new Thread(() -> {
            new Server(port);
        });
        mySrv.start();
    }
}
