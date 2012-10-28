/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DemoSort;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

/**
 *
 * @author dungit
 */
public class SortingExample {

    private Directory directory;

    public SortingExample(Directory directory) {
        this.directory = directory;
    }

    public void displayResults(Query query, Sort sort)
            throws IOException {
        IndexSearcher searcher = new IndexSearcher(directory);
        searcher.setDefaultFieldSortScoring(true, false);
        
        TopDocs results = searcher.search(query, null, 20, Sort.RELEVANCE);
        
        System.out.println("\nResults for: " + query.toString() + " sorted by " + sort);
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        DecimalFormat scoreFormatter = new DecimalFormat("0.######");
        for (ScoreDoc sd : results.scoreDocs) {
            int docID = sd.doc;
            float score = sd.score;
            Document doc = searcher.doc(docID);
            out.println(scoreFormatter.format(score)+": "+ sd.score+ ": "+ doc.get("keyword"));
            //out.println(searcher.explain(query, docID));
        }
        searcher.close();

    }
}