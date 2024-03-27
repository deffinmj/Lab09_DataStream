import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamFrame extends JFrame {
    private JTextArea originalTA;
    private JTextArea filteredTA;
    private JTextField searchTF;
    private Path currentFilePath;

    public DataStreamFrame() {
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Data Stream Assignment");
        setLayout(new BorderLayout());

        filteredTA = new JTextArea();
        originalTA = new JTextArea();
        searchTF = new JTextField();
        JButton loadBtn = new JButton("Load File");
        JButton quitBtn = new JButton("Quit");
        JButton searchBtn = new JButton("Search File");

        loadBtn.addActionListener((ActionEvent ae) -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            fileChooser.setFileFilter(filter);

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                currentFilePath = fileChooser.getSelectedFile().toPath();
                try {
                    Stream<String> lines = Files.lines(currentFilePath);
                    originalTA.setText(lines.collect(Collectors.joining("\n")));
                    lines.close();
                    searchBtn.setEnabled(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        quitBtn.addActionListener((ActionEvent ae) -> {
            System.exit(0);
        });


        searchBtn.addActionListener((ActionEvent ae) -> {
            String search = searchTF.getText();
            try {
                Stream<String> lines = Files.lines(currentFilePath);
                Stream<String> filteredLines = lines.filter(line -> line.contains(search));
                filteredTA.setText(filteredLines.collect(Collectors.joining("\n")));
                lines.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error searching the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPnl = new JPanel();
        buttonPnl.add(searchBtn);
        buttonPnl.add(loadBtn);
        buttonPnl.add(quitBtn);

        JPanel textAreaPnl = new JPanel(new GridLayout(1, 2));
        textAreaPnl.add(new JScrollPane(originalTA));
        textAreaPnl.add(new JScrollPane(filteredTA));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Enter string to search: "), BorderLayout.WEST);
        searchPanel.add(searchTF, BorderLayout.CENTER);

        add(searchPanel, BorderLayout.NORTH);
        add(textAreaPnl, BorderLayout.CENTER);
        add(buttonPnl, BorderLayout.SOUTH);

        searchBtn.setEnabled(false);
    }
}