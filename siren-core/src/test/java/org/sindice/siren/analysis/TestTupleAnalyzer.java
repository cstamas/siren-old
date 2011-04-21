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
 * @author Renaud Delbru [ 5 Feb 2009 ]
 * @link http://renaud.delbru.fr/
 * @copyright Copyright (C) 2009 by Renaud Delbru, All rights reserved.
 */
package org.sindice.siren.analysis;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.sindice.siren.analysis.TupleAnalyzer.URINormalisation;
import org.sindice.siren.analysis.attributes.CellAttribute;
import org.sindice.siren.analysis.attributes.TupleAttribute;

public class TestTupleAnalyzer {

  private final TupleAnalyzer _a = new TupleAnalyzer(new StandardAnalyzer(Version.LUCENE_31));

  public TestTupleAnalyzer() {
    _a.setURINormalisation(URINormalisation.FULL);
  }

  public void assertAnalyzesTo(final Analyzer a, final String input,
                                final String[] expected)
  throws Exception {
    this.assertAnalyzesTo(a, input, expected, null);
  }

  public void assertAnalyzesTo(final Analyzer a, final String input,
                                final String[] expectedImages,
                                final String[] expectedTypes)
  throws Exception {
    this.assertAnalyzesTo(a, input, expectedImages, expectedTypes, null);
  }

  public void assertAnalyzesTo(final Analyzer a, final String input,
                                final String[] expectedImages,
                                final String[] expectedTypes,
                                final int[] expectedPosIncrs)
  throws Exception {
    this.assertAnalyzesTo(a, input, expectedImages, expectedTypes, null, null,
      null);
  }

  public void assertAnalyzesTo(final Analyzer a, final String input,
                                final String[] expectedImages,
                                final String[] expectedTypes,
                                final int[] expectedPosIncrs,
                                final int[] expectedTupleID,
                                final int[] expectedCellID)
  throws Exception {
    final TokenStream t = a.reusableTokenStream("", new StringReader(input));

    assertTrue("has TermAttribute", t.hasAttribute(TermAttribute.class));
    final TermAttribute termAtt = t.getAttribute(TermAttribute.class);

    TypeAttribute typeAtt = null;
    if (expectedTypes != null) {
      assertTrue("has TypeAttribute", t.hasAttribute(TypeAttribute.class));
      typeAtt = t.getAttribute(TypeAttribute.class);
    }

    PositionIncrementAttribute posIncrAtt = null;
    if (expectedPosIncrs != null) {
      assertTrue("has PositionIncrementAttribute", t.hasAttribute(PositionIncrementAttribute.class));
      posIncrAtt = t.getAttribute(PositionIncrementAttribute.class);
    }

    TupleAttribute tupleAtt = null;
    if (expectedTupleID != null) {
      assertTrue("has TupleAttribute", t.hasAttribute(TupleAttribute.class));
      tupleAtt = t.getAttribute(TupleAttribute.class);
    }

    CellAttribute cellAtt = null;
    if (expectedCellID != null) {
      assertTrue("has CellAttribute", t.hasAttribute(CellAttribute.class));
      cellAtt = t.getAttribute(CellAttribute.class);
    }

    for (int i = 0; i < expectedImages.length; i++) {

      assertTrue("token "+i+" exists", t.incrementToken());

      assertEquals(expectedImages[i], termAtt.term());

      if (expectedTypes != null) {
        assertEquals(expectedTypes[i], typeAtt.type());
      }

      if (expectedPosIncrs != null) {
        assertEquals(expectedPosIncrs[i], posIncrAtt.getPositionIncrement());
      }

      if (expectedTupleID != null) {
        assertEquals(expectedTupleID[i], tupleAtt.tuple());
      }

      if (expectedCellID != null) {
        assertEquals(expectedCellID[i], cellAtt.cell());
      }
    }

    assertFalse("end of stream", t.incrementToken());
    t.end();
    t.close();
  }

  @Test
  public void testURI()
  throws Exception {
    this.assertAnalyzesTo(_a, "<http://renaud.delbru.fr/>",
      new String[] { "renaud", "delbru", "http://renaud.delbru.fr" },
      new String[] { "<URI>", "<URI>", "<URI>" });
    this.assertAnalyzesTo(_a, "<http://Renaud.Delbru.fr/>",
      new String[] { "renaud", "delbru", "http://renaud.delbru.fr" },
      new String[] { "<URI>", "<URI>", "<URI>" });
    this.assertAnalyzesTo(
      _a,
      "<http://renaud.delbru.fr/page.html?query=a+query&hl=en&start=20&sa=N>",
      new String[] { "renaud", "delbru", "page", "html", "query",
                     "query", "start",
                     "http://renaud.delbru.fr/page.html?query=a+query&hl=en&start=20&sa=n" },
      new String[] { "<URI>", "<URI>", "<URI>", "<URI>", "<URI>", "<URI>",
                     "<URI>", "<URI>" });
    this.assertAnalyzesTo(_a, "<mailto:renaud@delbru.fr>",
      new String[] { "renaud", "delbru",
                     "mailto:renaud@delbru.fr" },
      new String[] { "<URI>", "<URI>", "<URI>" });
    this.assertAnalyzesTo(_a, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>",
      new String[] { "1999", "syntax", "type",
                     "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"},
      new String[] { "<URI>", "<URI>", "<URI>", "<URI>" });
  }

  @Test
  public void testLiteral()
  throws Exception {
    this.assertAnalyzesTo(_a, "\"foo bar FOO BAR\"", new String[] { "foo",
        "bar", "foo", "bar" }, new String[] { "<ALPHANUM>", "<ALPHANUM>",
        "<ALPHANUM>", "<ALPHANUM>" });
    this.assertAnalyzesTo(_a, "\"ABC\\u0061\\u0062\\u0063\\u00E9\\u00e9ABC\"",
      new String[] { "abcabcééabc" }, new String[] { "<ALPHANUM>" });
  }

  @Test
  public void testLanguage()
  throws Exception {
    this.assertAnalyzesTo(_a, "\"test\"@en", new String[] { "test" },
      new String[] { "<ALPHANUM>" });
  }

  @Test
  public void testDatatype()
  throws Exception {
    this.assertAnalyzesTo(_a, "<http://test/>^^<http://type/test>",
      new String[] { "test", "http://test" },
      new String[] { "<URI>", "<URI>" });
  }

  @Test
  public void testBNodeFiltering()
  throws Exception {
    this.assertAnalyzesTo(_a, "_:b123 <aaa> <bbb> _:b212",
      new String[] { "aaa", "bbb" },
      new String[] { "<URI>", "<URI>" });
  }

}
