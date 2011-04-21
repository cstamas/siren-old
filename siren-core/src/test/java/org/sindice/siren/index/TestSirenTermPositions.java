/**
 * Copyright 2009, Renaud Delbru
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * @project siren
 * @author Renaud Delbru [ 21 Apr 2009 ]
 * @link http://renaud.delbru.fr/
 * @copyright Copyright (C) 2009 by Renaud Delbru, All rights reserved.
 */
package org.sindice.siren.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sindice.siren.analysis.TupleAnalyzer;
import org.sindice.siren.search.QueryTestingHelper;
import org.sindice.siren.search.SirenIdIterator;

public class TestSirenTermPositions {

  protected QueryTestingHelper _helper = null;

  @Before
  public void setUp()
  throws Exception {
    _helper = new QueryTestingHelper(new TupleAnalyzer(new StandardAnalyzer(Version.LUCENE_31)));
  }

  @After
  public void tearDown()
  throws Exception {
    _helper.close();
  }

  @Test
  public void testNextSimpleOccurence1()
  throws Exception {
    _helper.addDocument("\"word1\" . ");
    final Term term = new Term("content", "word1");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    assertTrue(termPositions.next());
    assertEquals(0, termPositions.doc());
    assertEquals(0, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());

    assertEquals(1, termPositions.freq());
    assertEquals(0, termPositions.nextPosition());
    assertEquals(0, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(0, termPositions.pos());

    // end of the list, should return NO_MORE_POS
    assertEquals(SirenIdIterator.NO_MORE_POS, termPositions.nextPosition());
  }

  @Test
  public void testNextSimpleOccurence2()
  throws Exception {
    _helper.addDocument("\"word1\" \"word2 word3 word4\" . ");
    final Term term = new Term("content", "word3");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    assertTrue(termPositions.next());
    assertEquals(0, termPositions.doc());
    assertEquals(0, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());

    assertEquals(1, termPositions.freq());
    // here it is assumed that the position of the term in the global position
    // in the flow of tokens (and not within a cell).
    assertEquals(2, termPositions.nextPosition());
    assertEquals(0, termPositions.tuple());
    assertEquals(1, termPositions.cell());
    assertEquals(2, termPositions.pos());

    // end of the list, should return NO_MORE_POS
    assertEquals(SirenIdIterator.NO_MORE_POS, termPositions.nextPosition());
  }

  @Test
  public void testNextMultipleOccurences1()
  throws Exception {
    _helper.addDocument("\"word1 word1 word1\" . ");
    final Term term = new Term("content", "word1");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    assertTrue(termPositions.next());
    assertEquals(0, termPositions.doc());
    assertEquals(0, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());

    assertEquals(3, termPositions.freq());
    assertEquals(0, termPositions.nextPosition());
    assertEquals(0, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(0, termPositions.pos());
    assertEquals(1, termPositions.nextPosition());
    assertEquals(0, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(1, termPositions.pos());
    assertEquals(2, termPositions.nextPosition());
    assertEquals(0, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(2, termPositions.pos());

    // end of the list, should return NO_MORE_POS
    assertEquals(SirenIdIterator.NO_MORE_POS, termPositions.nextPosition());
  }

  @Test
  public void testNextMultipleOccurences2()
  throws Exception {
    _helper.addDocument("\"word1 word2\" \"word1\" . \"word1 word2\" . \"word1\" . ");
    final Term term = new Term("content", "word1");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    assertTrue(termPositions.next());
    assertEquals(0, termPositions.doc());
    assertEquals(0, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());

    assertEquals(4, termPositions.freq());
    assertEquals(0, termPositions.nextPosition());
    assertEquals(0, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(0, termPositions.pos());
    assertEquals(2, termPositions.nextPosition());
    assertEquals(0, termPositions.tuple());
    assertEquals(1, termPositions.cell());
    assertEquals(2, termPositions.pos());
    assertEquals(3, termPositions.nextPosition());
    assertEquals(1, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(3, termPositions.pos());
    assertEquals(5, termPositions.nextPosition());
    assertEquals(2, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(5, termPositions.pos());

    // end of the list, should return NO_MORE_POS
    assertEquals(SirenIdIterator.NO_MORE_POS, termPositions.nextPosition());
  }

  @Test
  public void testSkipTo()
  throws Exception {
    for (int i = 0; i < 64; i++) {
      _helper.addDocument("\"aaa aaa\" . \"aaa\" \"aaa\" .");
      _helper.addDocument("\"aaa bbb\" . \"aaa ccc\" .");
    }

    final Term term = new Term("content", "aaa");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    termPositions.skipTo(16);
    assertEquals(16, termPositions.doc());
    assertEquals(16, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());
    assertEquals(4, termPositions.freq());

    termPositions.skipTo(33, 1);
    assertEquals(33, termPositions.doc());
    assertEquals(33, termPositions.entity());
    assertEquals(1, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(2, termPositions.pos());
    assertEquals(2, termPositions.freq());

    termPositions.skipTo(96, 1, 1);
    assertEquals(96, termPositions.doc());
    assertEquals(96, termPositions.entity());
    assertEquals(1, termPositions.tuple());
    assertEquals(1, termPositions.cell());
    assertEquals(3, termPositions.pos());
    assertEquals(4, termPositions.freq());
  }

  /**
   * If the entity, tuple and cell are not found, it should return the first
   * match that is greater than the target. (SRN-17)
   */
  @Test
  public void testSkipToNotFound()
  throws Exception {
    for (int i = 0; i < 32; i++) {
      _helper.addDocument("\"aaa aaa\" . \"aaa\" \"aaa\" .");
      _helper.addDocument("\"aaa bbb\" . \"aaa ccc\" . \"aaa bbb\" . \"aaa ccc\" \"aaa bbb\" . ");
    }

    final Term term = new Term("content", "bbb");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    // Should move to the next entity, without updating tuple and cell
    // information
    assertTrue(termPositions.skipTo(16));
    assertEquals(17, termPositions.doc());
    assertEquals(17, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());
    assertEquals(3, termPositions.freq());

    // Should jump to the third tuples
    assertTrue(termPositions.skipTo(17, 1));
    assertEquals(17, termPositions.doc());
    assertEquals(17, termPositions.entity());
    assertEquals(2, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(5, termPositions.pos());
    assertEquals(3, termPositions.freq());

    // Should jump to the second cell
    assertTrue(termPositions.skipTo(17, 3, 0));
    assertEquals(17, termPositions.doc());
    assertEquals(17, termPositions.entity());
    assertEquals(3, termPositions.tuple());
    assertEquals(1, termPositions.cell());
    assertEquals(9, termPositions.pos());
    assertEquals(3, termPositions.freq());
  }

  @Test
  public void testSkipToAtSameEntity()
  throws Exception {
    for (int i = 0; i < 64; i++) {
      _helper.addDocument("\"aaa aaa\" . \"aaa\" \"aaa\" .");
      _helper.addDocument("\"aaa bbb\" . \"aaa ccc\" .");
    }

    final Term term = new Term("content", "aaa");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    termPositions.skipTo(16);
    assertEquals(16, termPositions.doc());
    assertEquals(16, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());
    assertEquals(4, termPositions.freq());

    termPositions.skipTo(16, 1);
    assertEquals(16, termPositions.doc());
    assertEquals(16, termPositions.entity());
    assertEquals(1, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(2, termPositions.pos());
    assertEquals(4, termPositions.freq());

    termPositions.skipTo(16, 1, 1);
    assertEquals(16, termPositions.doc());
    assertEquals(16, termPositions.entity());
    assertEquals(1, termPositions.tuple());
    assertEquals(1, termPositions.cell());
    assertEquals(3, termPositions.pos());
    assertEquals(4, termPositions.freq());
  }

  @Test
  public void testSkipToEntityNextPosition()
  throws Exception {
    for (int i = 0; i < 32; i++) {
      _helper.addDocument("\"aaa aaa\" . \"aaa\" \"aaa\" .");
      _helper.addDocument("\"aaa bbb\" . \"aaa ccc\" .");
    }

    final Term term = new Term("content", "aaa");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    termPositions.skipTo(16);
    assertEquals(16, termPositions.doc());
    assertEquals(16, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());
    assertEquals(4, termPositions.freq());

    for (int i = 0; i < termPositions.freq(); i++) {
      assertEquals(i, termPositions.nextPosition());
    }
    assertEquals(SirenIdIterator.NO_MORE_POS, termPositions.nextPosition());
  }

  @Test
  public void testSkipToCellNextPosition()
  throws Exception {
    for (int i = 0; i < 32; i++) {
      _helper.addDocument("\"aaa aaa\" . \"aaa\" \"aaa\" .");
      _helper.addDocument("\"aaa bbb\" . \"aaa ccc\" .");
    }

    final Term term = new Term("content", "aaa");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    termPositions.skipTo(16, 1, 0);
    assertEquals(16, termPositions.doc());
    assertEquals(16, termPositions.entity());
    assertEquals(1, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(2, termPositions.pos());
    assertEquals(4, termPositions.freq());

    assertEquals(3, termPositions.nextPosition());
    assertEquals(1, termPositions.tuple());
    assertEquals(1, termPositions.cell());
    assertEquals(3, termPositions.pos());

    // end of the list, should return NO_MORE_POS
    assertEquals(SirenIdIterator.NO_MORE_POS, termPositions.nextPosition());
  }

  @Test
  public void testSkipToNext()
  throws Exception {
    for (int i = 0; i < 32; i++) {
      _helper.addDocument("\"aaa aaa\" . \"aaa\" \"aaa\" .");
      _helper.addDocument("\"aaa bbb\" . \"aaa ccc\" .");
    }

    final Term term = new Term("content", "aaa");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    termPositions.next();
    assertEquals(0, termPositions.doc());
    assertEquals(0, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());
    assertEquals(4, termPositions.freq());

    termPositions.skipTo(16);
    assertEquals(16, termPositions.doc());
    assertEquals(16, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());
    assertEquals(4, termPositions.freq());

    termPositions.next();
    assertEquals(17, termPositions.doc());
    assertEquals(17, termPositions.entity());
    assertEquals(-1, termPositions.tuple());
    assertEquals(-1, termPositions.cell());
    assertEquals(-1, termPositions.pos());
    assertEquals(2, termPositions.freq());
  }

  @Test
  public void testSkipToNonExistingEntityTupleCell()
  throws Exception {
    for (int i = 0; i < 16; i++) {
      _helper.addDocument("\"aaa aaa\" . \"aaa\" \"aaa\" .");
      _helper.addDocument("\"aaa bbb\" . \"aaa ccc\" .");
    }

    final Term term = new Term("content", "aaa");
    final IndexReader reader = _helper.getIndexReader();
    final SirenTermPositions termPositions = new SirenTermPositions(reader.termPositions(term));

    // does not exist, should skip to entity 17 and to the first cell
    assertTrue(termPositions.skipTo(16, 3, 2));
    assertEquals(17, termPositions.doc());
    assertEquals(17, termPositions.entity());
    assertEquals(0, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(-1, termPositions.dataset());
    assertEquals(0, termPositions.pos());

    // does not exist, should skip to entity 19 and to the first cell
    assertTrue(termPositions.skipTo(18, 2, 2));
    assertEquals(19, termPositions.doc());
    assertEquals(19, termPositions.entity());
    assertEquals(0, termPositions.tuple());
    assertEquals(0, termPositions.cell());
    assertEquals(-1, termPositions.dataset());
    assertEquals(0, termPositions.pos());

    assertFalse(termPositions.skipTo(31, 2, 0)); // does not exist, reach end of list: should return false
  }

}
