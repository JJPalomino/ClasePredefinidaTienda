import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Tienda extends JFrame {
    private JPanel panel;
    private JTextField nameProducto;
    private JTextField fechaCad;
    private JTextField lote;
    private JTextField codBarras;
    private JButton registrarButton;
    private JTextField precio;
    private JList lista;
    Connection con;
    PreparedStatement ps;
    DefaultListModel mod = new DefaultListModel();
    Statement st;
    ResultSet r;


    public Tienda() {
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) throws RuntimeException {
                try {
                    insertar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    public void listar() throws SQLException {
        conectar();
        lista.setModel(mod);
        st = con.createStatement();
        r = st.executeQuery("SELECT nameProducto, precio, codigoBarras, lote, caducidad FROM tienda2");
        mod.removeAllElements();
        while (r.next()){
            mod.addElement(r.getString(1)+" "+r.getInt(2)+" "+r.getString(3)+
                    " "+ r.getString(4)+" "+r.getString(5));
        }
    }

    public void insertar() throws SQLException {
        conectar();
        ps = con.prepareStatement("INSERT INTO tienda2 (?,?,?,?,?)");
        ps.setString(1,
                nameProducto.getText());
        ps.setInt(2, Integer.parseInt(precio.getText()));
        ps.setString(3,
                codBarras.getText());
        ps.setString(4,
                lote.getText());
        ps.setString(5,
                fechaCad.getText());

        if (ps.executeUpdate()>0) {
            lista.setModel(mod);
            mod.removeAllElements();
            mod.addElement("Ingreso Exitoso");

            nameProducto.setText("");
            precio.setText("");
            codBarras.setText("");
            lote.setText("");
            fechaCad.setText("");
        }
    }

    public static void main(String[] args) {
        Tienda t = new Tienda();
        t.setContentPane(new Tienda().panel);
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        t.setVisible(true);
        t.pack();
    }

    public void conectar() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/tienda2", "palomino", "root123");
            System.out.println("Conección exitosa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

