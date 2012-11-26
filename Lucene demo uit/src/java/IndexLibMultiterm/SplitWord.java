/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexLibMultiterm;

import java.io.IOException;

/**
 *
 * @author dungit
 */
public class SplitWord {

    static String delims = "[ .,?!()-]+";
    static String[] st = null;
    public static void main(String[] args) throws IOException {
        String word="Information Flow - IF";
        st = word.split(delims);
        for(String Temp:st)
        {
            System.out.println(Temp+"//");
        }
    }
}
