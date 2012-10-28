/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoComplete;

/**
 *
 * @author Dungit86
 */
import java.io.File;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.index.TermVectorOffsetInfo;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneSearcher {

    public String fileDirectory0 = null;
    public String fileDirectory1 = null;
    private IndexSearcher indexSearcher = null;

    public LuceneSearcher() {
    }

    public LuceneSearcher(String lucenePath, String lucenePath1) {
        this.fileDirectory0 = lucenePath;
        this.fileDirectory1 = lucenePath1;
    }

    public LuceneSearcher(String lucenePath) {
        this.fileDirectory0 = lucenePath;
    }

    private IndexSearcher getLuceneSearcher(String fileDirectory) throws CorruptIndexException, IOException {
        Directory directory = new SimpleFSDirectory(new File(fileDirectory));
        IndexReader indexReader = IndexReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);
        return indexSearcher;
    }

    public ArrayList<AutocompleteDTO> search(String queryText)
            throws CorruptIndexException, IOException, ParseException {
        IndexSearcher indexSearcher0 = getLuceneSearcher(this.fileDirectory0);
        IndexSearcher indexSearcher1 = getLuceneSearcher(this.fileDirectory1);
        indexSearcher0.setDefaultFieldSortScoring(true, false);
        indexSearcher1.setDefaultFieldSortScoring(true, false);

        ArrayList<AutocompleteDTO> list = new ArrayList<AutocompleteDTO>();
        list.addAll(searchPerKey1(queryText, "keyword", indexSearcher0, "idKeyword"));
        list.addAll(searchPerKey1(queryText, "authorName", indexSearcher1, "idAuthor"));

        return list;
    }
    /*
     * Search for autocomplete
     */
    public ArrayList<AutocompleteDTO> searchPerKey1(String queryText, String key, IndexSearcher indexSearcher, String getID)
            throws ParseException, IOException {
        BooleanQuery booleanQuery = new BooleanQuery();
        String[] st = queryText.split(" ");
        for (String term : st) {
            /*QueryParser parser = new QueryParser(Version.LUCENE_36, key, new StandardAnalyzer(Version.LUCENE_36));
            parser.setLowercaseExpandedTerms(true);
            parser.setFuzzyPrefixLength(1);
            Query query = parser.parse(term);*/
            Term term1 = new Term(key, term);
            //Query query = new WildcardQuery(term1);
            Query query = new PrefixQuery(term1);
            //System.out.println();
            //Query query = new FuzzyQuery(term1, 0.5F);
            booleanQuery.add(query, BooleanClause.Occur.MUST);
        }
        ArrayList<AutocompleteDTO> List = new ArrayList<AutocompleteDTO>();
        TopDocs hits = indexSearcher.search(booleanQuery, null, 100, Sort.RELEVANCE);
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            AutocompleteDTO object = new AutocompleteDTO();
            object.setObjectName(doc.get(key));
            object.setType(key);
            object.setScore(scoreDoc.score);
            object.setIdObject(doc.get(getID));
            List.add(object);
        }
        return List;
    }

    public void doSearch(String db, String querystr) throws IOException, ParseException {
        // 1. Specify the analyzer for tokenizing text.  
        //    The same analyzer should be used as was used for indexing  
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

        Directory index = FSDirectory.open(new File(db));

        // 2. query  
        Query q = new QueryParser(Version.LUCENE_CURRENT, "keyword", analyzer).parse(querystr);

        // 3. search  
        int hitsPerPage = 10;
        IndexSearcher searcher = new IndexSearcher(index, true);
        IndexReader reader = IndexReader.open(index, true);
        searcher.setDefaultFieldSortScoring(true, false);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display term positions, and term indexes   
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {

            int docId = hits[i].doc;
            TermFreqVector tfvector = reader.getTermFreqVector(docId, "keyword");
            TermPositionVector tpvector = (TermPositionVector) tfvector;
            // this part works only if there is one term in the query string,  
            // otherwise you will have to iterate this section over the query terms.  
            int termidx = tfvector.indexOf(querystr);
            int[] termposx = tpvector.getTermPositions(termidx);
            TermVectorOffsetInfo[] tvoffsetinfo = tpvector.getOffsets(termidx);

            for (int j = 0; j < termposx.length; j++) {
                System.out.println("termpos : " + termposx[j]);
            }
            for (int j = 0; j < tvoffsetinfo.length; j++) {
                int offsetStart = tvoffsetinfo[j].getStartOffset();
                int offsetEnd = tvoffsetinfo[j].getEndOffset();
                System.out.println("offsets : " + offsetStart + " " + offsetEnd);
            }

            // print some info about where the hit was found...  
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("path"));
        }

        // searcher can only be closed when there  
        // is no need to access the documents any more.   
        searcher.close();
    }
}
