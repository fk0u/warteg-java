import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RiwayatPembelianApp extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalPenghasilanLabel;

    public RiwayatPembelianApp() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Riwayat Pembelian Warteg Actune");
        setPreferredSize(new Dimension(600, 400));

        String[] columnNames = {"Tanggal", "Nama Pelanggan", "Total Harga"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showDetail();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        totalPenghasilanLabel = new JLabel("Total Penghasilan: Rp0");
        totalPenghasilanLabel.setFont(new Font("Arial", Font.BOLD, 12));
        totalPenghasilanLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(totalPenghasilanLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("riwayat_pembelian.csv"))) {
            String line;
            int totalPenghasilan = 0;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String tanggal = data[0];
                String namaPelanggan = data[1];
                int totalHarga = Integer.parseInt(data[2]);

                tableModel.addRow(new Object[]{tanggal, namaPelanggan, totalHarga});

                totalPenghasilan += totalHarga;
            }

            totalPenghasilanLabel.setText("Total Penghasilan: Rp" + totalPenghasilan);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String tanggal = (String) table.getValueAt(selectedRow, 0);
            String namaPelanggan = (String) table.getValueAt(selectedRow, 1);

            JTextArea detailTextArea = new JTextArea();
            detailTextArea.setEditable(false);

            try (BufferedReader reader = new BufferedReader(new FileReader("riwayat_pembelian.csv"))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equals(tanggal) && data[1].equals(namaPelanggan)) {
                        detailTextArea.append("Tanggal: " + data[0] + "\n");
                        detailTextArea.append("Nama Pelanggan: " + data[1] + "\n");
                        detailTextArea.append("Total Harga: Rp" + data[2] + "\n");
                        detailTextArea.append("Pesanan:\n");

                        String pesanan = data[3].substring(1, data[3].length() - 1);
                        String[] pesananItems = pesanan.split(", ");
                        for (String item : pesananItems) {
                            String[] itemData = item.split("=");
                            String menuId = itemData[0];
                            String menuInfo = itemData[1].substring(1, itemData[1].length() - 1);
                            String[] menuData = menuInfo.split(", ");
                            String namaMenu = menuData[0].split("=")[1];
                            int jumlahMenu = Integer.parseInt(menuData[2].split("=")[1]);
                            detailTextArea.append(jumlahMenu + "x " + namaMenu + "\n");
                        }

                        break;
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat detail pembelian.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(detailTextArea), "Detail Pembelian - " + namaPelanggan, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RiwayatPembelianApp().setVisible(true);
            }
        });
    }
}
