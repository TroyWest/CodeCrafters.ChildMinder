import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.LinkedList;

public class NappyDriver implements Runnable {
    private static BufferedReader in;
    private static NappyServer server = new NappyServer(null);
    private static boolean consoleRunning = true;
    public static LinkedList<Thread> threads = new LinkedList<Thread>();
    public Thread consoleThread;
    public static ServerListener serverListener;
    private static int port;

    public void processInput() {
        System.out.print(':');
        String c = "";
        try {
            c = in.readLine();
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        switch (c) {
            case "a":
                addChild();
                break;
            case "d":
                deleteChild();
                break;
            case "l":
                server.displayChildren();
                break;
            case "s":
                if (serverListener.isRunning) {
                    stopServer();
                } else {
                    startServer();
                }
                break;
            case "q":
                consoleRunning = false;
                break;
            default:
                System.out.println("Unknown command. Please try again");
                break;
        }

    }

    public void displayMenu() {
        System.out.println();
        System.out.println("NappyServer Copyright 2019 CodeCrafters");
        System.out.println("=======================================");
        System.out.println("(a) Add child");
        System.out.println("(d) Delete child");
        System.out.println("(l) List all children");
        if (serverListener.isRunning) {
            System.out.println("(s) Stop server");
        } else {
            System.out.println("(s) Start server");
        }
        System.out.println("(q) Quit Nappy server");
    }

    public void addChild() {
        try {
            System.out.print("Child's Name: ");
            String name = "";
            name = in.readLine();
            System.out.print("Date of birth (yyyy-mm-dd): ");
            String dob = in.readLine();
            server.AddChild(name, Date.valueOf(dob));
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }

    public void deleteChild() {
        try {
            System.out.print("Child's Name: ");
            String name = in.readLine();
            System.out.print("Child's ID: ");
            int id = Integer.parseInt(in.readLine());
            server.DeleteChild(name);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException intex) {
            System.out.println(intex.getMessage());
        }
        in = new BufferedReader(new InputStreamReader(System.in));
        while (consoleRunning) {
            displayMenu();
            processInput();
            if (!consoleRunning) {
                serverListener.stopServer();
                server.closeConnections();
            }
        }
    }

    public void startServer() {
        if (serverListener.isRunning) {
            System.out.println("Server is already running");
        } else {
            System.out.println("Starting server on port " + serverListener.GetPort());
            serverListener = new ServerListener(port);
            serverListener.isRunning = true;
            serverListener.start();
        }
    }

    public void stopServer() {
        if (serverListener.isRunning) {
            System.out.println("Stopping server");
            serverListener.isRunning = false;
            serverListener.stopServer();
        } else {
            System.out.println("Server is not started");
        }
    }


    public static void main(String[] args) {
        port = 5454;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        serverListener = new ServerListener(port);
        serverListener.start();
        NappyDriver driver = new NappyDriver();
        driver.consoleThread = new Thread(driver);
        driver.consoleThread.start();

    }
}
