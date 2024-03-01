package com.lcc.leetcode.leetcodedemo.codeCapric.链表;

import java.util.HashSet;
import java.util.Set;

public class 环形链表II142 {
    /**
     * 直接使用set去存储一下，就可以发现了了
     * @param head
     * @return
     */
    public ListNode detectCycle1(ListNode head) {
        Set<ListNode> walked=new HashSet<>();
        ListNode curr=head;
        while (curr!=null){
            if (walked.contains(curr.next)){
                return curr.next;
            }
            walked.add(curr);
            curr=curr.next;
        }
        return null;
    }



}
