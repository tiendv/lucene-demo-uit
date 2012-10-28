/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PositionTerm;

import AutoComplete.LuceneSearcher;
import java.io.IOException;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

/**
 *
 * @author Dungit86
 */
public class GetPositionOfTerm {
    /**
     *
     * @param args
     * @throws CorruptIndexException
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args)
            throws CorruptIndexException, IOException, ParseException {
        LuceneSearcher LuceneSearcher =new LuceneSearcher();
        LuceneSearcher.doSearch("C://INDEX-KEYWORD", "time");
    }
}
