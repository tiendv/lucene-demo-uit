/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoComplete;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author Dungit86
 */
public class SearchInstance {

    public String Dictionary = "C://INDEX-AUTOCOMPLETE";

    public SearchInstance() {
    }

    public ArrayList<AutocompleteDTO> Search(String Keyword) throws IOException {
        ArrayList<AutocompleteDTO> List = new ArrayList<AutocompleteDTO>();

        Directory Directory = new SimpleFSDirectory(new File(this.Dictionary));
        IndexReader indexReader = IndexReader.open(Directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        BooleanQuery blQuery = new BooleanQuery();

        Keyword = Keyword.toLowerCase();

        //System.out.println(Keyword);
        String delims = "[ .,?!-]+";
        String[] st = Keyword.split(delims);
        int NumberTerm = 1;
        
        if (st[0].length() < 4) {
            Term term = new Term("term" + Integer.toString(NumberTerm), st[0]);
            PhraseQuery query = new PhraseQuery();
            query.add(term);
            blQuery.add(query, BooleanClause.Occur.MUST);
            NumberTerm++;
        } else {
            Term term = new Term("term" + Integer.toString(NumberTerm), st[0]);
            Query query = new PrefixQuery(term);
            blQuery.add(query, BooleanClause.Occur.MUST);
            NumberTerm++;
        }
        for (int i = 1; i < st.length; i++) {
            Term term = new Term("term" + Integer.toString(NumberTerm), st[i]);
            Query query = new PrefixQuery(term);
            blQuery.add(query, BooleanClause.Occur.MUST);
            NumberTerm++;
        }
        
        TopDocs hits = indexSearcher.search(blQuery, null, 10);

        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = indexSearcher.doc(scoreDoc.doc);
            //System.out.println(i + "  " + doc.get("keyword"));
            AutocompleteDTO object = new AutocompleteDTO();
            object.setObjectName(doc.get("keyword"));
            object.setType(doc.get("Object"));
            object.setScore(scoreDoc.score);
            object.setIdObject(doc.get("idObject"));
            List.add(object);
        }
        return List;
    }
}
