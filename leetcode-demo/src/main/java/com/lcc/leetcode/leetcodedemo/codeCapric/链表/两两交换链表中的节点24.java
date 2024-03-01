package com.lcc.leetcode.leetcodedemo.codeCapric.链表;

public class 两两交换链表中的节点24 {

    public ListNode swapPairs(ListNode head) {
        //如果head.next==null 返回head
        if (head == null || head.next == null) return head;

        ListNode dummyHead = new ListNode(), curr = dummyHead;
        dummyHead.next = head;

        while (curr.next != null && curr.next.next != null) {
            // 1  2  3
            //记录3节点
            ListNode temp = curr.next.next;
            //让1->3
            curr.next.next = temp.next;

            //让2->1
            temp.next = curr.next;

            //让假头->2
            curr.next = temp;

            curr = temp.next;

        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode dummyHead = new ListNode(), curr = dummyHead;
        for (int it = 1; it <= 4; it++) {
            curr.next = new ListNode(it);
            curr = curr.next;
        }

        ListNode listNode = new 两两交换链表中的节点24().swapPairs(dummyHead.next);
        System.out.println();
    }
}
