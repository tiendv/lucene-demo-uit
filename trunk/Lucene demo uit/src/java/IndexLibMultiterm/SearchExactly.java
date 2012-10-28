/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexLibMultiterm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author Dungit86
 */
public class SearchExactly {

    static String[] StopWord = {"a", "an", "and", "are", "as", "at", "be", "but", "by",
        "for", "if", "in", "into", "is", "it",
        "no", "not", "of", "on", "or", "such",
        "that", "the", "their", "then", "there", "these",
        "they", "this", "to", "was", "will", "with"
    };
    static List<String> StopWordArray = Arrays.asList(StopWord);

    public static void main(String[] args) throws IOException {
        String Keyword = "SIGAL - International Symposium on Algorithms";
        String Dictionary = "C://INDEX-AUTOCOMPLETE";
        Directory Directory = new SimpleFSDirectory(new File(Dictionary));
        IndexReader indexReader = IndexReader.open(Directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        BooleanQuery blQuery = new BooleanQuery();

        Keyword = Keyword.toLowerCase();

        String delims = "[ .,?!-]+";
        String[] st = Keyword.split(delims);
        int NumberTerm = 1;
        int length = st.length;
        for (int i = 0; i < length; i++) {
            if (!StopWordArray.contains(st[i])) {
                Term term = new Term("term" + Integer.toString(NumberTerm), st[i]);
                PhraseQuery query = new PhraseQuery();
                query.add(term);
                blQuery.add(query, BooleanClause.Occur.MUST);
            }
            NumberTerm++;
        }
        TopDocs hits = indexSearcher.search(blQuery, null, 1000);

        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            //if (doc.get("keyword").split(delims).length == length) {
            System.out.println(i + "  " + doc.get("keyword"));
            //}
        }
    }
}
