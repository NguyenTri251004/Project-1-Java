package Project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlangWordDictionary {
	private static HashMap<String, HashSet<String>> dictionary;
	private static ArrayList<String> searchHistory;

	public SlangWordDictionary() {
		dictionary = new HashMap<>();
		searchHistory = new ArrayList<>();
	}

	public HashMap<String, HashSet<String>> getDictionary() {
		return dictionary;
	}

	public static void loadFromFile(String fileName, HashMap<String, HashSet<String>> dictionary) {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				Pattern pattern = Pattern.compile("(.*?)`(.*)");
				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {
					String slangWord = matcher.group(1).trim();
					String[] meanings = matcher.group(2).split("\\|");

					HashSet<String> definitionSet = new HashSet<>();
					for (String meaning : meanings) {
						definitionSet.add(meaning.trim());
					}
					dictionary.put(slangWord, definitionSet);
				}
			}
			System.out.println("Danh sách từ điển đã được cập nhật từ file: " + fileName);
		} catch (IOException e) {
			System.out.println("Gặp lỗi khi đọc file: " + e.getMessage());
		}
	}

	public static void printDictionary(HashMap<String, HashSet<String>> dictionary) {
		for (Map.Entry<String, HashSet<String>> entry : dictionary.entrySet()) {
			System.out.println("SlangWord: " + entry.getKey() + " có nghĩa là: " + entry.getValue());
		}
	}

	public String searchSlangWord(String slangWord) {
		searchHistory.add(slangWord);
		if (dictionary.containsKey(slangWord)) {
			return "SlangWord: " + slangWord + " có nghĩa là: " + dictionary.get(slangWord);
		} else {
			return "SlangWord không có trong từ điển!";
		}
	}

	public static void searchDefinition(String definition) {
		boolean flag = false;
		System.out.println("Kết quả tìm kiếm được với nghĩa: " + definition);
		for (Map.Entry<String, HashSet<String>> entry : dictionary.entrySet()) {
			for (String meaning : entry.getValue()) {
				if (meaning.toLowerCase().contains(definition.toLowerCase())) {
					System.out.println("SlangWord: " + entry.getKey() + " - có nghĩa là: " + entry.getValue());
					flag = true;
				}
			}
		}
		if (!flag) {
			System.out.println("Không tìm thấy SlangWord nào có nghĩa là: " + definition);
		}
	}

	public static void displaySearchHistory() {
		System.out.println("Lịch sử tìm kiếm của bạn: ");
		if (searchHistory.isEmpty()) {
			System.out.println("Chưa có tìm kiếm SlangWord nào!.");
		} else {
			for (String slang : searchHistory) {
				System.out.println("- " + slang);
			}
		}
	}

	public void addSlangWord(String slangWord, String newDefinition) {
		Scanner scanner = new Scanner(System.in);

		if (dictionary.containsKey(slangWord)) {
			System.out.println(slangWord + " đã tồn tại với các nghĩa: " + dictionary.get(slangWord));
			System.out.print("Bạn muốn overwrite hay duplicate? (o/d): ");
			String choice = scanner.nextLine().trim().toLowerCase();

			if (choice.equals("o")) {
				HashSet<String> newDefinitions = new HashSet<>();
				newDefinitions.add(newDefinition.trim());
				dictionary.put(slangWord, newDefinitions);
				System.out.println(slangWord + " đã được overwrite với nghĩa mới!");
			} else if (choice.equals("d")) {
				dictionary.get(slangWord).add(newDefinition.trim());
				System.out.println("Nghĩa mới đã được thêm vào " + slangWord + ".");
			} else {
				System.out.println("Hủy thao tác.");
			}
		} else {
			HashSet<String> definitions = new HashSet<>();
			definitions.add(newDefinition.trim());
			dictionary.put(slangWord, definitions);
			System.out.println("Slang word '" + slangWord + "' đã được thêm mới.");
		}
		scanner.close();
	}

	public void editSlangWord(String slangWord) {
		if (dictionary.containsKey(slangWord)) {
			HashSet<String> definitions = dictionary.get(slangWord);
			System.out.println("Slang word '" + slangWord + "' có các nghĩa sau:");
			int index = 1;
			ArrayList<String> definitionList = new ArrayList<>(definitions);
			for (String def : definitionList) {
				System.out.println(index + ". " + def);
				index++;
			}
			Scanner scanner = new Scanner(System.in);
			System.out.print("Chọn số thứ tự của nghĩa bạn muốn chỉnh sửa (hoặc 0 để hủy): ");
			int choice = scanner.nextInt();
			scanner.nextLine();

			if (choice > 0 && choice <= definitionList.size()) {
				System.out.print("Nhập nghĩa mới: ");
				String newDefinition = scanner.nextLine().trim();
				definitions.remove(definitionList.get(choice - 1));
				definitions.add(newDefinition);
				System.out.println("Nghĩa đã được cập nhật thành: " + newDefinition);
			} else if (choice == 0) {
				System.out.println("Hủy thao tác chỉnh sửa.");
			} else {
				System.out.println("Lựa chọn không hợp lệ.");
			}
		} else {
			System.out.println("Slang word '" + slangWord + "' không tồn tại trong từ điển.");
		}
	}

	public void deleteSlangWord(String slangWord) {
		if (dictionary.containsKey(slangWord)) {
			System.out.println("Slang word " + slangWord + " có các nghĩa:");
			HashSet<String> definitions = dictionary.get(slangWord);
			for (String definition : definitions) {
				System.out.println("- " + definition);
			}
			Scanner scanner = new Scanner(System.in);
			System.out.print("Bạn có chắc chắn muốn xóa từ này không? (y/n): ");
			String confirm = scanner.nextLine().trim().toLowerCase();

			if (confirm.equals("y")) {
				dictionary.remove(slangWord);
				System.out.println("Slang word " + slangWord + " đã được xóa khỏi từ điển.");
			} else {
				System.out.println("Hủy thao tác xóa.");
			}
		} else {
			System.out.println("Slang word '" + slangWord + "' không tồn tại trong từ điển.");
		}
	}

	public void resetToOriginal(String fileName) {
		try {
			dictionary.clear();
			loadFromFile(fileName, dictionary);
			System.out.println("Danh sách Slang Words đã được reset");
		} catch (Exception e) {
			System.out.println("Gặp lỗi khi reset danh sách Slang Words: " + e.getMessage());
		}
	}

	public void randomSlangWord() {
		if (dictionary.isEmpty()) {
			System.out.println("Danh sách Slang Words rỗng.");
			return;
		}
		ArrayList<String> keys = new ArrayList<>(dictionary.keySet());
		int randomIndex = (int) (Math.random() * keys.size());
		String randomSlang = keys.get(randomIndex);
		HashSet<String> definitions = dictionary.get(randomSlang);
		System.out.println("Slang word được random: " + randomSlang);
		System.out.println("Nghĩa: " + String.join(", ", definitions));
	}

	public void randomQuizSlangWord() {
		if (dictionary.isEmpty()) {
			System.out.println("Danh sách Slang Words hiện đang trống.");
			return;
		}
		ArrayList<String> keys = new ArrayList<>(dictionary.keySet());
		int questionIndex = (int) (Math.random() * keys.size());
		String questionSlang = keys.get(questionIndex);
		HashSet<String> correctDefinitions = dictionary.get(questionSlang);
		String correctAnswer = new ArrayList<>(correctDefinitions).get(0);
		ArrayList<String> options = new ArrayList<>();
		options.add(correctAnswer);

		while (options.size() < 4) {
			int randomIndex = (int) (Math.random() * keys.size());
			String randomSlang = keys.get(randomIndex);
			HashSet<String> randomDefinitions = dictionary.get(randomSlang);
			String randomDefinition = new ArrayList<>(randomDefinitions).get(0);
			if (!options.contains(randomDefinition)) {
				options.add(randomDefinition);
			}
		}
		Collections.shuffle(options);
		System.out.println("Slang word: " + questionSlang);
		System.out.println("Định nghĩa đúng cho từ này là?");
		for (int i = 0; i < options.size(); i++) {
			System.out.println((i + 1) + ". " + options.get(i));
		}

		Scanner scanner = new Scanner(System.in);
		System.out.print("Nhập số đáp án của bạn (1-4): ");
		int userAnswer = scanner.nextInt();

		if (userAnswer >= 1 && userAnswer <= 4 && options.get(userAnswer - 1).equals(correctAnswer)) {
			System.out.println("Chính xác! Đáp án là: " + correctAnswer);
		} else {
			System.out.println("Sai rồi! Đáp án đúng là: " + correctAnswer);
		}
	}

	public void randomQuizDefinition() {
		if (dictionary.isEmpty()) {
			System.out.println("Danh sách Slang Words hiện đang trống.");
			return;
		}
		ArrayList<String> keys = new ArrayList<>(dictionary.keySet());
		int questionIndex = (int) (Math.random() * keys.size());
		String correctSlang = keys.get(questionIndex);
		HashSet<String> correctDefinitions = dictionary.get(correctSlang);
		String questionDefinition = new ArrayList<>(correctDefinitions).get(0);
		ArrayList<String> options = new ArrayList<>();
		options.add(correctSlang);
		while (options.size() < 4) {
			int randomIndex = (int) (Math.random() * keys.size());
			String randomSlang = keys.get(randomIndex);
			if (!options.contains(randomSlang)) {
				options.add(randomSlang);
			}
		}
		Collections.shuffle(options);
		System.out.println("Định nghĩa: " + questionDefinition);
		System.out.println("Slang word nào có nghĩa trên?");
		for (int i = 0; i < options.size(); i++) {
			System.out.println((i + 1) + ". " + options.get(i));
		}
		Scanner scanner = new Scanner(System.in);
		System.out.print("Nhập số đáp án của bạn (1-4): ");
		int userAnswer = scanner.nextInt();
		if (userAnswer >= 1 && userAnswer <= 4 && options.get(userAnswer - 1).equals(correctSlang)) {
			System.out.println("Chính xác! Slang word là: " + correctSlang);
		} else {
			System.out.println("Sai rồi! Slang word đúng là: " + correctSlang);
		}
	}

	public static void main(String[] args) {
		SlangWordDictionary dic = new SlangWordDictionary();
		loadFromFile("slang.txt", dic.getDictionary());
//		printDictionary(dic.dictionary);
//		dic.editSlangWord(">.<");
//		System.out.println(dic.searchSlangWord(">.<"));
		dic.randomQuizDefinition();
	}
}
