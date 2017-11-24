package core.Managers;

import java.io.IOException;
import java.util.Scanner;

public class ServerManager {

    public static String runCommand(String command){
        String output = null;
        try{
            Scanner scanner = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A");
            if(scanner.hasNext()) output = scanner.next();
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
        return output;
    }
}
