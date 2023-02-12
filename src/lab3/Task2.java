package lab3;

public class Task2 {
    public static void main(String[] args) {
        String[] strMain = "java Palindrome madam racecar apple kayak song noon".split(" ");
        for (String s : strMain) {
            System.out.println(s + "\t\t" + reverseString(s) + "\t\t" + isPalindrome(s));
        }
    }

    public static String reverseString(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            str.append(s.charAt(i));
        }
        return str.toString();
    }

    public static boolean isPalindrome(String s) {
        return s.equals(reverseString(s));
    }
}
