package Controllers;

import DatabaseConnection.SQLConnector;
import Forms.Components.ViewTabel;
import Model.Client;
import Model.OrderForClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Danh Thao
 */
public class ClientController {

    public static ClientController instance;
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    private static boolean isInitiallized = false;

    private final String loadDataClientSQL = "SELECT * FROM Clients";

    private final String insertClientSql = "INSERT INTO Clients(nameClient,telClient"
            + ",addressClient ,gmailClient  ) VALUES(?,?,?,?)";

    private final String deleteClienByTelltSql = "DELETE FROM Clients WHERE  telClient=?";

    private final String deleteClientOnBill = "DELETE FROM OrderDetails WHERE idOrder=?"
            + "DELETE FROM Orders WHERE idClient=?"
            + "DELETE FROM Clients WHERE telClient=?";

    private final String updateClientSql = "UPDATE Clients SET nameClient=?,telClient=?,"
            + "gmailClient=? , addressClient =?  WHERE idClient=?";

    private final String findIdOrder = "SELECT idOrder FROM OrderDetails";
    private final String findIdOrderOfOrder = "SELECT idOrder FROM Orders";
    private final String viewOrderOfClient = "SELECT nameClient, telClient, namePhone, brandPhone, pricePhone, quantity "
            + "FROM Orders inner join Clients on Orders.idClient=Clients.idClient "
            + "inner join OrderDetails on Orders.idOrder=OrderDetails.idOrder "
            + "inner join Phones on Phones.idPhone=OrderDetails.idPhone "
            + "WHERE Clients.idClient=?";
    private final String delOrderOfOrder = "DELETE FROM Orders WHERE idOrder=?"
            + "DELETE FROM Clients WHERE telClient=?";

    private ViewTabel viewTabel = new ViewTabel();

    public static void init() {
        if (isInitiallized == true) {
            return;
        }
        instance = new ClientController();
        isInitiallized = true;
    }

    private void setupDatabaseCommand(String sql) throws SQLException {
        try {
            SQLConnector.getForName();
            conn = SQLConnector.getConnection();
            ps = conn.prepareStatement(sql);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PhoneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<Client> loadDataAccounts() {
        List<Client> li = new ArrayList<Client>();
        try {
            setupDatabaseCommand(loadDataClientSQL);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idClient");
                String name = rs.getString("nameClient");
                String phone = rs.getString("telClient");
                String address = rs.getString("addressClient");
                String gmail = rs.getString("gmailClient");
                Client _client = new Client(id, name, phone, address, gmail);
                li.add(_client);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unexpected error: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return li;
    }

    public List<OrderForClient> viewOrderOfClient(int id) {
        List<OrderForClient> li = new ArrayList<>();

        try {
            setupDatabaseCommand(viewOrderOfClient);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                String nameClient = rs.getString("nameClient");
                String telClient = rs.getString("telClient");
                String namePhone = rs.getString("namePhone");
                String brandPhone = rs.getString("brandPhone");
                double pricePhone = rs.getDouble("pricePhone");
                int quantity = rs.getInt("quantity");

                OrderForClient e = new OrderForClient(nameClient, telClient, namePhone, brandPhone, pricePhone, quantity);
                li.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return li;
    }

    public void addClient(Client cl) throws SQLException {
        try {
            setupDatabaseCommand(insertClientSql);

            ps.setString(1, cl.getNameClient());
            ps.setString(2, cl.getTelClient());
            ps.setString(3, cl.getAddressClient());
            ps.setString(4, cl.getGmailClient());

            int rss = ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CAN NOT ADD THIS CLIENT");
        }
    }

    public void delClient(String telephone) {
        try {
            setupDatabaseCommand(deleteClienByTelltSql);
            ps.setString(1, telephone);

            int rss = ps.executeUpdate();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, "CAN NOT DELETE THIS CLIENT");
        }
    }

    public void delClientOnOrder(int id, String phone) {
        try {
            setupDatabaseCommand(delOrderOfOrder);
            ps.setInt(1, id);
            ps.setString(2, phone);

            int rss = ps.executeUpdate();

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, "CAN NOT DELETE THIS CLIENT");
        }
    }

    public void delClientOnBill(int idOr, int id, String phone) {
        try {
            setupDatabaseCommand(deleteClientOnBill);
            ps.setInt(1, idOr);
            ps.setInt(2, id);
            ps.setString(3, phone);
            int rss = ps.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "CAN NOT DELETE THIS CLIENT");
        }
    }

    public int findIdOrder() throws SQLException {
        setupDatabaseCommand(findIdOrder);
        int id = -1;
        rs = ps.executeQuery();
        while (rs.next()) {
            id = rs.getInt("idOrder");
        }
        return id;
    }

    public int findIdOrderOfOrder() throws SQLException {
        setupDatabaseCommand(findIdOrderOfOrder);
        int id = -1;
        rs = ps.executeQuery();
        while (rs.next()) {
            id = rs.getInt("idOrder");
        }
        return id;
    }

    public void updateClient(Client cl) {
        try {
            setupDatabaseCommand(updateClientSql);
            ps.setString(1, cl.getNameClient());
            ps.setString(2, cl.getTelClient());
            ps.setString(3, cl.getGmailClient());
            ps.setString(4, cl.getAddressClient());
            ps.setInt(5, cl.getIdClient());

            int rss = ps.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "CAN NOT UPDATE THIS CLIENT");
        }
    }

}
