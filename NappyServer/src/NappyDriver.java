import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;

public class NappyDriver {
    private static BufferedReader in;
    private static NappyServer server;
    private static boolean isRunning = true;

    public static void processInput() {
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

    public static void displayMenu() {
        System.out.println();
        System.out.println("NappyServer Copyright 2019 CodeCrafters");
        System.out.println("=======================================");
        System.out.println("(a) Add child");
        System.out.println("(d) Delete child");
        System.out.println("(l) List all children");
        System.out.println("(q) Quit Nappy server");
    }

    public static void addChild() {
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

    public static void deleteChild() {
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


    public static void main(String[] args) {
        server = new NappyServer();
        in = new BufferedReader(new InputStreamReader(System.in));
        while (isRunning) {
            displayMenu();
            processInput();
        }
        server.closeConnections();
    }
}
