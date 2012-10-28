/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Spellchecker;

import java.io.File;
import java.io.IOException;
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

    private static final String INDEX_DIR = "C://INDEX-SUGGEST";

    public static void main(String[] args) throws IOException, Exception {
        //String KeyTest = "Computer Program";
        String KeyWrong = "C. Lee Giles";
        //KeyWrong= KeyWrong.replace(".", " ");
        int Distance = 2*KeyWrong.split(" ").length;
        LuceneSpellChecker spellChecker = new LuceneSpellChecker();
        spellChecker.setSpellIndexDir(INDEX_DIR);
        spellChecker.setBoostEdges(true);
        spellChecker.setEditDistanceCutoff(Distance);
        spellChecker.setSoundexDiffCutoff(4);
        spellChecker.initSearch();

        //--------------------------------------
        //LuceneSpellChecker.AnalysisResult result = spellChecker.analyze(KeyTest);
        //System.out.println(result.gram3s);

        List<String> list = spellChecker.suggestSimilar(KeyWrong,10);
        System.out.println(list);

        spellChecker.destroy();

    }
}
