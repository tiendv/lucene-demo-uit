/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DemoSpellchecker;

import AutoComplete.AutocompleteDTO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class LuceneSpellSearcher {

    public LuceneSpellSearcher() {
    }
    private static final String INDEX_DIR = "C://INDEX-SUGGESTION";
    /*
     public static void main(String[] args) throws IOException, Exception {
     //String KeyTest = "Computer Program";
     String keyword = "C. Lee Gil";
     keyword = keyword.toLowerCase();
     keyword = keyword.replaceAll("[()-]", " ").replace(".", "");
     System.out.println(keyword);
     int Distance = 8;//keyword.split(" ").length;
     LuceneSpellChecker spellChecker = new LuceneSpellChecker();
     spellChecker.setSpellIndexDir(INDEX_DIR);
     spellChecker.setBoostEdges(true);
     spellChecker.setEditDistanceCutoff(Distance);
     spellChecker.setSoundexDiffCutoff(4);
     spellChecker.initSearch();

     //--------------------------------------
     //LuceneSpellChecker.AnalysisResult result = spellChecker.analyze(KeyTest);
     //System.out.println(result.gram3s);

     List<String> list = spellChecker.suggestSimilar(keyword, 10);
     System.out.println(list);

     spellChecker.destroy();
     }
     */

    public ArrayList<AutocompleteDTO> suggest(String keyword) throws Exception {
        //String keyword = "time day system";
        ArrayList<AutocompleteDTO> List = new ArrayList<AutocompleteDTO>();
        keyword = keyword.toLowerCase();
        String delims = "[ ,?!()-.]+";
        keyword = keyword.replaceAll(delims, " ");
        System.out.println(keyword);
        //int Distance = keyword.length()/4;
        int Distance = 5;

        LuceneSpellChecker spellChecker = new LuceneSpellChecker();
        spellChecker.setSpellIndexDir(INDEX_DIR);
        spellChecker.setBoostEdges(true);
        spellChecker.setEditDistanceCutoff(Distance);
        spellChecker.setSoundexDiffCutoff(4);
        spellChecker.initSearch();

        //--------------------------------------
        //LuceneSpellChecker.AnalysisResult result = spellChecker.analyze(KeyTest);
        //System.out.println(result.gram3s);

        List.addAll(spellChecker.suggestSimilar(keyword));

        spellChecker.destroy();
        return List;
    }
}
