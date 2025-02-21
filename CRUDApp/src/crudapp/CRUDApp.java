import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Date;

public class CRUDApp {
    public static void main(String[] args) {
        // Frame utama
        JFrame frame = new JFrame("Simple Cashier Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE, 2), "Input Item", 0, 0, new Font("Arial", Font.BOLD, 14), Color.BLUE));
        inputPanel.setBackground(new Color(230, 240, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel itemLabel = new JLabel("Item:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel quantityLabel = new JLabel("Quantity:");

        JTextField itemField = new JTextField(15);
        JTextField priceField = new JTextField(10);
        JTextField quantityField = new JTextField(5);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear All");
        JButton printButton = new JButton("Print Receipt"); // Tombol baru untuk cetak struk

        addButton.setBackground(Color.GREEN);
        updateButton.setBackground(Color.ORANGE);
        deleteButton.setBackground(Color.RED);
        clearButton.setBackground(Color.LIGHT_GRAY);
        printButton.setBackground(Color.CYAN);

        addButton.setForeground(Color.WHITE);
        updateButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        clearButton.setForeground(Color.BLACK);
        printButton.setForeground(Color.BLACK);

        // Tambahkan komponen input
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(itemLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(itemField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputPanel.add(priceLabel, gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputPanel.add(priceField, gbc);
        gbc.gridx = 4; gbc.gridy = 0; inputPanel.add(quantityLabel, gbc);
        gbc.gridx = 5; gbc.gridy = 0; inputPanel.add(quantityField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(addButton, gbc);
        gbc.gridx = 1; inputPanel.add(updateButton, gbc);
        gbc.gridx = 2; inputPanel.add(deleteButton, gbc);
        gbc.gridx = 3; inputPanel.add(clearButton, gbc);
        gbc.gridx = 4; inputPanel.add(printButton, gbc);

        // Tabel untuk Daftar Belanja
        DefaultTableModel model = new DefaultTableModel(new String[]{"Item", "Price", "Quantity", "Total"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel Total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel("Total: ");
        JLabel totalAmountLabel = new JLabel("0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalPanel.add(totalLabel);
        totalPanel.add(totalAmountLabel);

        // Fungsi untuk menghitung total
        DecimalFormat df = new DecimalFormat("0.00");

        addButton.addActionListener(e -> {
            String item = itemField.getText();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();

            if (item.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);
                double total = price * quantity;

                model.addRow(new Object[]{item, df.format(price), quantity, df.format(total)});

                double currentTotal = Double.parseDouble(totalAmountLabel.getText());
                totalAmountLabel.setText(df.format(currentTotal + total));

                itemField.setText("");
                priceField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Price and Quantity must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        updateButton.addActionListener(e -> {
    int selectedRow = table.getSelectedRow();
    if (selectedRow != -1) {
        String item = itemField.getText();
        String priceText = priceField.getText();
        String quantityText = quantityField.getText();

        if (item.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            double total = price * quantity;

            // Update the table
            model.setValueAt(item, selectedRow, 0);
            model.setValueAt(df.format(price), selectedRow, 1);
            model.setValueAt(quantity, selectedRow, 2);
            model.setValueAt(df.format(total), selectedRow, 3);

            // Recalculate the total amount
            double newTotal = 0;
            for (int i = 0; i < model.getRowCount(); i++) {
                newTotal += Double.parseDouble(model.getValueAt(i, 3).toString());
            }
            totalAmountLabel.setText(df.format(newTotal));

            itemField.setText("");
            priceField.setText("");
            quantityField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Price and Quantity must be numeric.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(frame, "Please select a row to update.", "Error", JOptionPane.WARNING_MESSAGE);
    }
});


        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                double rowTotal = Double.parseDouble(model.getValueAt(selectedRow, 3).toString());
                double currentTotal = Double.parseDouble(totalAmountLabel.getText());
                totalAmountLabel.setText(df.format(currentTotal - rowTotal));

                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> {
            model.setRowCount(0);
            totalAmountLabel.setText("0.00");
        });

        printButton.addActionListener(e -> {
            StringBuilder receipt = new StringBuilder();
            receipt.append("----------- RECEIPT -----------\n");
            receipt.append("Date: ").append(new Date()).append("\n\n");
            receipt.append(String.format("%-20s %-10s %-10s %-10s\n", "Item", "Price", "Qty", "Total"));
            receipt.append("--------------------------------\n");

            for (int i = 0; i < model.getRowCount(); i++) {
                String item = model.getValueAt(i, 0).toString();
                String price = model.getValueAt(i, 1).toString();
                String quantity = model.getValueAt(i, 2).toString();
                String total = model.getValueAt(i, 3).toString();
                receipt.append(String.format("%-20s %-10s %-10s %-10s\n", item, price, quantity, total));
            }

            receipt.append("\n--------------------------------\n");
            receipt.append("Total: ").append(totalAmountLabel.getText()).append("\n");
            receipt.append("----------- THANK YOU -----------");

            JTextArea textArea = new JTextArea(receipt.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane1 = new JScrollPane(textArea);
            scrollPane1.setPreferredSize(new Dimension(400, 500));

            JOptionPane.showMessageDialog(frame, scrollPane1, "Receipt", JOptionPane.INFORMATION_MESSAGE);
        });

        // Tambahkan Komponen ke Frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(totalPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
