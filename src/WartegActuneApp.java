import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

public class WartegActuneApp extends JFrame {
    private Map<Integer, Map<String, Object>> menuMakananMap;
    private Map<Integer, Map<String, Object>> menuMinumanMap;
    private Map<Integer, Integer> pesanan;
    private JTextField namaField;
    private JTextArea pesananTextArea;
    private JLabel totalHargaLabel;
    private JTextField jumlahBayarField;
    private JCheckBox cetakOtomatisCheckbox;
    private JPanel menuPanel;

    public WartegActuneApp() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Warteg Actune Self-Service");
        setPreferredSize(new Dimension(800, 600));

        menuMakananMap = new HashMap<>();
        menuMakananMap.put(1, createMenuItem("Nasi Putih", 5000));
        menuMakananMap.put(2, createMenuItem("Sayur Asem", 3000));
        menuMakananMap.put(3, createMenuItem("Ayam Goreng", 10000));
        menuMakananMap.put(4, createMenuItem("Tempe Goreng", 5000));
        menuMakananMap.put(5, createMenuItem("Sambal", 2000));
        menuMakananMap.put(6, createMenuItem("Telur Dadar", 3000));
        menuMakananMap.put(7, createMenuItem("Telur Balado", 5000));
        menuMakananMap.put(8, createMenuItem("Cah Kangkung", 4000));
        menuMakananMap.put(9, createMenuItem("Ikan Goreng", 8000));
        menuMakananMap.put(10, createMenuItem("Nasi Uduk", 6000));
        menuMakananMap.put(11, createMenuItem("Mie Goreng", 7000));
        menuMakananMap.put(12, createMenuItem("Kangkung Goreng", 4000));
        menuMakananMap.put(13, createMenuItem("Tahu Bacem", 3000));

        menuMinumanMap = new HashMap<>();
        menuMinumanMap.put(14, createMenuItem("Es Teh", 3000));
        menuMinumanMap.put(15, createMenuItem("Es Jeruk", 3000));
        menuMinumanMap.put(16, createMenuItem("Teh Panas", 3000));
        menuMinumanMap.put(17, createMenuItem("Kopi Hitam", 3000));
        menuMinumanMap.put(18, createMenuItem("Air Putih", 2000));

        pesanan = new HashMap<>();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel menuLabel = new JLabel("Selamat datang di Warteg Actune Self-Service!");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 16));
        menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(menuLabel, BorderLayout.NORTH);

        JPanel namaPanel = new JPanel();
        namaPanel.setLayout(new FlowLayout());
        JLabel namaLabel = new JLabel("Nama Pelanggan:");
        namaField = new JTextField(20);
        namaPanel.add(namaLabel);
        namaPanel.add(namaField);
        mainPanel.add(namaPanel, BorderLayout.NORTH);

        JPanel menuBarPanel = new JPanel();
        menuBarPanel.setLayout(new FlowLayout());

        JButton makananButton = new JButton("Makanan");
        makananButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMakananMenu();
            }
        });
        menuBarPanel.add(makananButton);

        JButton minumanButton = new JButton("Minuman");
        minumanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMinumanMenu();
            }
        });
        menuBarPanel.add(minumanButton);

        mainPanel.add(menuBarPanel, BorderLayout.NORTH);

        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5, 3, 5, 5));
        mainPanel.add(menuPanel, BorderLayout.WEST);

        showMakananMenu();

        JPanel pesananPanel = new JPanel();
        pesananPanel.setLayout(new BorderLayout());
        JLabel pesananLabel = new JLabel("Pesanan Anda:");
        pesananLabel.setFont(new Font("Arial", Font.BOLD, 14));
        pesananLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pesananPanel.add(pesananLabel, BorderLayout.NORTH);

        pesananTextArea = new JTextArea(10, 40);
        pesananTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(pesananTextArea);
        pesananPanel.add(scrollPane, BorderLayout.CENTER);

        totalHargaLabel = new JLabel("Total Harga: Rp0");
        totalHargaLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        totalHargaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pesananPanel.add(totalHargaLabel, BorderLayout.SOUTH);

        mainPanel.add(pesananPanel, BorderLayout.CENTER);

        JPanel bayarPanel = new JPanel();
        bayarPanel.setLayout(new FlowLayout());
        JLabel jumlahBayarLabel = new JLabel("Jumlah Bayar:");
        jumlahBayarField = new JTextField(10);
        JButton bayarButton = new JButton("Bayar");
        bayarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bayar();
            }
        });
        cetakOtomatisCheckbox = new JCheckBox("Cetak Otomatis");
        cetakOtomatisCheckbox.setSelected(true);
        bayarPanel.add(jumlahBayarLabel);
        bayarPanel.add(jumlahBayarField);
        bayarPanel.add(bayarButton);
        bayarPanel.add(cetakOtomatisCheckbox);
        mainPanel.add(bayarPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void showMakananMenu() {
        menuPanel.removeAll();
        JLabel makananLabel = new JLabel("Menu Makanan");
        makananLabel.setFont(new Font("Arial", Font.BOLD, 14));
        makananLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuPanel.add(makananLabel);
        for (Map.Entry<Integer, Map<String, Object>> entry : menuMakananMap.entrySet()) {
            int menuId = entry.getKey();
            String menuNama = (String) entry.getValue().get("nama");
            int menuHarga = (int) entry.getValue().get("harga");
    
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new FlowLayout());
    
            JButton minusButton = new JButton("-");
            minusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    kurangiPesanan(menuId);
                }
            });
            itemPanel.add(minusButton);
    
            JLabel menuLabel = new JLabel(menuNama + " (Rp" + menuHarga + ")");
            itemPanel.add(menuLabel);
    
            JButton plusButton = new JButton("+");
            plusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tambahPesanan(menuId);
                }
            });
            itemPanel.add(plusButton);
    
            menuPanel.add(itemPanel);
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private void showMinumanMenu() {
        menuPanel.removeAll();
        JLabel minumanLabel = new JLabel("Menu Minuman");
        minumanLabel.setFont(new Font("Arial", Font.BOLD, 14));
        minumanLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuPanel.add(minumanLabel);
        for (Map.Entry<Integer, Map<String, Object>> entry : menuMinumanMap.entrySet()) {
            int menuId = entry.getKey();
            String menuNama = (String) entry.getValue().get("nama");
            int menuHarga = (int) entry.getValue().get("harga");
    
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new FlowLayout());
    
            JButton minusButton = new JButton("-");
            minusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    kurangiPesanan(menuId);
                }
            });
            itemPanel.add(minusButton);
    
            JLabel menuLabel = new JLabel(menuNama + " (Rp" + menuHarga + ")");
            itemPanel.add(menuLabel);
    
            JButton plusButton = new JButton("+");
            plusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tambahPesanan(menuId);
                }
            });
            itemPanel.add(plusButton);
    
            menuPanel.add(itemPanel);
        }
        menuPanel.revalidate();
        menuPanel.repaint();
    }    
    
    private Map<String, Object> createMenuItem(String nama, int harga) {
        Map<String, Object> menuItem = new HashMap<>();
        menuItem.put("nama", nama);
        menuItem.put("harga", harga);
        return menuItem;
    }
    
    private void tambahPesanan(int menuId) {
        pesanan.put(menuId, pesanan.getOrDefault(menuId, 0) + 1);
        updatePesananTextArea();
    }
    
    private void kurangiPesanan(int menuId) {
        if (pesanan.containsKey(menuId)) {
            int jumlah = pesanan.get(menuId);
            if (jumlah > 1) {
                pesanan.put(menuId, jumlah - 1);
            } else {
                pesanan.remove(menuId);
            }
            updatePesananTextArea();
        }
    }
    
    private void updatePesananTextArea() {
        pesananTextArea.setText("");
    
        int totalHarga = 0;
        for (Map.Entry<Integer, Integer> entry : pesanan.entrySet()) {
            int menuId = entry.getKey();
            int jumlahMenu = entry.getValue();
    
            Map<String, Object> menuInfo = getMenuInfo(menuId);
            String namaMenu = (String) menuInfo.get("nama");
            int hargaMenu = (int) menuInfo.get("harga");
    
            int subtotal = hargaMenu * jumlahMenu;
            totalHarga += subtotal;
    
            pesananTextArea.append(jumlahMenu + " x " + namaMenu + "\t\tRp" + subtotal + "\n");
        }
    
        totalHargaLabel.setText("Total Harga: Rp" + totalHarga);
    }
    
    private Map<String, Object> getMenuInfo(int menuId) {
        if (menuMakananMap.containsKey(menuId)) {
            return menuMakananMap.get(menuId);
        } else if (menuMinumanMap.containsKey(menuId)) {
            return menuMinumanMap.get(menuId);
        }
        return null;
    }
    
    private void bayar() {
        int totalHarga = 0;
        for (Map.Entry<Integer, Integer> entry : pesanan.entrySet()) {
            int menuId = entry.getKey();
            int jumlahMenu = entry.getValue();
    
            Map<String, Object> menuInfo = getMenuInfo(menuId);
            int hargaMenu = (int) menuInfo.get("harga");
    
            totalHarga += hargaMenu * jumlahMenu;
        }
    
        if (totalHarga == 0) {
            JOptionPane.showMessageDialog(this, "Mohon tambahkan pesanan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int jumlahBayar = Integer.parseInt(jumlahBayarField.getText());
    
        if (jumlahBayar < totalHarga) {
            JOptionPane.showMessageDialog(this, "Jumlah bayar tidak mencukupi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int kembalian = jumlahBayar - totalHarga;
    
        String namaPelanggan = namaField.getText();
    
        LocalDateTime tanggalWaktu = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = tanggalWaktu.format(formatter);
    
        StringBuilder buktiPembayaran = new StringBuilder();
        buktiPembayaran.append("Warteg Actune\n");
        buktiPembayaran.append("============\n");
        buktiPembayaran.append(formattedDateTime).append("\n");
        buktiPembayaran.append("Nama Pelanggan: ").append(namaPelanggan).append("\n");
        buktiPembayaran.append("============\n");
        buktiPembayaran.append("Pesanan:\n");
    
        for (Map.Entry<Integer, Integer> entry : pesanan.entrySet()) {
            int menuId = entry.getKey();
            int jumlahMenu = entry.getValue();
    
            Map<String, Object> menuInfo = getMenuInfo(menuId);
            String namaMenu = (String) menuInfo.get("nama");
    
            buktiPembayaran.append(jumlahMenu).append("x ").append(namaMenu).append("\n");
        }
    
        buktiPembayaran.append("\nTotal Harga: Rp").append(totalHarga);
        buktiPembayaran.append("\nJumlah Uang: Rp").append(jumlahBayar);
        buktiPembayaran.append("\nKembalian: Rp").append(kembalian).append("\n");
        buktiPembayaran.append("============\n");
        buktiPembayaran.append("Terimakasih telah makan di Warteg Actune\n");
    
        JOptionPane.showMessageDialog(this, buktiPembayaran.toString(), "Bukti Pembayaran", JOptionPane.INFORMATION_MESSAGE);
    
        if (cetakOtomatisCheckbox.isSelected()) {
            cetakBuktiPembayaran(buktiPembayaran.toString());
        }
    
        simpanDataPembelian(formattedDateTime, namaPelanggan, totalHarga, pesanan);
    
        pesanan.clear();
        updatePesananTextArea();
        namaField.setText("");
        jumlahBayarField.setText("");
    }
    
    private void cetakBuktiPembayaran(String buktiPembayaran) {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(new Printable() {
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            String[] lines = buktiPembayaran.split("\n");
            int y = 100;
            for (String line : lines) {
                g2d.drawString(line, 100, y);
                y += 20;
            }

            return Printable.PAGE_EXISTS;
        }
    });
    boolean doPrint = job.printDialog();
    if (doPrint) {
        try {
            job.print();
            JOptionPane.showMessageDialog(this, "Bukti pembayaran berhasil dicetak.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencetak bukti pembayaran.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    private void simpanDataPembelian(String tanggal, String namaPelanggan, int totalHarga, Map<Integer, Integer> pesanan) {
        try {
        FileWriter writer = new FileWriter("riwayat_pembelian.csv", true);
        writer.write(tanggal + "," + namaPelanggan + "," + totalHarga + "," + pesanan.toString() + "\n");
        writer.close();
        } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data pembelian.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new WartegActuneApp().setVisible(true);
                }
            });
        }
    }
    
