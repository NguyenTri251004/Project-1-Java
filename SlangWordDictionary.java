package Project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
				System.out.println("Từ lóng: " + entry.getKey() + " - Nghĩa: " + entry.getValue());
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

	public static void main(String[] args) {
		SlangWordDictionary dic = new SlangWordDictionary();
		loadFromFile("slang.txt", dic.getDictionary());
		System.out.println(dic.searchSlangWord("FIFO"));
		System.out.println(dic.searchSlangWord("WOW"));
		dic.displaySearchHistory();
//		PrintDictionary(dic.getDictionary());
	}
}
