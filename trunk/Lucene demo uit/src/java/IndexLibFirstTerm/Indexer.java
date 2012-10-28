/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexLibFirstTerm;

import AutoComplete.AutocompleteDTO;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Dungit86
 */
public class Indexer {

    public static void main(String[] args) throws IOException {
        String KeywordDir = "C://INDEX-KEYWORD";
        String Dictionary = "C://INDEX-KEYWORD-AUTO";

        Directory KeywordDirectory = new SimpleFSDirectory(new File(KeywordDir));
        IndexReader indexReader = IndexReader.open(KeywordDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        BooleanQuery blQuery = new BooleanQuery();
        Query query = new MatchAllDocsQuery();
        blQuery.add(query, BooleanClause.Occur.SHOULD);


        File indexDir = new File(Dictionary);
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        Directory directory = FSDirectory.open(indexDir);
        IndexWriter writer = new IndexWriter(directory, config);

        TopDocs hits = indexSearcher.search(blQuery, null, 1000);
        String delims = "[ .,?!-]+";
        String[] st = null;
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(i + doc.get("keyword"));

            st = doc.get("keyword").split(delims);

            Document d = new Document();
            d.add(new Field("keyword", doc.get("keyword"), Field.Store.YES, Field.Index.ANALYZED));
            d.add(new Field("FirstTermKeyword", st[0], Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(d);
            d = null;
        }
        writer.close();
    }
}
