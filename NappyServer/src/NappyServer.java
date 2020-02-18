import Models.Child;
import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.util.LinkedList;

public class NappyServer implements Runnable {
    private DataLayer dl = null;
    private LinkedList<Child> children = null;
    private Socket socket = null;

    public NappyServer(Socket _socket) {
        dl = new DataLayer();
        children = dl.readChildren();
        socket = _socket;
    }

    public void displayChildren() {
        for (Child child : children) {
            System.out.println(child.getId() + ") " + child.getName());
        }
        System.out.println(children.size() + " children in system");
    }

    public void AddChild(String name, Date dob) {
        Child child = new Child(name, dob);
        dl.AddChild(child);
        children = dl.readChildren();
    }

    public Child FindChildInMemory(String name) {
        Child c = null;
        for (Child child : children) {
            if (child.getName().equalsIgnoreCase(name)) {
                return child;
            }
        }
        return null;
    }

    public void DeleteChild(String name) {
        Child c = dl.GetChild(name);

        dl.DeleteChild(c);
        System.out.println(name + " deleted");
        children = dl.readChildren();

    }

    public void closeConnections() {
        dl.close();
    }

    public void run() {
        try {
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            JSONObject outputData = new JSONObject();
            outputData.put("Version", "ChildMinder v0.1 server");

            dout.writeUTF(outputData.toJSONString());
            Thread.sleep(100);
            socket.close();
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        } catch (InterruptedException intex) {
            System.out.println(intex.getMessage());

        }
    }
}
