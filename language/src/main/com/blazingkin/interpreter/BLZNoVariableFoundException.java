package com.blazingkin.interpreter;
import java.util.Collection;

public class BLZNoVariableFoundException extends BLZRuntimeException {

    String baseMsg = "";

    public static final long serialVersionUID = 867530999l;

    public BLZNoVariableFoundException(String message, Collection<String> options, String original) {
        super(message + "\nDid you mean \""  +calculateDidYouMean(options, original) + "\"?");
        baseMsg = message;
	}
	
	public BLZNoVariableFoundException(BLZNoVariableFoundException original, Collection<String> options, String lookingFor) {
		super(original.baseMsg + "\nDid you mean \"" + calculateDidYouMean(options, lookingFor) + "\"?");
		baseMsg = original.baseMsg;
	}




    public static String calculateDidYouMean(Collection<String> options, String original){
        int lowest = Integer.MAX_VALUE;
        String didYouMean = "";
        for (String s : options) {
            int distance = levenshteinDistance(s, original);
            if (distance < lowest) {
                lowest = distance;
                didYouMean = s;
            }
        }
        return didYouMean;
    }



    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
	}
	
    public static int min(int... numbers) {
		int max = Integer.MAX_VALUE;
		for (int num : numbers) {
			max = num < max ? num : max;
		}
		return max;
    }

	public static int levenshteinDistance(String x, String y) {
		int[][] dp = new int[x.length() + 1][y.length() + 1];
	
		for (int i = 0; i <= x.length(); i++) {
			for (int j = 0; j <= y.length(); j++) {
				if (i == 0) {
					dp[i][j] = j;
				}
				else if (j == 0) {
					dp[i][j] = i;
				}
				else {
					dp[i][j] = min(dp[i - 1][j - 1] 
					 + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
					  dp[i - 1][j] + 1, 
					  dp[i][j - 1] + 1);
				}
			}
		}
	
		return dp[x.length()][y.length()];
	}
}