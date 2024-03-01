package com.lcc.leetcode.leetcodedemo.codeCapric.hash;

public class 有效的字母异位词242 {

    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;


        //字母异位词就是字母相同，但是位置不同
        //1. 建立数组下标，标识26个位置的下标，判断26个字母出现的位置，然后判断s和t的长度必须相当，并且每个字母出现的位置必须相等
        int[] ans = new int[26]; //hash表，index标识字母，data标识字母出现次数
        char a = 'a'; //ASCii码的小写字母起始值
        for (char c : s.toCharArray()) {
            ans[c-a]++;
        }
        for (char c:t.toCharArray()){
            ans[c-a]--;
        }
        for (int an : ans) {
            if (an!=0)return false;
        }
        return true;
    }
}
