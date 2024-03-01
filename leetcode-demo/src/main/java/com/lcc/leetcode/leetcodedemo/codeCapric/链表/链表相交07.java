package com.lcc.leetcode.leetcodedemo.codeCapric.链表;

import java.util.Objects;

public class 链表相交07 {

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        //首先计算长度
        int headA_num = 0, headB_num = 0;
        for (ListNode curr = headA; curr != null; curr = curr.next, headA_num++) ;
        for (ListNode curr = headB; curr != null; curr = curr.next, headB_num++) ;

        int num = 0;
        ListNode beginA = headA, beginB = headB;

        if (headA_num > headB_num) {
            num = headA_num - headB_num;
            for (int i = 0; i < num; i++) {
                beginA = beginA.next;
            }
        } else {
            num = headB_num - headA_num;
            for (int i = 0; i < num; i++) {
                beginB = beginB.next;
            }
        }

        while (beginA != null) {
            if (beginA == beginB) {
                return beginA;
            }
            beginA = beginA.next;
            beginB = beginB.next;
        }
        return null;
    }

    public ListNode getIntersectionNode1(ListNode headA, ListNode headB) {
        if (Objects.isNull(headA) || Objects.isNull(headB)) return null;

        ListNode pA = headA, pB = headB;

        //两个链表连在一起，长度相同，相同时就是相交点
        while (pA != pB) {
            pA = Objects.isNull(pA.next) ? pB : pA.next;
            pB = Objects.isNull(pB.next) ? pA : pB.next;
        }
        return pA;
    }
}
