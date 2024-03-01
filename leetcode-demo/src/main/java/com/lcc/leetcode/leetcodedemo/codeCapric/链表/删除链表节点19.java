package com.lcc.leetcode.leetcodedemo.codeCapric.链表;

public class 删除链表节点19 {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        //slow fast两个指针遍历，fast领先slow n个身位，当fast.next==null时返回slow

        ListNode dummyNode = new ListNode(), slow = dummyNode, fast = dummyNode;
        dummyNode.next = head;

        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }

        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }

        slow.next = slow.next.next;
        return dummyNode.next;
    }

    public static void main(String[] args) {
        ListNode head=new ListNode(),curr=head;

        for (int i = 1; i < 3; i++) {
            curr.next = new ListNode(i);
            curr=curr.next;
        }

        ListNode listNode = new 删除链表节点19().removeNthFromEnd(head.next, 1);
        System.out.println("结束");
        /**
         *    1  2
         * fast
         *       *
         *
         *   *      *
         */
    }


}
