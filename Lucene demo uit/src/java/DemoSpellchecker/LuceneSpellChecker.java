/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DemoSpellchecker;

/**
 *
 * @author dungit
 */
import AutoComplete.AutocompleteDTO;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
//import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class LuceneSpellChecker {

    private String spellIndexDir;
    private boolean boostEdges;
    private int editDistanceCutoff = 3;
    private int soundexDiffCutoff = 4;
    private IndexWriter spellIndexWriter;
    private IndexSearcher spellIndexSearcher;
    String delims = "[ ,?!()-.]+";

    public LuceneSpellChecker() {
    }

    public class AnalysisResult {

        public String input;
        public List<String> gram3s = new ArrayList<String>();
        public List<String> gram4s = new ArrayList<String>();
        public String start3 = "";
        public String start4 = "";
        public String end3 = "";
        public String end4 = "";
    }

    public void setBoostEdges(boolean boostEdges) {
        this.boostEdges = boostEdges;
    }

    public void setEditDistanceCutoff(int editDistanceCutoff) {
        this.editDistanceCutoff = editDistanceCutoff;
    }

    public void setSoundexDiffCutoff(int soundexDiffCutoff) {
        this.soundexDiffCutoff = soundexDiffCutoff;
    }

    public void setSpellIndexDir(String spellIndexDir) {
        this.spellIndexDir = spellIndexDir;
    }

    public void initIndex() throws Exception {
        File indexDir = new File(spellIndexDir);
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        Directory directory = FSDirectory.open(indexDir);
        this.spellIndexWriter = new IndexWriter(directory, config);


        /*
         System.out.println(spellIndexDir);
         Directory Directory = new SimpleFSDirectory(new File(spellIndexDir));
         IndexReader indexReader = IndexReader.open(Directory);
         this.spellIndexSearcher = new IndexSearcher(indexReader);
         */
    }

    public void initSearch() throws Exception {
        Directory Directory = new SimpleFSDirectory(new File(spellIndexDir));
        IndexReader indexReader = IndexReader.open(Directory);
        spellIndexSearcher = new IndexSearcher(indexReader);
    }

    public void destroy() throws Exception {
        if (spellIndexWriter != null) {
            spellIndexWriter.close();
        }
        if (spellIndexSearcher != null) {
            spellIndexSearcher.close();
        }
    }

    public void flush() throws Exception {
        spellIndexWriter.optimize();
    }

    public void addToDictionary(String keyword, String objectType) throws IOException {
        Document doc = new Document();
        doc.add(new Field("word", keyword, Store.YES, Index.ANALYZED));
        doc.add(new Field("objectType", objectType, Store.YES, Index.ANALYZED));

        String dictionaryEntry = keyword.toLowerCase();
        dictionaryEntry = dictionaryEntry.replaceAll(delims, " ");
        doc.add(new Field("compare", dictionaryEntry, Store.YES, Index.ANALYZED));

        String End = dictionaryEntry.replace(" ", "0");
        AnalysisResult result = analyze(End);

        for (String gram3 : result.gram3s) {
            doc.add(new Field("gram3", gram3, Store.YES, Index.ANALYZED));
        }
        for (String gram4 : result.gram4s) {
            doc.add(new Field("gram4", gram4, Store.YES, Index.ANALYZED));
        }
        doc.add(new Field("start3", result.start3, Store.YES, Index.ANALYZED));
        doc.add(new Field("start4", result.start4, Store.YES, Index.ANALYZED));
        doc.add(new Field("end3", result.end3, Store.YES, Index.ANALYZED));
        doc.add(new Field("end4", result.end4, Store.YES, Index.ANALYZED));
        spellIndexWriter.addDocument(doc);
    }

    public List<String> suggestSimilar(String input, int maxSuggestions)
            throws IOException, EncoderException {
        String inputToLowerCase = input.toLowerCase();
        AnalysisResult result = analyze(inputToLowerCase);
        //System.out.println(result.gram3s);
        BooleanQuery query = new BooleanQuery();
        addGramQuery(query, "gram3", result.gram3s);
        addGramQuery(query, "gram4", result.gram4s);
        addEdgeQuery(query, "start3", result.start3);
        addEdgeQuery(query, "end3", result.end3);
        addEdgeQuery(query, "start4", result.start4);
        addEdgeQuery(query, "end4", result.end4);

        Set<String> words = new HashSet<String>();
        TopDocs hits = spellIndexSearcher.search(query, maxSuggestions, Sort.RELEVANCE);
        int numHits = hits.scoreDocs.length;
        for (int i = 0; i < numHits; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = spellIndexSearcher.doc(scoreDoc.doc);
            String suggestion = doc.get("word");
            // if the suggestion is the same as the input, ignore it
            /*if (suggestion.equalsIgnoreCase(input)) {
             continue;
             }*/
            // if the suggestion is within the specified levenshtein's distance, include it
            //System.out.println(analyze(suggestion).gram3s);

            int Distance = StringUtils.getLevenshteinDistance(input, suggestion);
            System.out.println(Distance + suggestion);
            if (Distance < editDistanceCutoff) {
                words.add(suggestion);
            }
            // if they sound the same, include it
            /*
             Soundex soundex = new Soundex();
             if (soundex.difference(input, suggestion) >= soundexDiffCutoff) {
             words.add(suggestion);
             }
             */
        }
        List<String> wordlist = new ArrayList<String>();
        wordlist.addAll(words);
        return wordlist.subList(0, Math.min(maxSuggestions, wordlist.size()));
    }

    public ArrayList<AutocompleteDTO> suggestSimilar(String input)
            throws IOException, EncoderException {
        ArrayList<AutocompleteDTO> List = new ArrayList<AutocompleteDTO>();

        String inputToLowerCase = input.toLowerCase();
        inputToLowerCase = inputToLowerCase.replace(" ", "0");
        AnalysisResult result = analyze(inputToLowerCase);
        //System.out.println(result.gram3s);
        BooleanQuery query = new BooleanQuery();
        addGramQuery(query, "gram3", result.gram3s);
        addGramQuery(query, "gram4", result.gram4s);
        addEdgeQuery(query, "start3", result.start3);
        addEdgeQuery(query, "end3", result.end3);
        addEdgeQuery(query, "start4", result.start4);
        addEdgeQuery(query, "end4", result.end4);

        TopDocs hits = spellIndexSearcher.search(query, 10, Sort.RELEVANCE);
        int numHits = hits.scoreDocs.length;
        for (int i = 0; i < numHits; i++) {
            AutocompleteDTO object = new AutocompleteDTO();
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = spellIndexSearcher.doc(scoreDoc.doc);
            String suggestion = doc.get("compare");
            // if the suggestion is the same as the input, ignore it
            /*if (suggestion.equalsIgnoreCase(input)) {
             continue;
             }*/
            // if the suggestion is within the specified levenshtein's distance, include it
            //System.out.println(analyze(suggestion).gram3s);

            int Distance = StringUtils.getLevenshteinDistance(input, suggestion);
            //System.out.println(Distance + suggestion);
            if (Distance < editDistanceCutoff) {
                object.setObjectName(Distance + " " + doc.get("word"));
                object.setType(doc.get("objectType"));
            }
            // if they sound the same, include it
            /*
             Soundex soundex = new Soundex();
             if (soundex.difference(input, suggestion) >= soundexDiffCutoff) {
             words.add(suggestion);
             }
             */
            if (object.getObjectName() != "") {
                List.add(object);
            }
        }
        return List;
    }

    public AnalysisResult analyze(String input) throws IOException {
        AnalysisResult result = new AnalysisResult();
        result.input = input;
        Analyzer analyzer = new NGramAnalyzer(3, 4);
        TokenStream tokenStream = analyzer.tokenStream("dummy", new StringReader(input));
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        while (tokenStream.incrementToken()) {
            String text = charTermAttribute.toString();
            if (text.length() == 3) {
                result.gram3s.add(text);
            } else if (text.length() == 4) {
                result.gram4s.add(text);
            } else {
                continue;
            }
        }
        result.start3 = input.substring(0, Math.min(input.length(), 3));
        result.start4 = input.substring(0, Math.min(input.length(), 4));
        result.end3 = input.substring(Math.max(0, input.length() - 3), input.length());
        result.end4 = input.substring(Math.max(0, input.length() - 4), input.length());
        return result;
    }

    private void addGramQuery(BooleanQuery query, String fieldName, List<String> grams) {
        for (String gram : grams) {
            //System.out.println(fieldName+":"+gram);
            query.add(new TermQuery(new Term(fieldName, gram)), Occur.SHOULD);
        }
    }

    private void addEdgeQuery(BooleanQuery query, String fieldName, String fieldValue) {
        TermQuery start3Query = new TermQuery(new Term(fieldName, fieldValue));
        if (boostEdges) {
            start3Query.setBoost(2.0F);
        }
        query.add(start3Query, Occur.SHOULD);
    }
}
