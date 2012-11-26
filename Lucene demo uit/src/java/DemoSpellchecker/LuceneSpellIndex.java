/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DemoSpellchecker;

import DemoSpellchecker.LuceneSpellChecker.AnalysisResult;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author Dungit86
 */
public class LuceneSpellIndex {

    private static final String INDEX_DIR = "C://INDEX-SUGGESTION";
    private static final String delims = "[ ,?!()-.]+";

    public static void main(String[] args) throws IOException, Exception {

        String KeywordDir = "C://INDEX-KEYWORD";
        String AuthorDir = "C://INDEX-AUTHOR";
        LuceneSpellChecker spellChecker = new LuceneSpellChecker();
        spellChecker.setSpellIndexDir(INDEX_DIR);
        spellChecker.initIndex();

        //---------------------------------------
        Directory Directory = new SimpleFSDirectory(new File(KeywordDir));
        IndexReader indexReader = IndexReader.open(Directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        BooleanQuery blQuery = new BooleanQuery();
        Query query = new MatchAllDocsQuery();
        blQuery.add(query, BooleanClause.Occur.SHOULD);
        TopDocs hits = indexSearcher.search(blQuery, null, Integer.MAX_VALUE);
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            String keyword = doc.get("keyword");
            
            System.out.println(i + keyword);

            spellChecker.addToDictionary(keyword, "keyword");
        }
        //----------------------------------------

        Directory = new SimpleFSDirectory(new File(AuthorDir));
        indexReader = IndexReader.open(Directory);
        indexSearcher = new IndexSearcher(indexReader);

        blQuery = new BooleanQuery();
        query = new MatchAllDocsQuery();
        blQuery.add(query, BooleanClause.Occur.SHOULD);

        TopDocs hits1 = indexSearcher.search(blQuery, null, Integer.MAX_VALUE);
        for (int i = 0; i < hits1.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits1.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            String keyword = doc.get("authorName");
            
            System.out.println(i + keyword);
            
            spellChecker.addToDictionary(keyword, "author");
        }


        spellChecker.destroy();

    }
}
