package com.lcc.leetcode.leetcodedemo.codeCapric.hash;

public class 赎金信383 {

    //使用数组记录magazine数组的出场次数，然后遍历ransomNote，依次扣减，不能扣减时返回false
    public boolean canConstruct(String ransomNote, String magazine) {
        char[] ransomNoteCharArray = ransomNote.toCharArray(), magazineCharArray = magazine.toCharArray();

        int[] arr = new int[26];

        for (char c : magazineCharArray) {
            arr[c - 'a']++;
        }

        for (char c:ransomNoteCharArray){
            arr[c-'a']--;
            if (arr[c-'a']<0)return false;
        }

        return true;
    }
}
