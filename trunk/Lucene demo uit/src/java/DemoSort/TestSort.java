/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DemoSort;

import java.io.File;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author dungit
 */
public class TestSort {
    public static void main(String[] args) throws Exception {
        /*
        QueryParser parser = new QueryParser(Version.LUCENE_36,
                "keyword",
                new StandardAnalyzer(
                Version.LUCENE_36));
        BooleanQuery query = new BooleanQuery();
        query.add(parser.parse("time delay"), BooleanClause.Occur.MUST);
        */
        
        String queryText = "time delay";
        BooleanQuery booleanQuery = new BooleanQuery();
        String[] st = queryText.split(" ");
        for (String term : st) {
            Term term1 = new Term("keyword", term + "~");
            Query query = new FuzzyQuery(term1, 0.4F);
            booleanQuery.add(query, BooleanClause.Occur.MUST);
        }
        
        Directory directory = new SimpleFSDirectory(new File("C://INDEX-KEYWORD"));
        SortingExample example = new SortingExample(directory);
        example.displayResults(booleanQuery, Sort.RELEVANCE);
        //example.displayResults(query, Sort.INDEXORDER);
        //example.displayResults(query,new Sort(new SortField("category", SortField.STRING),SortField.FIELD_SCORE));
        //example.displayResults(query,new Sort(new SortField[]{SortField.FIELD_SCORE,new SortField("category", SortField.STRING)}));
        directory.close();
    }
}
