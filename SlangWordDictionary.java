package Project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

	public static String searchSlangWord(String slangWord) {
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
//	public void editSlangWord(String slangWord, String newDefinition) {
//		if (dictionary.containsKey(slangWord)) {
//			dictionary.put(slangWord, newDefinition);
//			System.out.println("Slang word '" + slangWord + "' đã được cập nhật nghĩa mới.");
//		} else {
//			System.out.println("Slang word '" + slangWord + "' không tồn tại trong từ điển.");
//		}
//	}

	public static void main(String[] args) {
		SlangWordDictionary dic = new SlangWordDictionary();
		loadFromFile("slang.txt", dic.getDictionary());
//		printDictionary(dic.dictionary);
		searchDefinition("An");
	}
}
