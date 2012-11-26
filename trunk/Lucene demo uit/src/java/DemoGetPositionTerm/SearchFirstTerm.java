/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DemoGetPositionTerm;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

/**
 *
 * @author Dungit86
 */
public class SearchFirstTerm {
    public static void main(String[] args)
            throws CorruptIndexException, IOException, ParseException {
        LuceneSearcherForSeachTerm luceneSearcher = null;
        String Dictionary = "C://spellchecker";
        String lucenePath = "c://INDEX-AUTHOR";
        String lucenePath2 = "c://INDEX-AUTHOR";
        if (luceneSearcher == null) {
            luceneSearcher = new LuceneSearcherForSeachTerm(Dictionary, lucenePath, lucenePath2);
        }
        
        String queryText = "kiem";
        String delims = "[ .,?!]+";
        String[] st = queryText.split(delims);
        System.out.println("Query Term: " + queryText);
        ArrayList<String> ListTerm = luceneSearcher.AutocompleteForTerm(st[0], "word");
        
        System.out.println(ListTerm);
        for(String Term:ListTerm)
        {
            luceneSearcher.search(Term, st);
        }
        //System.out.println(luceneSearcher.ListDocId);
        //System.out.println(luceneSearcher.count);
        /*
        System.out.println("Query Term: " + queryText);
        luceneSearcher.search(queryText);
        * */

    }
}
