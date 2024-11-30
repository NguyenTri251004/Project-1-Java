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
			System.out.println("Danh sách từ điển đã được reset");
		} catch (Exception e) {
			System.out.println("Gặp lỗi khi reset danh sách từ điển: " + e.getMessage());
		}
	}

	public String randomSlangWord() {
		if (dictionary.isEmpty()) {
			return "Danh sách từ điển rỗng.";
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
			System.out.println("Danh sách từ điẻn đang trống.");
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

		Map<String, Integer> quiz = new LinkedHashMap<>();
		quiz.put(correctDefinition, 1);

		while (quiz.size() < 4) {
			int index = (int) (Math.random() * allDefinitions.size());
			String randomDefinition = allDefinitions.get(index);

			if (!quiz.containsKey(randomDefinition)) {
				quiz.put(randomDefinition, 0);
			}
		}

		List<Map.Entry<String, Integer>> entryList = new ArrayList<>(quiz.entrySet());
		Collections.shuffle(entryList);

		Map<String, Integer> shuffledQuiz = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : entryList) {
			shuffledQuiz.put(entry.getKey(), entry.getValue());
		}

		return shuffledQuiz;
	}

	public String getRandomDefinition() {
		if (dictionary.isEmpty()) {
			return null;
		}

		ArrayList<String> allDefinitions = new ArrayList<>();
		for (HashSet<String> definitions : dictionary.values()) {
			allDefinitions.addAll(definitions);
		}

		int index = (int) (Math.random() * allDefinitions.size());
		return allDefinitions.get(index);
	}

	public Map<String, Integer> randomQuizSlangs(String definition) {
		Map<String, Integer> quiz = new LinkedHashMap<>();

		if (dictionary.isEmpty() || definition == null || definition.isEmpty()) {
			return quiz;
		}

		String correct = null;
		for (Map.Entry<String, HashSet<String>> entry : dictionary.entrySet()) {
			if (entry.getValue().contains(definition)) {
				correct = entry.getKey();
				break;
			}
		}

		if (correct == null) {
			return quiz;
		}

		quiz.put(correct, 1);

		ArrayList<String> keys = new ArrayList<>(dictionary.keySet());
		while (quiz.size() < 4) {
			int randomIndex = (int) (Math.random() * keys.size());
			String randomSlang = keys.get(randomIndex);

			if (!quiz.containsKey(randomSlang)) {
				quiz.put(randomSlang, 0);
			}
		}

		List<Map.Entry<String, Integer>> entries = new ArrayList<>(quiz.entrySet());
		Collections.shuffle(entries);

		Map<String, Integer> shuffledQuizData = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : entries) {
			shuffledQuizData.put(entry.getKey(), entry.getValue());
		}
		
		return shuffledQuizData;
	}
}
