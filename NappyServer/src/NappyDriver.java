import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.sql.Date;
import java.util.LinkedList;

public class NappyDriver implements Runnable {
    private static BufferedReader in;
    private static NappyServer server = new NappyServer(null);
    private static boolean isRunning = true;
    private static int port = 5454;
    private static LinkedList<Thread> threads = new LinkedList<Thread>();

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
            case "q":
                isRunning = false;
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
        System.out.println("(q) Quit Nappy server");
    }

    public void addChild() {
        try {
            System.out.print("Models.Child's Name: ");
            String name = "";
            //while (name == "") {
            name = in.readLine();
            //}

            System.out.print("Date of birth (yyyy-mm-dd): ");
            String dob = in.readLine();
            server.AddChild(name, Date.valueOf(dob));
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }

    public void deleteChild() {
        try {
            System.out.print("Models.Child's Name: ");
            String name = in.readLine();
            System.out.print("Models.Child's ID: ");
            int id = Integer.parseInt(in.readLine());
            server.DeleteChild(name);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
    }

    @Override
    public void run() {
        in = new BufferedReader(new InputStreamReader(System.in));
        while (isRunning) {
            displayMenu();
            processInput();
        }
        for (int i = 0; i < threads.size(); i++) {
            Thread current = threads.get(i);
            current.interrupt();
        }
        server.closeConnections();
    }


    public static void main(String[] args) {
        int port = 5454;
        NappyDriver driver = new NappyDriver();
        Thread console = new Thread(driver);
        console.start();
        try {
            while (isRunning) {
                NappyServer serverInstance;
                ServerSocket ss = new ServerSocket(port);
                serverInstance = new NappyServer(ss.accept());
                Thread newInstanceThread = new Thread(serverInstance);
                threads.add(newInstanceThread);
                newInstanceThread.start();
                ss.close();
            }

        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }

    }
}
