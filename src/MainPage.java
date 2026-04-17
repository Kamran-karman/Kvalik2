import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;

public class MainPage {
    private JTable table1;
    private JButton butAdd;
    private JButton butClear;
    private JButton butDel;
    private JPanel panel1;
    private JScrollPane scrollPane;
    private JButton butSave;
    private TimeValidatingTableModel tableModel; // Используем кастомную модель
    private final String dataFilePath = "table_data.txt"; // Файл для сохранения данных

    public MainPage() {
        butAdd.setBackground(new Color(76, 175, 80)); // Зелёный
        butAdd.setForeground(Color.WHITE); // Белый текст
        butAdd.setFocusPainted(false); // Убираем рамку фокуса
        butClear.setBackground(new Color(33, 150, 243)); // Синий
        butClear.setForeground(Color.WHITE); // Белый текст
        butClear.setFocusPainted(false);
        butDel.setBackground(new Color(244, 67, 54)); // Красный
        butDel.setForeground(Color.WHITE); // Белый текст
        butClear.setFocusPainted(false);
        butSave.setBackground(new Color(250, 200, 20));
        butAdd.setForeground(Color.WHITE); // Белый текст
        butAdd.setFocusPainted(false); // Убираем рамку фокуса

        configureScrollBar();
        setupTable();
        addActionListeners();
        setupKeyBindings();
        loadDataFromFile();
    }

    private void configureScrollBar() {
        // Устанавливаем серый цвет фона скроллбара
        scrollPane.getVerticalScrollBar().setBackground(Color.LIGHT_GRAY);
        scrollPane.getHorizontalScrollBar().setBackground(Color.LIGHT_GRAY);

        // Настраиваем внешний вид скроллбара через BasicScrollBarUI
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                // Цвет «ползунка» (thumb) — тёмно‑серый
                this.thumbColor = new Color(100, 100, 100);
                // Цвет дорожки (track) — светло‑серый
                this.trackColor = Color.LIGHT_GRAY;
            }
        });

        scrollPane.getHorizontalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                // Цвет «ползунка» для горизонтального скролла
                this.thumbColor = new Color(100, 100, 100);
                // Цвет дорожки для горизонтального скролла
                this.trackColor = Color.LIGHT_GRAY;
            }
        });
    }

    private void setupTable() {
        // Создаём модель таблицы с двумя колонками
        tableModel = new TimeValidatingTableModel(new Object[]{"N", "Время", "Получившееся число"}, 0);
        table1.setModel(tableModel);
    }

    private void addActionListeners() {
        butAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmptyRow();
            }
        });

        butClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSelectedRow();
            }
        });

        butDel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });

        butSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDataToFile();
            }
        });
    }

    // Добавление пустой строки в таблицу
    private void addEmptyRow() {
        tableModel.addRow(new Object[]{"", "", ""});

    }

    private void clearSelectedRow() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                tableModel.setValueAt("", selectedRow, i);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Пожалуйста, выберите строку для очистки.");
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(null, "Пожалуйста, выберите строку для удаления.");
        }
    }

    private void setupKeyBindings() {
        // Получаем InputMap и ActionMap для таблицы
        InputMap inputMap = table1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = table1.getActionMap();

        // 1. Ctrl + N → butAdd (добавление строки)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "addRow");
        actionMap.put("addRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmptyRow();
            }
        });

        // 2. Backspace → butClear (очистка строки)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "clearRow");
        actionMap.put("clearRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearSelectedRow();
            }
        });

        // 3. Delete → butDel (удаление строки)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");
        actionMap.put("deleteRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }

    private void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
            // Записываем заголовки столбцов
            writer.write("N;Время;Получившееся число");
            writer.newLine();

            // Записываем данные таблицы
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String nValue = tableModel.getValueAt(i, 0).toString();
                String timeValue = tableModel.getValueAt(i, 1).toString();
                String digValue = tableModel.getValueAt(i, 2).toString();
                writer.write(nValue + ";" + timeValue + ";" + digValue);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка сохранения данных: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromFile() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            return; // Файл не существует — ничего не делаем
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFilePath))) {
            String line;
            boolean firstLine = true; // Пропускаем первую строку (заголовки)

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length == 3) {
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].trim();
                    }

                    // Дополнительная проверка данных
                    if (!parts[0].isEmpty() || !parts[1].isEmpty() || !parts[2].isEmpty()) {
                        tableModel.addRow(parts);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка загрузки данных: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private static class TimeValidatingTableModel extends DefaultTableModel {
        public TimeValidatingTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            // Валидируем только столбец «Время» (индекс 1)
            if (column == 1) {
                String timeStr = (value != null) ? value.toString().trim() : "";

                // Проверка на пустой ввод
                if (timeStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Время не может быть пустым!",
                            "Ошибка ввода",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Проверка на отрицательный ввод
                try {
                    double timeValue = Double.parseDouble(timeStr);
                    if (timeValue < 0) {
                        JOptionPane.showMessageDialog(null,
                                "Время не может быть отрицательным!",
                                "Ошибка ввода",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Время должно быть числом!",
                            "Ошибка ввода",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Если валидация пройдена, устанавливаем значение
            super.setValueAt(value, row, column);
        }
    }
}
