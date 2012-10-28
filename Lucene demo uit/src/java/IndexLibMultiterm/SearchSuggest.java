/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexLibMultiterm;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author Dungit86
 */
public class SearchSuggest {
    public static void main(String[] args) throws IOException {
        String Keyword = "Information Retrie";
        String Dictionary = "C://INDEX-AUTOCOMPLETE";

        Directory Directory = new SimpleFSDirectory(new File(Dictionary));
        IndexReader indexReader = IndexReader.open(Directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        BooleanQuery blQuery = new BooleanQuery();

        Keyword = Keyword.toLowerCase();

        String delims = "[ .,?!-]+";
        String[] st = Keyword.split(delims);
        int NumberTerm = 1;
        for (int i = 0; i < st.length; i++) {
            System.out.println(st[i]);
            Term term = new Term("term" + Integer.toString(NumberTerm), st[i]);
            Query query = new FuzzyQuery(term,0.6f);
            blQuery.add(query, BooleanClause.Occur.MUST);
            NumberTerm++;
        }
        TopDocs hits = indexSearcher.search(blQuery, null, 1000);

        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(i + "  " + doc.get("keyword"));
        }
    }
}
