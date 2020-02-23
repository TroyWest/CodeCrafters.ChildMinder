import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener extends Thread {
    public boolean isRunning = true;
    private int port = 5454;
    private ServerSocket ss;

    public ServerListener(int _port) {
        port = _port;
    }

    public int GetPort() {
        return port;
    }

    public void stopServer() {
        try {
            this.ss.close();
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Server listening on port " + port);
        try {
            while (isRunning) {
                NappyServer serverInstance;
                ss = new ServerSocket(port);

                serverInstance = new NappyServer(ss.accept());
                Thread newInstanceThread = new Thread(serverInstance);
                NappyDriver.threads.add(newInstanceThread);
                newInstanceThread.start();
                ss.close();
            }

        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }
}
