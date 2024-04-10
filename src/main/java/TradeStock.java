import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TradeStock {
    public static void main(String[] args){
        if(args.length != 2){
            System.out.println("Incorrect number of arguments. Please run as java -jar TradeStock.jar {binary file} {algorithm to use}\n");
            System.out.println("The options for algorithms (not case sensitive) are: DivCon_nlogn, DivCon_linear, DecCon_linear");
            return;
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(args[0]))) {
            int n = dis.readInt();
            float[] prices = new float[n];
            for (int i = 0; i < n; i++) {
                prices[i] = dis.readFloat();
            }
    

            String algorithm = args[1].toLowerCase();
            switch (algorithm){
                case "divcon_nlogn":
                    DivCon_NlogN();
                    break;
                case "divcon_linear":
                    DivCon_Linear();
                    break;
                case "deccon_linear":
                    DecCon_Linear();
                    break;
                default:
                    System.out.println("Invalid algorithm specified. Please choose one of the following: DivCon_nlogn, DivCon_linear, DecCon_linear");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the binary file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        
    }

    public static void DivCon_NlogN(){

    }

    public static void DivCon_Linear() {
        
    }

    public static void DecCon_Linear() {

    }
}
