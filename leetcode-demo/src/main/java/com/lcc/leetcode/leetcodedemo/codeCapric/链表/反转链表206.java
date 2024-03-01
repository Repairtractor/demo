package com.lcc.leetcode.leetcodedemo.codeCapric.链表;

import com.lcc.leetcode.leetcodedemo.codeCapric.链表.ListNode;

public class 反转链表206 {
    public ListNode reverseList(ListNode head) {
        //采用头插法，反转就好了
        ListNode dummyHead = new ListNode(),curr=head;

        while (curr!=null){
            ListNode temp=curr.next;
            curr.next=dummyHead.next;
            dummyHead.next=curr;
            curr=temp;
        }

        return dummyHead.next;
    }


}
