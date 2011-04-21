/**
 *
 * ------------------------------------------------------------
 *
 * @project solr-plugins
 *
 * Copyright (C) 2007,
 * @author Renaud Delbru [ 25 Apr 2008 ]
 * @link http://renaud.delbru.fr/
 * All rights reserved.
 */
package org.sindice.siren.qparser.analysis;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.sindice.siren.analysis.TupleTokenizer;
import org.sindice.siren.analysis.filter.SirenPayloadFilter;
import org.sindice.siren.analysis.filter.TokenTypeFilter;

public class NTripleTestHelper {

  protected static final String _defaultField = "explicit_content";
  protected static final String _implicitField = "implicit_content";
  protected static final String _ID_FIELD     = "id";

  /**
   * Create a IndexWriter for a RAMDirectoy
   */
  public static IndexWriter createRamIndexWriter(final RAMDirectory dir)
  throws CorruptIndexException, LockObtainFailedException, IOException {
    final IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_31, new SimpleTupleAnalyzer());
    final IndexWriter ramIndexWriter = new IndexWriter(dir, config);
    return ramIndexWriter;
  }

  /**
   * Create a IndexSearcher for a RAMDirectoy
   */
  public static IndexSearcher createRamIndexSearcher(final RAMDirectory dir)
  throws CorruptIndexException, LockObtainFailedException, IOException {
    return new IndexSearcher(dir);
  }

  /**
   * A tuple analyzer especially made for unit test, that only uses the
   * mandatory filter.
   */
  private static class SimpleTupleAnalyzer extends Analyzer {

    /**
     * Builds an analyzer with the default stop words ({@link #STOP_WORDS}).
     */
    public SimpleTupleAnalyzer() {}

    @Override
    public final TokenStream tokenStream(final String fieldName, final Reader reader) {
      final TupleTokenizer stream = new TupleTokenizer(reader, Integer.MAX_VALUE,
        new WhitespaceAnalyzer(Version.LUCENE_31));
      TokenStream result = new TokenTypeFilter(stream, new int[] {TupleTokenizer.BNODE,
                                                                  TupleTokenizer.DOT,
                                                                  TupleTokenizer.DATATYPE,
                                                                  TupleTokenizer.LANGUAGE});
      result = new SirenPayloadFilter(result);
      return result;
    }

    @Override
    public final TokenStream reusableTokenStream(final String fieldName, final Reader reader) throws IOException {
      SavedStreams streams = (SavedStreams) this.getPreviousTokenStream();
      if (streams == null) {
        streams = new SavedStreams();
        this.setPreviousTokenStream(streams);
        streams.tokenStream = new TupleTokenizer(reader, Integer.MAX_VALUE,
          new WhitespaceAnalyzer(Version.LUCENE_31));
        streams.filteredTokenStream = new TokenTypeFilter(streams.tokenStream,
          new int[] {TupleTokenizer.BNODE, TupleTokenizer.DOT,
                     TupleTokenizer.DATATYPE, TupleTokenizer.LANGUAGE});
        streams.filteredTokenStream = new SirenPayloadFilter(streams.filteredTokenStream);
      } else {
        streams.tokenStream.reset(reader);
      }
      return streams.filteredTokenStream;
    }

    private final class SavedStreams {
      TupleTokenizer tokenStream;
      TokenStream filteredTokenStream;
    }

  }

}
