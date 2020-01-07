import Models.Child;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class DataLayer {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public DataLayer() {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost/nappy?user=&password=");
            statement = connect.createStatement();
        } catch (SQLException sqlex) {
            DisplaySQLException(sqlex);
        }
    }

    public void AddChild(@NotNull Child child) {
        try {
            int result = statement.executeUpdate("INSERT INTO `child` (`id`, `name`, `dob`) VALUES (NULL, '" + child.getName() + "', '" + child.getDob() + "')");
            System.out.println(result + " record added");
        } catch (SQLException sqlex) {
            DisplaySQLException(sqlex);
        }
    }

    public Child GetChild(String name) {
        ArrayList<Child> children = new ArrayList<Child>();
        try {
            String query = "SELECT * FROM `child` WHERE `name` LIKE '" + name + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.first()) {

                return new Child(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("dob"));

            }
        } catch (SQLException sqlex) {
            DisplaySQLException(sqlex);
        }
        return null;
    }

    public void DeleteChild(@NotNull Child child) {
        try {
            int result = statement.executeUpdate("DELETE FROM `child` WHERE `child`.`id` = " + child.getId());
            System.out.println(result + " record deleted");
        } catch (SQLException sqlex) {
            DisplaySQLException(sqlex);
        }
    }

    public LinkedList<Child> readChildren() {
        LinkedList<Child> children = new LinkedList<Child>();
        try {
            resultSet = statement.executeQuery("SELECT * FROM nappy.child");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Date dob = resultSet.getDate("dob");
                children.add(new Child(id, name, dob));
            }
        } catch (SQLException sqlex) {
            DisplaySQLException(sqlex);
        }
        return children;
    }

    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (SQLException sqlex) {
            DisplaySQLException(sqlex);
        }

    }

    public void DisplaySQLException(SQLException sqlex) {
        System.out.println(sqlex.getMessage());
    }
}
