import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TradeStock {
    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Incorrect number of arguments. Please run as java TradeStock {binary file} {number representation of algorithm to use}");
            System.out.println("For example, \"java Tradestock mystock.bin 2\", which would run the DivCon_linear algorithm on a file mystock.bin");
            System.out.println("The options for algorithms (not case sensitive) are: \n\t1. DivCon_nlogn, \n\t2. DivCon_linear, \n\t3. DecCon_linear, \n\t4. all");
            return;
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(args[0]))) {
            int n = dis.readInt();
            double[] prices = new double[n];
            for (int i = 0; i < n; i++) {
                prices[i] = dis.readFloat();
            }
    
            if(n<2){
                System.out.println("Insufficient data to perform trading analysis.");
                return;
            }

            System.out.printf("Jack Casey\n%s", args[0]);
            String algorithm = args[1].toLowerCase();
            switch (algorithm){
                case "1":
                    DivConNlogN(n, prices);
                    break;
                case "2":
                    DivConLinear(n, prices);
                    break;
                case "3":
                    DecConLinear(n, prices);
                    break;
                case "4":
                    DivConNlogN(n, prices);
                    DivConLinear(n, prices);
                    DecConLinear(n, prices);
                    break;
                default:
                    System.out.println("Invalid algorithm specified. Please choose one of the following: \n\t1. DivCon_nlogn, \n\t2. DivCon_linear, \n\t3. DecCon_linear, \n\t4. all");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the binary file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    public static void DivConNlogN(int length, double[] prices) {
        int[] result = divConNlogN(prices, 0, length - 1);
        int buyPos = result[0];
        int sellPos = result[1];
        double profit = prices[sellPos] - prices[buyPos];
        System.out.printf("\nTheta(nlogn) Divide and Conquer\n\t%d, %d, %.4f", buyPos, sellPos, profit);
    }
    
    private static int[] divConNlogN(double[] prices, int left, int right) {
        if (left == right) {
            //this code is awful, but I didn't feel like refactoring my base case when I found out we couldn't buy and sell on the same day
            if(left < prices.length-1){
                right++;
            } else if (right == prices.length - 1){
                left--;
            }
            return new int[]{left, right};
        }
    
        int mid = (right+left)  / 2;
        int[] leftResult = divConNlogN(prices, left, mid);
        int[] rightResult = divConNlogN(prices, mid + 1, right);
        int[] crossResult = maxCrossingProfit(prices, left, mid, right);
    
        if (prices[leftResult[1]] - prices[leftResult[0]] >= prices[rightResult[1]] - prices[rightResult[0]]
                && prices[leftResult[1]] - prices[leftResult[0]] >= prices[crossResult[1]] - prices[crossResult[0]]) {
            return leftResult;
        } else if (prices[rightResult[1]] - prices[rightResult[0]] >= prices[leftResult[1]] - prices[leftResult[0]]
                && prices[rightResult[1]] - prices[rightResult[0]] >= prices[crossResult[1]] - prices[crossResult[0]]) {
            return rightResult;
        } else {
            return crossResult;
        }
    }
    
    private static int[] maxCrossingProfit(double[] prices, int left, int mid, int right) {
        double leftMin = prices[mid];
        int leftPos = mid;
        for (int i = mid; i >= left; i--) {
            if (prices[i] < leftMin) {
                leftMin = prices[i];
                leftPos = i;
            }
        }
    
        double rightMax = prices[mid+1];
        int rightPos = mid + 1;
        for (int i = mid + 1; i <= right; i++) {
            if (prices[i] > rightMax) {
                rightMax = prices[i];
                rightPos = i;
            }
        }
    
        return new int[]{leftPos, rightPos};
    }
    
    public static void DivConLinear(int length, double[] prices) {
        double[] result = divConLinear(prices, 0, length - 1);
        int buyPos = (int) result[0];
        int sellPos = (int) result[1];
        double profit = (double) result[2];
        System.out.printf("\nTheta(n) Divide and Conquer\n\t%d, %d, %.4f", buyPos, sellPos, profit);
    }
    
    private static double[] divConLinear(double[] prices, int start, int end) {
        int buyIndex, sellIndex, minLeftIndex, maxRightIndex;
        double maxProfit = 0;
        double[] result = new double[5];
    
        if (end - start == 1) {
            buyIndex = start;
            sellIndex = end;
            maxProfit = prices[sellIndex] - prices[buyIndex];
    
            minLeftIndex = prices[start] <= prices[end] ? start : end;
            maxRightIndex = prices[start] <= prices[end] ? end : start;
    
            result[0] = buyIndex;
            result[1] = sellIndex;
            result[2] = maxProfit;
            result[3] = minLeftIndex;
            result[4] = maxRightIndex;
        } else if (end - start == 2) {
            double profit1 = prices[end] - prices[start];
            double profit2 = prices[end - 1] - prices[start];
            double profit3 = prices[end] - prices[start + 1];
    
            if (profit1 >= profit2 && profit1 >= profit3) {
                buyIndex = start;
                sellIndex = end;
                maxProfit = profit1;
            } else if (profit2 >= profit3) {
                buyIndex = start;
                sellIndex = end - 1;
                maxProfit = profit2;
            } else {
                buyIndex = start + 1;
                sellIndex = end;
                maxProfit = profit3;
            }
    
            double minLeft = Math.min(prices[start], Math.min(prices[end - 1], prices[end]));
            double maxRight = Math.max(prices[start], Math.max(prices[end - 1], prices[end]));
    
            minLeftIndex = prices[start] == minLeft ? start : (prices[end - 1] == minLeft ? end - 1 : end);
            maxRightIndex = prices[start] == maxRight ? start : (prices[end - 1] == maxRight ? end - 1 : end);
    
            result[0] = buyIndex;
            result[1] = sellIndex;
            result[2] = maxProfit;
            result[3] = minLeftIndex;
            result[4] = maxRightIndex;
        } else {
            double[] leftResult = new double[5];
            double[] rightResult = new double[5];
    
            int mid = (start + end) / 2;
            leftResult = divConLinear(prices, start, mid);
            rightResult = divConLinear(prices, mid + 1, end);
    
            double leftProfit = (double) leftResult[2];
            double rightProfit = (double) rightResult[2];
            double crossProfit = prices[(int) rightResult[4]] - prices[(int) leftResult[3]];
    
            if (leftProfit >= rightProfit && leftProfit >= crossProfit) {
                buyIndex = (int) leftResult[0];
                sellIndex = (int) leftResult[1];
                maxProfit = leftProfit;
            } else if (rightProfit >= crossProfit) {
                buyIndex = (int) rightResult[0];
                sellIndex = (int) rightResult[1];
                maxProfit = rightProfit;
            } else {
                buyIndex = (int) leftResult[3];
                sellIndex = (int) rightResult[4];
                maxProfit = crossProfit;
            }
    
            minLeftIndex = prices[(int) leftResult[3]] <= prices[(int) rightResult[3]] ? (int) leftResult[3] : (int) rightResult[3];
            maxRightIndex = prices[(int) leftResult[4]] >= prices[(int) rightResult[4]] ? (int) leftResult[4] : (int) rightResult[4];
    
            result[0] = buyIndex;
            result[1] = sellIndex;
            result[2] = maxProfit;
            result[3] = minLeftIndex;
            result[4] = maxRightIndex;
        }
        return result;
    }
    
    public static void DecConLinear(int length, double[] prices) {
        double min = prices[0], max = prices[1], profit = 0;
        int buyPos = 0, sellPos = 1, minIndex = 0;
        for (int i = 1; i < length; i++) {
            if (prices[i] < min) {
                min = prices[i];
                minIndex = i;
            }
            if (prices[i] - min > profit) {
                max = prices[i];
                buyPos = minIndex;
                sellPos = i;
                profit = max - min;
            }
        }
        profit = prices[sellPos] - prices[buyPos];
        System.out.printf("\nTheta(n) Decrease and Conquer\n\t%d, %d, %.4f", buyPos,sellPos,profit);
    }
}
