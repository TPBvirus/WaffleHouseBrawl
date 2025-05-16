package tankwarsgame.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

class Solution {

    private static HashMap<Integer, Integer> rev = new HashMap<Integer, Integer>();
    public int reverse(int x) {
        String inputStr = Integer.toString(x);
        String alsoRet = "";
        if( x == 0){
            return 0;
        }
        char ch;
        int ret = 0;
        boolean isNegative = false;
        if( x < 0 ) {
            inputStr = inputStr.substring(1);
            isNegative = true;
            x *= -1;
        }

        for (int i=0; i < inputStr.length(); i++) {
            ch = inputStr.charAt(i); //extracts each character
            int val = ch - '0';
            rev.put(i, val);
        }


        boolean leadingZeros = true;
        for (int i = rev.size()-1; i >= 0; i--) {
            int currVal = rev.get(i);
            if (currVal == 0 && leadingZeros) {
                continue;
            }
            else if (currVal != 0 && leadingZeros) {
                leadingZeros = false;
                alsoRet += Integer.toString(currVal);
                System.out.println(alsoRet);
            }
            else{
                alsoRet += Integer.toString(currVal);
                System.out.println(alsoRet);
            }

        }
        System.out.println(rev.size());
        try {
            ret = Integer.parseInt(alsoRet);
        }
        catch (NumberFormatException e){
            System.err.println(e);
        }
        if (isNegative) {
            ret *= -1;
        }
        return ret;

    }

    private static HashMap<String, String> save = new HashMap<String, String>();
    public boolean isValid(String s) {
        int[] jeff = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            jeff[i] = -1;
        }
        for(int i = 0; i < s.length(); i++) {
            boolean firstParanthesis = false;
            boolean firstBracket = false;
            boolean firstBrace = false;

            System.out.println("Looking for matching" + s.charAt(i));
            char target = s.charAt(i);
            switch (target) {
                case '(':
                    firstParanthesis = true;
                    break;
                case '[':
                    firstBracket = true;
                    break;
                case '{':
                    firstBrace = true;
                    break;
            }

            if ( target == ')' && !firstParanthesis) {
                return false;
            }
            if ( target == ']' && !firstBracket) {
                return false;
            }
            if ( target == '}' && !firstBrace) {
                return false;
            }
            //check rest of string and mark down areas that are matched
            for(int j = i; j < s.length(); j++) {
                if(target == '(' && s.charAt(j) == ')') {
                    jeff[i] = j;
                    System.out.println("Breaking at 1");
                    break;}
                if(target == '[' && s.charAt(j) == ']') {
                    jeff[i] = j;
                    System.out.println("Breaking at 2");
                    break;}
                if(target == '{' && s.charAt(j) == '}') {
                    jeff[i] = j;
                    System.out.println("Breaking at 3");
                    break;}
                if(target == ')' || target == ']' || target == '}') {

                }
            }
        }
        return true;
    }

    public int mySqrt(int x) {
        int save = (int) mySqrt( (double)x , 5);
        return save;
    }

    public double mySqrt( double x , int iterations){
        if( iterations == 0){
            System.out.println(x);
            return x;
        }
        return mySqrt( x - ((x*x - 2) / 2*x) , iterations - 1);
    }

    public static void main(String[] args) {
        Solution x = new Solution();
        String s = "AAAAA";
        System.out.println(x.mySqrt(4));
    }
}