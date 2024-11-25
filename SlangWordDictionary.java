package Project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SlangWordDictionary {
	private HashMap<String, String> dictionary;
	private ArrayList<String> searchHistory;

	public HashMap<String, String> getDictionary() {
		return dictionary;
	}

	public SlangWordDictionary() {
		dictionary = new HashMap<>();
		searchHistory = new ArrayList<>();
	}

	public static void loadFromFile(String fileName, HashMap<String, String> dictionary) {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("`");
				if (parts.length == 2) {
					String key = parts[0].trim();
					String meaning = parts[1].trim();
					dictionary.put(key, meaning);
				}
			}
			System.out.println("Danh sach duoc cap nhat tu slang.txt");
		} catch (IOException e) {
			System.out.println("Gap loi khi doc: " + e.getMessage());
		}
	}

	public static void PrintDictionary(HashMap<String, String> dictionary) {
		for (String key : dictionary.keySet()) {
			System.out.println("SlangWord: " + key + " - Nghia: " + dictionary.get(key));
		}
	}

	public String searchSlangWord(String slangWord) {
		searchHistory.add(slangWord);
		return dictionary.getOrDefault(slangWord, "SlangWord khong co trong tu dien!");
	}

	public void sreachDefinition(String definition) {
		boolean flag = false;
		System.out.println("Ket qua tim kiem voi nghia: " + definition);
		for (Map.Entry<String, String> entry : dictionary.entrySet()) {
			if (entry.getValue().toLowerCase().contains(definition.toLowerCase())) {
				System.out.println("SlangWord" + entry.getKey() + " - Nghia: " + entry.getValue());
				flag = true;
			}
		}
		if (!flag) {
			System.out.println("Khong tim thay SlangWord nao co nghia la: " + definition);
		}
	}

	public void displaySearchHistory() {
		System.out.println("Lich su tim kiem cua ban: ");
		if (searchHistory.isEmpty()) {
			System.out.println("Chua co tim kiem SlangWord nao!.");
		} else {
			for (String slang : searchHistory) {
				System.out.println("- " + slang);
			}
		}
	}

	public void addSlangWord(String slangWord, String definition) {
		Scanner scanner = new Scanner(System.in);
		if (dictionary.containsKey(slangWord)) {
			System.out.println("Slang word '" + slangWord + "' da ton tai voi nghia: " + dictionary.get(slangWord));
			System.out.print("Ban co muon overwrite hay duplicate? (o/d): ");
			String choice = scanner.nextLine().trim().toLowerCase();

			if (choice.equals("o")) {
				dictionary.put(slangWord, definition);
				System.out.println("Slang word '" + slangWord + "' da duoc overwrite voi nghia moi!");
			} else if (choice.equals("d")) {
				String newKey = slangWord + "_new";
				dictionary.put(newKey, definition);
				System.out.println("Slang word" + newKey + "da duoc them.");
			} else {
				System.out.println("Huy thao tac.");
			}
		} else {
			dictionary.put(slangWord, definition);
			System.out.println("Slang word '" + slangWord + "' da duoc them moi.");
		}
		scanner.close();
	}

	public void editSlangWord(String slangWord, String newDefinition) {
		if (dictionary.containsKey(slangWord)) {
			dictionary.put(slangWord, newDefinition);
			System.out.println("Slang word '" + slangWord + "' đã được cập nhật nghĩa mới.");
		} else {
			System.out.println("Slang word '" + slangWord + "' không tồn tại trong từ điển.");
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		SlangWordDictionary dic = new SlangWordDictionary();
		loadFromFile("slang.txt", dic.getDictionary());
		System.out.println("\n--- Thêm slang word ---");
		System.out.print("Nhập slang word: ");
		String newSlang = scanner.nextLine();
		System.out.print("Nhập nghĩa: ");
		String newDefinition = scanner.nextLine();
		dic.addSlangWord(newSlang, newDefinition);

		System.out.println("\n--- Chỉnh sửa slang word ---");
		System.out.print("Nhập slang word muốn chỉnh sửa: ");
		String editSlang = scanner.nextLine();
		System.out.print("Nhập nghĩa mới: ");
		String editDefinition = scanner.nextLine();
		dic.editSlangWord(editSlang, editDefinition);
		System.out.println(dic.searchSlangWord("hi"));
		scanner.close();
	}
}
