/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestSearchQuery;

/**
 *
 * @author Dung Doan
 */
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class PhraseSearch {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        Directory index = new RAMDirectory();
        IndexWriter w = new IndexWriter(index, analyzer, true,
                IndexWriter.MaxFieldLength.UNLIMITED);
        addDoc(w, "Lucene in Action");
        addDoc(w, "Lucene for Dummies");
        addDoc(w, "Time Managing Gigabytes");
        addDoc(w, "Gigabytes Managing");
        addDoc(w, "The Art of Computer Science");
        w.close();

        // 2. query  
        String Text = "Managing Gigabytes";
        String delims = "[ .,?!()-]+";
        
        IndexSearcher searcher = new IndexSearcher(index, true);
        PhraseQuery query = new PhraseQuery();
        

        query.add(new Term("title", "managing"), 0);
        query.add(new Term("title", "gigabytes"), 1);
        //query.add(new Term("title", "action"),2 );  
        query.setSlop(0);
        System.out.println("Query: " + query.toString());
        TopDocs hits = searcher.search(query, 10);


        // the "title" arg specifies the default field to use  
        // when no field is explicitly specified in the query.  

        // 3. search  


        // 4. display results  
        System.out.println("Found " + hits.totalHits + " hits.");
        for (int i = 0; i < hits.totalHits; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document d = searcher.doc(scoreDoc.doc);
            System.out.println((i + 1) + ". " + d.get("title"));
        }

        // searcher can only be closed when there  
        // is no need to access the documents any more.   
        searcher.close();
    }

    private static void addDoc(IndexWriter w, String value) throws IOException {
        Document doc = new Document();
        doc.add(new Field("title", value, Field.Store.YES, Field.Index.ANALYZED));
        w.addDocument(doc);
    }
}
