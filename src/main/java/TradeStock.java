import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TradeStock {
    public static void main(String[] args) throws FileNotFoundException {
        if(args.length != 2){
            System.out.println("Incorrect number of arguments. Please run as java -jar TradeStock.jar {binary file} {algorithm to use}\n");
            System.out.println("The options for algorithms (not case sensitive) are: DivCon_nlogn, DivCon_linear, DecCon_linear");
        } else {
            Scanner scan = new Scanner(new File(args[0]));
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
        }
    }

    public static void DivCon_NlogN(){

    }

    public static void DivCon_Linear() {
        
    }

    public static void DecCon_Linear() {

    }
}
