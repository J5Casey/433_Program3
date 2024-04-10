import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TradeStock {
    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Incorrect number of arguments. Please run as java -jar TradeStock.jar {binary file} {algorithm to use}\n");
            System.out.println("The options for algorithms (not case sensitive) are: DivCon_nlogn, DivCon_linear, DecCon_linear, all");
            return;
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(args[0]))) {
            int n = dis.readInt();
            float[] prices = new float[n];
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
                case "divcon_nlogn":
                    DivCon_NlogN(n, prices);
                    break;
                case "divcon_linear":
                    DivCon_Linear(n, prices);
                    break;
                case "deccon_linear":
                    DecCon_Linear(n, prices);
                    break;
                case "all":
                    DivCon_NlogN(n, prices);
                    DivCon_Linear(n, prices);
                    DecCon_Linear(n, prices);
                    break;
                default:
                    System.out.println("Invalid algorithm specified. Please choose one of the following: DivCon_nlogn, DivCon_linear, DecCon_linear, all");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the binary file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    public static void DivCon_NlogN(int length, float[] prices) {
        int[] result = divConNlogN(prices, 0, length - 1);
        int buyPos = result[0];
        int sellPos = result[1];
        float profit = prices[sellPos] - prices[buyPos];
        System.out.printf("\nTheta(nlogn) Divide and Conquer\n%d, %d, %.4f", buyPos, sellPos, profit);
    }
    
    private static int[] divConNlogN(float[] prices, int left, int right) {
        if (left == right) {
            return new int[]{left, right};
        }
    
        int mid = (right+left)  / 2;;
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
    
    private static int[] maxCrossingProfit(float[] prices, int left, int mid, int right) {
        float leftMin = prices[mid];
        int leftPos = mid;
        for (int i = mid; i >= left; i--) {
            if (prices[i] < leftMin) {
                leftMin = prices[i];
                leftPos = i;
            }
        }
    
        float rightMax = prices[mid+1];
        int rightPos = mid + 1;
        for (int i = mid + 1; i <= right; i++) {
            if (prices[i] > rightMax) {
                rightMax = prices[i];
                rightPos = i;
            }
        }
    
        return new int[]{leftPos, rightPos};
    }
    
    public static void DivCon_Linear(int length, float[] prices) {
        int[] result = divConLinear(prices, 0, length - 1);
        int buyPos = result[0];
        int sellPos = result[1];
        float profit = prices[sellPos] - prices[buyPos];
        System.out.printf("\nTheta(n) Divide and Conquer\n%d, %d, %.4f", buyPos, sellPos, profit);
    }
    
    private static int[] divConLinear(float[] prices, int left, int right) {
        if (left == right) {
            return new int[]{left, right};
        }
    
        int mid = (right+left)  / 2;
        int[] leftResult = divConLinear(prices, left, mid);
        int[] rightResult = divConLinear(prices, mid + 1, right);
    
        int leftMin = leftResult[0];
        int leftMax = leftResult[1];
        int rightMin = rightResult[0];
        int rightMax = rightResult[1];
    
        float leftProfit = prices[leftMax] - prices[leftMin];
        float rightProfit = prices[rightMax] - prices[rightMin];
    
        int crossMin = Math.min(leftMin, rightMin);
        int crossMax = Math.max(leftMax, rightMax);
        float crossProfit = prices[crossMax] - prices[crossMin];
    
        if (crossProfit > Math.max(leftProfit, rightProfit)) {
            return new int[]{crossMin, crossMax};
        } else if (leftProfit > rightProfit) {
            return leftResult;
        } else {
            return rightResult;
        }
    }
    
    
    public static void DecCon_Linear(int length, float[] prices) {
        float min = prices[0], max = prices[0], profit = 0;
        int buyPos = 0, sellPos = 1, minPos = 0;
        for (int i = 1; i < length; i++) {
            if (prices[i] < min) {
                min = prices[i];
                minPos = i;
            }
            if (prices[i] - min > profit) {
                max = prices[i];
                buyPos = minPos;
                sellPos = i;
                profit = max - min;
            }
        }
        System.out.printf("\nTheta(n) Decrease and Conquer\n%d, %d, %.4f", buyPos,sellPos,profit);
    }
}
