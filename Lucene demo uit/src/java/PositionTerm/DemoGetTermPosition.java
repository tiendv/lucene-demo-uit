/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PositionTerm;

/**
 *
 * @author dungit
 */
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermVectorMapper;
import org.apache.lucene.index.TermVectorOffsetInfo;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.spans.Spans;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import org.apache.lucene.util.Version;


//Not thread-safe
/**
 *  This class is for demonstration purposes only.  No warranty, guarantee, etc. is implied.
 *
 * This is not production quality code!
 *
 *
 **/
public class DemoGetTermPosition {
  public static String[] DOCS = {
          "The quick red fox jumped over the lazy brown dogs.",
          "Mary had a little lamb whose fleece was white as snow.",
          "Moby Dick is a story of a whale and a man obsessed.",
          "The robber wore a black fleece jacket and a baseball cap.",
          "The English Springer Spaniel is the best of all dogs."
  };

  public static void main(String[] args) throws IOException {
    RAMDirectory ramDir = new RAMDirectory();
    //Index some made up content
    IndexWriter writer = new IndexWriter(ramDir, new StandardAnalyzer(Version.LUCENE_36), true, IndexWriter.MaxFieldLength.UNLIMITED);
    for (int i = 0; i < DOCS.length; i++) {
      Document doc = new Document();
      Field id = new Field("id", "doc_" + i, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
      doc.add(id);
      //Store both position and offset information
      Field text = new Field("content", DOCS[i], Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
      doc.add(text);
      writer.addDocument(doc);
    }
    writer.close();
    //Get a searcher
    IndexSearcher searcher = new IndexSearcher(ramDir);
    // Do a search using SpanQuery
    SpanTermQuery fleeceQ = new SpanTermQuery(new Term("content", "fleece"));
    TopDocs results = searcher.search(fleeceQ, 10);
    for (int i = 0; i < results.scoreDocs.length; i++) {
      ScoreDoc scoreDoc = results.scoreDocs[i];
      System.out.println("Score Doc: " + scoreDoc);
    }
    IndexReader reader = searcher.getIndexReader();
    Spans spans = fleeceQ.getSpans(reader);
    WindowTermVectorMapper tvm = new WindowTermVectorMapper();
    int window = 2;//get the words within two of the match
    while (spans.next() == true) {
      System.out.println("Doc: " + spans.doc() + " Start: " + spans.start() + " End: " + spans.end());
      //build up the window
      tvm.start = spans.start() - window;
      tvm.end = spans.end() + window;
      reader.getTermFreqVector(spans.doc(), "content", tvm);
      for (Object entry : tvm.entries.values()) {
        System.out.println("Entry: " + entry);
      }
      //clear out the entries for the next round
      tvm.entries.clear();
    }
  }

}
class WindowTermVectorMapper extends TermVectorMapper {

  int start;
  int end;
  LinkedHashMap entries = new LinkedHashMap();

  public void map(String term, int frequency, TermVectorOffsetInfo[] offsets, int[] positions) {
    for (int i = 0; i < positions.length; i++) {//unfortunately, we still have to loop over the positions
      //we'll make this inclusive of the boundaries
      if (positions[i] >= start && positions[i] < end){
        WindowEntry entry = (WindowEntry) entries.get(term);
        if (entry == null) {
          entry = new WindowEntry(term);
          entries.put(term, entry);
        }
        entry.positions.add(positions[i]);
      }
    }
  }

  public void setExpectations(String field, int numTerms, boolean storeOffsets, boolean storePositions) {
    // do nothing for this example
    //See also the PositionBasedTermVectorMapper.
  }

}

class WindowEntry{
  String term;
  List positions = new ArrayList();//a term could appear more than once w/in a position

  WindowEntry(String term) {
    this.term = term;
  }

  @Override
  public String toString() {
    return "WindowEntry{" + "term='" + term +", positions=" + positions +'}';
  }
}
