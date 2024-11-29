package Project;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public String getRandomSlangWord() {
		if (dictionary.isEmpty()) {
			return null;
		}
		ArrayList<String> keys = new ArrayList<>(dictionary.keySet());
		int randomIndex = (int) (Math.random() * keys.size());
		return keys.get(randomIndex);
	}

	public Map<String, Integer> randomQuizDefinitions(String slangWord) {
		if (dictionary.isEmpty()) {
			System.out.println("Danh sách Slang Words hiện đang trống.");
			return null;
		}

		if (!dictionary.containsKey(slangWord)) {
			System.out.println("Slang word '" + slangWord + "' không tồn tại trong từ điển.");
			return null;
		}

		HashSet<String> correctDefinitions = dictionary.get(slangWord);
		String correctDefinition = new ArrayList<>(correctDefinitions).get(0);

		ArrayList<String> allDefinitions = new ArrayList<>();
		for (HashSet<String> defs : dictionary.values()) {
			allDefinitions.addAll(defs);
		}

		Map<String, Integer> quizMap = new LinkedHashMap<>();
		quizMap.put(correctDefinition, 1);

		while (quizMap.size() < 4) {
			int randomIndex = (int) (Math.random() * allDefinitions.size());
			String randomDefinition = allDefinitions.get(randomIndex);

			if (!quizMap.containsKey(randomDefinition)) {
				quizMap.put(randomDefinition, 0);
			}
		}

		List<Map.Entry<String, Integer>> entryList = new ArrayList<>(quizMap.entrySet());
		Collections.shuffle(entryList);

		Map<String, Integer> shuffledQuizMap = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : entryList) {
			shuffledQuizMap.put(entry.getKey(), entry.getValue());
		}

		return shuffledQuizMap;
	}

	public String getRandomDefinition() {
		if (dictionary.isEmpty()) {
			return null;
		}

		ArrayList<String> allDefinitions = new ArrayList<>();
		for (HashSet<String> definitions : dictionary.values()) {
			allDefinitions.addAll(definitions);
		}

		int randomIndex = (int) (Math.random() * allDefinitions.size());
		return allDefinitions.get(randomIndex);
	}

	public Map<String, Integer> randomQuizSlangs(String definition) {
		Map<String, Integer> quizData = new LinkedHashMap<>();

		if (dictionary.isEmpty() || definition == null || definition.isEmpty()) {
			return quizData;
		}

		String correctSlang = null;
		for (Map.Entry<String, HashSet<String>> entry : dictionary.entrySet()) {
			if (entry.getValue().contains(definition)) {
				correctSlang = entry.getKey();
				break;
			}
		}

		if (correctSlang == null) {
			return quizData;
		}

		quizData.put(correctSlang, 1);

		ArrayList<String> keys = new ArrayList<>(dictionary.keySet());
		while (quizData.size() < 4) {
			int randomIndex = (int) (Math.random() * keys.size());
			String randomSlang = keys.get(randomIndex);

			if (!quizData.containsKey(randomSlang)) {
				quizData.put(randomSlang, 0);
			}
		}

		List<Map.Entry<String, Integer>> entries = new ArrayList<>(quizData.entrySet());
		Collections.shuffle(entries);

		Map<String, Integer> shuffledQuizData = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : entries) {
			shuffledQuizData.put(entry.getKey(), entry.getValue());
		}

		return shuffledQuizData;
	}
}
