/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IndexLibMultiterm;

import IndexLibFirstTerm.*;
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
        String AuthorDir = "C://INDEX-AUTHOR";
        String Dictionary = "C://INDEX-AUTOCOMPLETE";
        String delims = "[ .,?!()-]+";
        String[] st = null;

        Directory Directory = new SimpleFSDirectory(new File(KeywordDir));
        IndexReader indexReader = IndexReader.open(Directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        BooleanQuery blQuery = new BooleanQuery();
        Query query = new MatchAllDocsQuery();
        blQuery.add(query, BooleanClause.Occur.SHOULD);


        File indexDir = new File(Dictionary);
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        Directory directory = FSDirectory.open(indexDir);
        IndexWriter writer = new IndexWriter(directory, config);

        TopDocs hits = indexSearcher.search(blQuery, null,Integer.MAX_VALUE);
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(i + doc.get("keyword"));

            st = doc.get("keyword").split(delims);

            Document d = new Document();
            d.add(new Field("keyword", doc.get("keyword"), Field.Store.YES, Field.Index.ANALYZED));
            d.add(new Field("IdObject", doc.get("idKeyword"), Field.Store.YES, Field.Index.ANALYZED));
            d.add(new Field("Object", "keyword", Field.Store.YES, Field.Index.ANALYZED));
            int NumberTerm = 1;
            for (String term : st) {
                d.add(new Field("term" + Integer.toString(NumberTerm), term, Field.Store.YES, Field.Index.ANALYZED));
                NumberTerm++;
            }
            writer.addDocument(d);
            d = null;
        }

        Directory = new SimpleFSDirectory(new File(AuthorDir));
        indexReader = IndexReader.open(Directory);
        indexSearcher = new IndexSearcher(indexReader);

        blQuery = new BooleanQuery();
        query = new MatchAllDocsQuery();
        blQuery.add(query, BooleanClause.Occur.SHOULD);

        TopDocs hits1 = indexSearcher.search(blQuery, null,Integer.MAX_VALUE);
        for (int i = 0; i < hits1.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits1.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(i + doc.get("authorName"));
            
            st = doc.get("authorName").split(delims);
            Document d = new Document();
            d.add(new Field("keyword", doc.get("authorName"), Field.Store.YES, Field.Index.ANALYZED));
            d.add(new Field("IdObject", doc.get("idAuthor"), Field.Store.YES, Field.Index.ANALYZED));
            d.add(new Field("Object", "author", Field.Store.YES, Field.Index.ANALYZED));
            int NumberTerm = 1;
            for (String term : st) {
                d.add(new Field("term" + Integer.toString(NumberTerm), term, Field.Store.YES, Field.Index.ANALYZED));
                NumberTerm++;
            }
            writer.addDocument(d);
            d = null;

        }

        writer.close();
    }
}
