/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DemoParseString;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author dungit
 */
public class contain {
    public static void main(String[] args) throws IOException {
        String ID= "112344 45454";
        if(ID.contains("2"))
            System.out.println("Fail");
        System.out.println("//----------------------------------------");
        ArrayList<String> ListID = new ArrayList<String>();
        ListID.add("12321");
        if(ListID.contains("2"))
        {
            System.out.println("Fail");
        }else{
            System.out.println("True");
        }
    }
}
