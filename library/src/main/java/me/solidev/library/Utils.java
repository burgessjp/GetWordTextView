package me.solidev.library;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by _SOLID
 * Date:2016/11/10
 * Time:12:43
 * Desc:util for GetWordTextView
 */

class Utils {

    private static List<Character> sPunctuations;

    static {
        Character[] arr = new Character[]{',', '.', ';', '!', '，', '。', '！', '；', '、', '：', '“', '”'};
        sPunctuations = Arrays.asList(arr);
    }

    static boolean isChinese(char ch) {

        return !sPunctuations.contains(ch);

//        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
//        return pattern.matcher(ch).matches();
    }

    @NonNull
    static List<Integer> getEnglishWordIndices(String word) {
        List<Integer> wordIndices = getWordIndices(word, ' ');
        for (Character punctuation : sPunctuations) {
            wordIndices.addAll(getWordIndices(word, punctuation));
        }
        Collections.sort(wordIndices);
        return wordIndices;
    }

    /**
     * Get every word's index array of text
     *
     * @param word the content
     * @param ch   separate char
     * @return index array
     */
    private static List<Integer> getWordIndices(String word, char ch) {
        int pos = word.indexOf(ch);
        List<Integer> indices = new ArrayList<>();
        while (pos != -1) {
            indices.add(pos);
            pos = word.indexOf(ch, pos + 1);
        }
        return indices;
    }
}
