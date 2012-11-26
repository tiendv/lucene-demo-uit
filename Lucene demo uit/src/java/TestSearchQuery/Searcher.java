/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestSearchQuery;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author dungit
 */
public class Searcher {

    public static void main(String[] args) throws IOException {
        String IdOrg = "2";
        String Keyword = "10";
        String Dictionary = "C://INDEX//INDEX-PAPER";

        Directory Directory = new SimpleFSDirectory(new File(Dictionary));
        IndexReader indexReader = IndexReader.open(Directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        BooleanQuery blQuery = new BooleanQuery();

        Term termOrg = new Term("listIdOrg", IdOrg);
        Term termKey = new Term("listIdKeyword", Keyword);

        Query query = new TermQuery(termOrg);
        blQuery.add(query, BooleanClause.Occur.MUST);
        Query query1 = new TermQuery(termKey);
        blQuery.add(query1, BooleanClause.Occur.MUST);

        TopDocs hits = indexSearcher.search(blQuery, Integer.MAX_VALUE);
        System.out.println(hits.totalHits);
        for (int i = 0; i < hits.totalHits; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.get("idPaper"));
        }
        indexReader.close();
        indexSearcher.close();
    }
}
