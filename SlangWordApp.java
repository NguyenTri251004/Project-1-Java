package Project;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SlangWordApp extends JFrame {
	private static final long serialVersionUID = 1L;
	private SlangWordDictionary dic;
	private String question = "";
	private String[] options = new String[4];

	public SlangWordApp() {
		dic = new SlangWordDictionary();
		dic.loadFromFile("slang.txt", SlangWordDictionary.getDictionary());

		setTitle("Slang Word Dictionary");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Tab panel
		JTabbedPane tabbedPane = new JTabbedPane();

		// Tab 1: Search
		tabbedPane.add("Search", createSearchPanel());

		// Tab 2: Lịch sử tìm kiếm
		tabbedPane.add("Lịch sử tìm kiếm", createHistoryPanel());

		// Tab 3: Thêm Slang Word mới
		tabbedPane.add("Thêm", createAddSlangPanel());

		// Tab 4: Sửa Slang Word
		tabbedPane.add("Sửa", createEditSlangPanel());

		// Tab 5: Xóa Slang Word
		tabbedPane.add("Xóa", createDeleteSlangPanel());

		// Tab 6: Reset danh sách
		tabbedPane.add("Reset", createResetPanel());

		// Tab 7: Random Slang Word
		tabbedPane.add("Random", createRandomPanel());

		// Tab 8: Đố vui - Slang Word
//		tabbedPane.add("Đố vui Slang Word", createQuizSlangPanel());
//
//		// Tab 10: Đố vui - Definition
//		tabbedPane.add("Đố vui Definition", createQuizDefinitionPanel());

		add(tabbedPane, BorderLayout.CENTER);
	}

	private JPanel createSearchPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JLabel title = new JLabel("Tìm kiếm Slang Word và định nghĩa", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 18));
		panel.add(title, BorderLayout.NORTH);
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(2, 1, 10, 10));
		JPanel slangSearchPanel = new JPanel(new BorderLayout());
		JLabel slangLabel = new JLabel("Tìm kiếm Slang Word:");
		JTextField slangInput = new JTextField();
		slangInput.setPreferredSize(new Dimension(200, 25));
		JButton slangSearchButton = new JButton("Search");
		JTextArea slangResultArea = new JTextArea(10, 30);
		slangResultArea.setEditable(false);

		slangSearchPanel.add(slangLabel, BorderLayout.WEST);
		slangSearchPanel.add(slangInput, BorderLayout.CENTER);
		slangSearchPanel.add(slangSearchButton, BorderLayout.EAST);

		ActionListener slangSearchAction = e -> {
			String slangWord = slangInput.getText().trim();
			if (!slangWord.isEmpty()) {
				String result = dic.searchSlangWord(slangWord);
				slangResultArea.setText(result);
			} else {
				slangResultArea.setText("Nhập Slang Word mà bạn muốn tìm kiếm.");
			}
		};
		slangSearchButton.addActionListener(slangSearchAction);
		slangInput.addActionListener(slangSearchAction);

		JPanel definitionSearchPanel = new JPanel(new BorderLayout());
		JLabel defLabel = new JLabel("Tìm kiếm định nghĩa:");
		JTextField defInput = new JTextField();
		defInput.setPreferredSize(new Dimension(200, 25));
		JButton defSearchButton = new JButton("Search");
		JTextArea defResultArea = new JTextArea(10, 30);
		defResultArea.setEditable(false);

		definitionSearchPanel.add(defLabel, BorderLayout.WEST);
		definitionSearchPanel.add(defInput, BorderLayout.CENTER);
		definitionSearchPanel.add(defSearchButton, BorderLayout.EAST);

		ActionListener defSearchAction = e -> {
			String definition = defInput.getText().trim();
			if (!definition.isEmpty()) {
				String results = SlangWordDictionary.searchDefinition(definition);
				defResultArea.setText(results);
			} else {
				defResultArea.setText("Nhập định nghĩa mà bạn muốn tìm kiếm.");
			}
		};
		defSearchButton.addActionListener(defSearchAction);
		defInput.addActionListener(defSearchAction);

		inputPanel.add(slangSearchPanel);
		inputPanel.add(definitionSearchPanel);

		panel.add(inputPanel, BorderLayout.CENTER);

		JPanel resultPanel = new JPanel(new GridLayout(2, 1));
		resultPanel.add(new JScrollPane(slangResultArea));
		resultPanel.add(new JScrollPane(defResultArea));
		panel.add(resultPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel createHistoryPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JTextArea historyArea = new JTextArea(10, 30);
		historyArea.setEditable(false);

		JButton refreshButton = new JButton("Làm mới");

		refreshButton.addActionListener(e -> {
			StringBuilder historyContent = new StringBuilder();
			historyContent.append("Lịch sử tìm kiếm của bạn:\n");

			if (SlangWordDictionary.getSearchHistory().isEmpty()) {
				historyContent.append("Chưa có tìm kiếm SlangWord nào!");
			} else {
				for (String slang : SlangWordDictionary.getSearchHistory()) {
					historyContent.append("- ").append(slang).append("\n");
				}
			}
			historyArea.setText(historyContent.toString());
		});
		panel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
		panel.add(refreshButton, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel createAddSlangPanel() {
		JPanel panel = new JPanel(new GridLayout(4, 1));

		JLabel slangLabel = new JLabel("Nhập Slang Word:");
		JTextField slangField = new JTextField(20);
		JLabel meaningLabel = new JLabel("Nhập nghĩa:");
		JTextField meaningField = new JTextField(20);

		JButton addButton = new JButton("Thêm Slang Word");

		JTextArea resultArea = new JTextArea(5, 30);
		resultArea.setEditable(false);

		addButton.addActionListener(e -> {
			String slang = slangField.getText().trim();
			String meaning = meaningField.getText().trim();

			if (slang.isEmpty() || meaning.isEmpty()) {
				resultArea.setText("Vui lòng nhập đầy đủ Slang Word và nghĩa.");
				return;
			}

			if (SlangWordDictionary.getDictionary().containsKey(slang)) {
				int choice = JOptionPane.showConfirmDialog(null,
						slang + " đã tồn tại với các nghĩa: " + SlangWordDictionary.getDictionary().get(slang)
								+ "\nBạn có muốn ghi đè (Yes) hay thêm nghĩa mới (No)?",
						"Xác nhận", JOptionPane.YES_NO_OPTION);

				if (choice == JOptionPane.YES_OPTION) {
					HashSet<String> newDefinitions = new HashSet<>();
					newDefinitions.add(meaning);
					SlangWordDictionary.getDictionary().put(slang, newDefinitions);
					resultArea.setText("Đã ghi đè: " + slang + " với nghĩa mới: " + meaning);
				} else if (choice == JOptionPane.NO_OPTION) {
					SlangWordDictionary.getDictionary().get(slang).add(meaning);
					resultArea.setText("Đã thêm nghĩa mới vào: " + slang);
				}
			} else {
				HashSet<String> definitions = new HashSet<>();
				definitions.add(meaning);
				SlangWordDictionary.getDictionary().put(slang, definitions);
				resultArea.setText("Đã thêm mới: " + slang + " với nghĩa: " + meaning);
			}

			slangField.setText("");
			meaningField.setText("");
		});

		panel.add(slangLabel);
		panel.add(slangField);
		panel.add(meaningLabel);
		panel.add(meaningField);
		panel.add(addButton);
		panel.add(new JScrollPane(resultArea));

		return panel;
	}

	private JPanel createEditSlangPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		JLabel slangLabel = new JLabel("Nhập Slang Word cần chỉnh sửa:");
		slangLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JTextField slangField = new JTextField(20);
		slangField.setMaximumSize(slangField.getPreferredSize());

		JLabel meaningLabel = new JLabel("Chọn nghĩa cần chỉnh sửa:");
		meaningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JTextArea meaningArea = new JTextArea(5, 30);
		meaningArea.setEditable(false);
		JScrollPane meaningScroll = new JScrollPane(meaningArea);

		JButton editButton = new JButton("Chỉnh sửa nghĩa");
		editButton.setPreferredSize(new Dimension(150, 30));
		editButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		slangField.addActionListener(e -> {
			String slangWord = slangField.getText().trim();
			if (!slangWord.isEmpty()) {
				if (SlangWordDictionary.getDictionary().containsKey(slangWord)) {
					HashSet<String> definitions = SlangWordDictionary.getDictionary().get(slangWord);
					StringBuilder sb = new StringBuilder();
					int index = 1;
					for (String def : definitions) {
						sb.append(index).append(". ").append(def).append("\n");
						index++;
					}
					meaningArea.setText(sb.toString());
				} else {
					meaningArea.setText("Slang word không tồn tại.");
				}
			} else {
				meaningArea.setText("Vui lòng nhập Slang word.");
			}
		});

		editButton.addActionListener(e -> {
			String slangWord = slangField.getText().trim();
			if (!slangWord.isEmpty()) {
				if (SlangWordDictionary.getDictionary().containsKey(slangWord)) {
					HashSet<String> definitions = SlangWordDictionary.getDictionary().get(slangWord);
					ArrayList<String> definitionList = new ArrayList<>(definitions);

					String newDefinition = JOptionPane.showInputDialog(null,
							"Chọn số thứ tự của nghĩa bạn muốn chỉnh sửa (hoặc 0 để hủy):", "Chỉnh sửa nghĩa",
							JOptionPane.QUESTION_MESSAGE);

					try {
						int choice = Integer.parseInt(newDefinition.trim());
						if (choice > 0 && choice <= definitionList.size()) {
							String updatedDef = JOptionPane.showInputDialog(null,
									"Nhập nghĩa mới cho nghĩa số " + choice + ":", "Cập nhật nghĩa",
									JOptionPane.QUESTION_MESSAGE);
							if (updatedDef != null && !updatedDef.trim().isEmpty()) {
								definitions.remove(definitionList.get(choice - 1));
								definitions.add(updatedDef.trim());
								meaningArea.setText("Nghĩa đã được cập nhật thành: " + updatedDef);
							}
						} else if (choice == 0) {
							meaningArea.setText("Hủy thao tác chỉnh sửa.");
						} else {
							meaningArea.setText("Lựa chọn không hợp lệ.");
						}
					} catch (NumberFormatException ex) {
						meaningArea.setText("Vui lòng nhập một số hợp lệ.");
					}
				} else {
					meaningArea.setText("Slang word không tồn tại.");
				}
			} else {
				meaningArea.setText("Vui lòng nhập Slang word.");
			}
		});

		inputPanel.add(slangLabel);
		inputPanel.add(slangField);
		inputPanel.add(meaningLabel);
		inputPanel.add(meaningScroll);
		inputPanel.add(editButton);

		panel.add(inputPanel, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createDeleteSlangPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		JLabel slangLabel = new JLabel("Nhập Slang Word cần xóa:");
		slangLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JTextField slangField = new JTextField(20);
		slangField.setMaximumSize(slangField.getPreferredSize());

		JTextArea resultArea = new JTextArea(5, 30);
		resultArea.setEditable(false);
		JScrollPane resultScroll = new JScrollPane(resultArea);

		slangField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String slangWord = slangField.getText().trim();
					if (!slangWord.isEmpty()) {
						if (SlangWordDictionary.getDictionary().containsKey(slangWord)) {
							HashSet<String> definitions = SlangWordDictionary.getDictionary().get(slangWord);
							StringBuilder sb = new StringBuilder();
							sb.append("Slang word ").append(slangWord).append(" có các nghĩa:\n");
							for (String def : definitions) {
								sb.append("- ").append(def).append("\n");
							}
							resultArea.setText(sb.toString());

							int confirm = JOptionPane.showConfirmDialog(panel,
									"Bạn có chắc chắn muốn xóa từ này không?", "Xóa Slang Word",
									JOptionPane.YES_NO_OPTION);

							if (confirm == JOptionPane.YES_OPTION) {
								SlangWordDictionary.getDictionary().remove(slangWord);
								resultArea.setText("Slang word " + slangWord + " đã được xóa khỏi từ điển.");
							} else {
								resultArea.setText("Hủy thao tác xóa.");
							}
						} else {
							resultArea.setText("Slang word " + slangWord + " không tồn tại trong từ điển.");
						}
					} else {
						resultArea.setText("Vui lòng nhập Slang word.");
					}
				}
			}
		});

		inputPanel.add(slangLabel);
		inputPanel.add(slangField);
		inputPanel.add(resultScroll);

		panel.add(inputPanel, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createResetPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

		JLabel resetLabel = new JLabel("Reset danh sách Slang Words về trạng thái ban đầu:");
		resetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton resetButton = new JButton("Reset");
		resetButton.setPreferredSize(new Dimension(150, 30));
		resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JTextArea resultArea = new JTextArea(5, 30);
		resultArea.setEditable(false);
		JScrollPane resultScroll = new JScrollPane(resultArea);

		resetButton.addActionListener(e -> {
			String fileName = "slang.txt";
			dic.resetToOriginal(fileName);
			resultArea.setText("Danh sách từ điển đã được reset từ file " + fileName);
		});

		inputPanel.add(resetLabel);
		inputPanel.add(resetButton);
		inputPanel.add(resultScroll);

		panel.add(inputPanel, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createRandomPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JTextArea randomArea = new JTextArea(5, 30);
		randomArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(randomArea);

		JButton randomButton = new JButton("Lấy Slang ngẫu nhiên");

		randomButton.addActionListener(e -> {
			String result = dic.randomSlangWord();
			randomArea.setText(result);
		});

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(randomButton, BorderLayout.SOUTH);

		return panel;
	}

//	private JPanel createQuizSlangPanel() {
//		return panel;
//	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			SlangWordApp app = new SlangWordApp();
			app.setVisible(true);
		});
	}
}