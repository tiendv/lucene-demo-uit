/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PositionTerm;

/**
 *
 * @author Dungit86
 */
import AutoComplete.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

public class LuceneSearcherForSeachTerm {

    public String fileDirectory0 = null;
    public String fileDirectory1 = null;
    public String fileDictionary = null;
    public int count = 0;
    private IndexSearcher indexSearcher = null;
    //public ArrayList<Integer> ListDocId = null;

    public LuceneSearcherForSeachTerm() {
    }

    public LuceneSearcherForSeachTerm(String Dictionary, String lucenePath, String lucenePath1) {
        this.fileDictionary = Dictionary;
        this.fileDirectory0 = lucenePath;
        this.fileDirectory1 = lucenePath1;
    }

    public LuceneSearcherForSeachTerm(String lucenePath) {
        this.fileDirectory0 = lucenePath;
    }

    private IndexSearcher getLuceneSearcher(String fileDirectory) throws CorruptIndexException, IOException {
        Directory directory = new SimpleFSDirectory(new File(fileDirectory));
        IndexReader indexReader = IndexReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);
        return indexSearcher;
    }

    public ArrayList<AutocompleteDTO> search(String queryText, String[] ListString)
            throws CorruptIndexException, IOException, ParseException {
        IndexSearcher indexSearcher0 = getLuceneSearcher(this.fileDirectory0);
        IndexSearcher indexSearcher1 = getLuceneSearcher(this.fileDirectory1);
        indexSearcher0.setDefaultFieldSortScoring(true, false);
        indexSearcher1.setDefaultFieldSortScoring(true, false);

        ArrayList<AutocompleteDTO> list = new ArrayList<AutocompleteDTO>();
        list.addAll(GetPosition(queryText, ListString, "keyword", indexSearcher0, "idKeyword"));
        //list.addAll(GetPosition(queryText, "authorName", indexSearcher1, "idAuthor"));
        return list;
    }

    public ArrayList<AutocompleteDTO> GetPosition(String Term, String[] st, String Field, IndexSearcher indexSearcher, String getID)
            throws ParseException, IOException {
        ArrayList<AutocompleteDTO> List = new ArrayList<AutocompleteDTO>();
        SpanTermQuery fleeceQ = new SpanTermQuery(new Term(Field, Term));
        BooleanQuery booleanQuery = new BooleanQuery();

        Query query = new PrefixQuery(new Term(Field, Term));
        booleanQuery.add(query, BooleanClause.Occur.MUST);
        for (int i = 1; i < st.length; i++) {
            Term term1 = new Term(Field, st[i]);
            query = new PrefixQuery(term1);
            booleanQuery.add(query, BooleanClause.Occur.MUST);
        }

        TopDocs hits = indexSearcher.search(booleanQuery, null, 100, Sort.RELEVANCE);
        IndexReader reader = indexSearcher.getIndexReader();
        
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            //Document doc = indexSearcher.doc(scoreDoc.doc);
            //System.out.println(doc.get(Field));
            //System.out.println(scoreDoc.doc);
            Spans spans = fleeceQ.getSpans(reader);
            while (spans.next() == true) {
                if (spans.start() == 0) 
                {
                    if(spans.doc()==scoreDoc.doc)
                    {
                    Document doc1 = indexSearcher.doc(spans.doc());
                    System.out.println("@@"+doc1.get(Field));
                    System.out.println(spans.doc());
                    }
                }
            }
        }
        
        //System.out.println("----------------------------------------");
        /*
        while (spans.next() == true) {
                if (spans.start() == 0 )//&& spans.doc() == scoreDoc.doc) 
                {
                    Document doc = indexSearcher.doc(spans.doc());
                    System.out.println(doc.get(Field));
                    System.out.println(spans.doc());
                }
            }*/
        //System.out.println("@@@@@@");


        return List;
    }

    public ArrayList<String> AutocompleteForTerm(String Term, String key)
            throws ParseException, IOException {
        IndexSearcher indexDictionary = getLuceneSearcher(this.fileDictionary);
        BooleanQuery booleanQuery = new BooleanQuery();
        Term term1 = new Term(key, Term);
        Query query = new PrefixQuery(term1);
        //booleanQuery.add(query, BooleanClause.Occur.MUST);
        ArrayList<String> ListTerm = new ArrayList<String>();
        TopDocs hits = indexDictionary.search(query, null, 1000);
        int i = 0;
        for (i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexDictionary.doc(scoreDoc.doc);
            ListTerm.add(doc.get(key));
            //System.out.println(doc.get(key));
        }
        System.out.println(i);
        return ListTerm;
    }
}
