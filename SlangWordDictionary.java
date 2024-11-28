package Project;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class SlangWordDictionary {
	private static HashMap<String, HashSet<String>> dictionary;
	private static ArrayList<String> searchHistory;

	public SlangWordDictionary() {
		dictionary = new HashMap<>();
		searchHistory = new ArrayList<>();
	}

	public static HashMap<String, HashSet<String>> getDictionary() {
		return dictionary;
	}

	public static ArrayList<String> getSearchHistory() {
		return searchHistory;
	}

	public void loadFromFile(String fileName, HashMap<String, HashSet<String>> dictionary) {
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

	public static String searchDefinition(String definition) {
		boolean found = false;
		StringBuilder results = new StringBuilder();
		results.append("Kết quả tìm kiếm được với nghĩa: ").append(definition).append("\n");

		for (Map.Entry<String, HashSet<String>> entry : dictionary.entrySet()) {
			for (String meaning : entry.getValue()) {
				if (meaning.toLowerCase().contains(definition.toLowerCase())) {
					results.append("SlangWord: ").append(entry.getKey()).append(" - Nghĩa: ").append(meaning)
							.append("\n");
					found = true;
				}
			}
		}
		if (!found) {
			results.append("Không tìm thấy SlangWord nào có nghĩa là: ").append(definition);
		}

		return results.toString();
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

	public void resetToOriginal(String fileName) {
		try {
			dictionary.clear();
			loadFromFile(fileName, dictionary);
			System.out.println("Danh sách Slang Words đã được reset");
		} catch (Exception e) {
			System.out.println("Gặp lỗi khi reset danh sách Slang Words: " + e.getMessage());
		}
	}

	public String randomSlangWord() {
		if (dictionary.isEmpty()) {
			return "Danh sách Slang Words rỗng.";
		}
		ArrayList<String> keys = new ArrayList<>(dictionary.keySet());
		int randomIndex = (int) (Math.random() * keys.size());
		String randomSlang = keys.get(randomIndex);
		HashSet<String> definitions = dictionary.get(randomSlang);
		return "Slang word được random: " + randomSlang + "\nNghĩa: " + String.join(", ", definitions);
	}

	public String randomQuizSlangWord() {
		if (dictionary.isEmpty()) {
			return "Danh sách Slang Words hiện đang trống.";
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

		StringBuilder quizQuestion = new StringBuilder();
		quizQuestion.append("Slang word: " + questionSlang + "\n");
		for (String option : options) {
			quizQuestion.append(option + "\n");
		}

		return quizQuestion.toString();
	}

	public String getCorrectAnswer(String slangWord) {
		if (dictionary.containsKey(slangWord)) {
			return new ArrayList<>(dictionary.get(slangWord)).get(0);
		}
		return "";
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
//	public static void main(String[] args) {
//		SlangWordDictionary dic = new SlangWordDictionary();
//		loadFromFile("slang.txt", dic.getDictionary());
//		dic.randomQuizDefinition();
//	}
}
