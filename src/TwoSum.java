import org.jetbrains.annotations.Contract;
import org.junit.Test;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TwoSum {

    /** 1. Two Sum   ***************************************************************************************************
     *
     *   Given an array of integers, return indices of the two numbers such that they add up to a specific target.
     * You may assume that each input would have exactly one solution, and you may not use the same element twice.
     *
     * Given nums = [2, 7, 11, 15], target = 9,
     * return [0, 1].
     *
     * Method: 一次遍历哈希表
     *     哈希表查找的时间复杂度一般为 O(1), 当哈希地址冲突发生时, 其时间复杂度为最差的 O(n).
     *   只要哈希函数选取得当, 哈希表查找的时间复杂度就会稳定为 O(1).
     *     将数组元素逐个插入到哈希表中，在插入之前检查 target 与当前元素之差是否已经存在于哈希表中,
     *   如果存在说明有解, 否则将该元素插入到哈希表中, 直到遍历完所有数组元素.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] twoSum(int[] nums, int target){
        if(nums.length<2){
            throw new RuntimeException("No solution for two sum");
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i=0; i<nums.length; i++) {
            int complement = target - nums[i];
            if(map.containsKey(complement)){
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        throw new RuntimeException("No solution for two sum");
    }

    /** 167. Two Sum II - Input array is sorted  ***********************************************************************
     *
     *   Given an array of integers that is already sorted in ascending order, find two numbers such that
     * they add up to a specific target number.
     *   The function twoSum should return indices of the two numbers such that they add up to the target,
     * where index1 must be less than index2. Please note that your returned answers (both index1 and index2)
     * are not zero-based.
     *   You may assume that each input would have exactly one solution and you may not use the same element twice.
     *
     * Input: numbers={2, 7, 11, 15}, target=9
     * Output: index1=1, index2=2
     *
     * Method: 由于数组已经按从小到大的顺序排列好, 因此设置两个指针分别指向数组首尾元素, 通过移动这两个指针来遍历数组.
     *   如果指针指向的两个数之和刚好等于 target, 直接返回两个指针的位置; 如果大于 target, 则尾指针要向左移一位;
     *   如果小于 target, 则首指针要向右移一位; 直到两个指针相遇.
     *
     *  Time Complexity: O(n)
     *  Space Complexity: O(1)
     */
    public List<Integer> twoSumSorted(int[] nums, int target){
        if(nums.length<2){
            System.err.println("No solution");
            return null;
        }
        for(int begin = 0, end = nums.length-1; begin < end; ){
            int temp = nums[begin] + nums[end];
            if(temp == target){
                List<Integer> result = new ArrayList<Integer>();
                result.add(begin);
                result.add(end);
                return result;
            }
            else if(temp > target){
                end--;
            }
            else{
                begin++;
            }
        }
        System.err.println("No solution");
        return null;
    }

    /** 170. Two Sum III - Data structure design  **********************************************************************
     *
     *   Design and implement a TwoSum class. It should support the following operations:add and find.
     *  add - Add the number to an internal data structure.
     *  find - Find if there exists any pair of numbers which sum is equal to the value.
     *
     * add(1); add(3); add(5);
     * find(4) -> true
     * find(7) -> false
     */
    class TwoSumInner{

        // 数值-个数 映射
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        public void add(int num){
            if(map.containsKey(num)){
                map.put(num, map.get(num)+1);
            }
            else{
                map.put(num, 1);
            }
        }

        public boolean find(int num){
            for(int key: map.keySet()){
                int temp = num - key;
                if((temp == key && map.get(key)>1) || (temp != key && map.containsKey(temp))){
                    return true;
                }
            }
            return false;
        }

    }

    /** 653. Two Sum IV - Input is a BST  ******************************************************************************
     *
     *   Given a Binary Search Tree and a target number, return true if there exist two elements in the BST
     * such that their sum is equal to the given target.
     *
     * Method One: 深度优先遍历 BST, 遍历过程中采用 HashSet 保存已经遍历过的值.
     *   每次遍历时，在将新节点的值插入哈希表之前, 需要检查哈希表中是否包含k与该节点值之差.
     *   也适用于一般的二叉树.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean findTarget(TreeNode root, int k) {
        HashSet<Integer> values = new HashSet<Integer>();
        return dfsTraverse(root, k, values);
    }

    /**
     * Method Two: 通过中序遍历 BST, 将 BST 转换成一个按从小到大顺序的有序数组, 设置两个指针, 分别指向数组的首尾元素,
     *   通过移动这两个指针来查找是否有两个数之和为 k.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean findTarget2(TreeNode root, int k){
        List<Integer> nums = new ArrayList<Integer>();
        inOrder(root, nums);
        return twoSum(nums, k);
    }

    /**
     * Method Three: 利用 BST 的性质, 对每个节点, 判断 k 与该节点值之差在 BST 中是否存在.
     *
     * Time Complexity: O(nlogn).
     * Space Complexity: O(h). h is the height of BST, which is logn at best case and n at worst case.
     */
    public boolean findTarget3(TreeNode root, int k){
        return dfsTraverse(root, root, k);
    }

    /**
     * 深度优先遍历 BST
     */
    public boolean dfsTraverse(TreeNode root, int k, HashSet<Integer> values) {
        if(root==null){
            return false;
        }
        int complement = k - root.val;
        if(values.contains(complement)) {
            return true;
        }
        else{
            values.add(root.val);
            return dfsTraverse(root.left, k, values) || dfsTraverse(root.right, k, values);
        }
    }

    /**
     * 中序遍历 BST 得到有序数组
     */
    public void inOrder(TreeNode node, List<Integer> nums){
        if(node==null){
            return;
        }
        inOrder(node.left, nums);
        nums.add(node.val);
        inOrder(node.right, nums);
    }

    /**
     * 检查有序数组中是否有两数之和为 target
     */
    public boolean twoSum(List<Integer> nums, int target){
        if(nums.size()<2){
            return false;
        }
        for(int begin = 0, end = nums.size()-1; begin < end; ){
            int temp = nums.get(begin) + nums.get(end);
            if(temp == target){
                return true;
            }
            else if(temp > target){
                end --;
            }
            else{
                begin ++;
            }
        }
        return false;
    }

    /**
     * 深度优先遍历 BST
     */
    public boolean dfsTraverse(TreeNode root, TreeNode cur, int k) {
        if(cur == null){
            return false;
        }
        return dfsSearch(root, cur,(k-cur.val))
                || dfsTraverse(root,cur.left, k)
                || dfsTraverse(root, cur.right, k);
    }

    /**
     * 深度优先查找 value
     */
    public boolean dfsSearch(TreeNode root, TreeNode cur, int value){
        if(root==null){
            return false;
        }
        return ((value == root.val) && (root != cur))
                || ((value < root.val) && dfsSearch(root.left, cur, value))
                || ((value > root.val) && dfsSearch(root.right, cur, value));
    }

    class TreeNode{
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}