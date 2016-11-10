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
        Character[] arr = new Character[]{',', '.', ';', '!', '"', '，', '。', '！', '；', '、', '：', '“', '”','?','？'};
        sPunctuations = Arrays.asList(arr);
    }

    static boolean isChinese(char ch) {
        return !sPunctuations.contains(ch);
    }

    @NonNull
    static List<WordInfo> getEnglishWordIndices(String content) {
        List<Integer> separatorIndices = getSeparatorIndices(content, ' ');
        for (Character punctuation : sPunctuations) {
            separatorIndices.addAll(getSeparatorIndices(content, punctuation));
        }
        Collections.sort(separatorIndices);
        List<WordInfo> wordInfoList = new ArrayList<>();
        int start = 0;
        int end;
        for (int i = 0; i < separatorIndices.size(); i++) {
            end = separatorIndices.get(i);
            if (start == end) {
                start++;
            } else {
                WordInfo wordInfo = new WordInfo();
                wordInfo.setStart(start);
                wordInfo.setEnd(end);
                wordInfoList.add(wordInfo);
                start = end + 1;
            }
        }
        return wordInfoList;
    }

    /**
     * Get every word's index array of text
     *
     * @param word the content
     * @param ch   separate char
     * @return index array
     */
    private static List<Integer> getSeparatorIndices(String word, char ch) {
        int pos = word.indexOf(ch);
        List<Integer> indices = new ArrayList<>();
        while (pos != -1) {
            indices.add(pos);
            pos = word.indexOf(ch, pos + 1);
        }
        return indices;
    }
}
