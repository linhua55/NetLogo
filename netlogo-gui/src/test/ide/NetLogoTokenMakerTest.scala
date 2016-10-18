// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.ide

import javax.swing.text.Segment

import org.fife.ui.rsyntaxtextarea.{ Token => RstaToken, TokenImpl, TokenTypes }

import org.nlogo.core.TokenType
import org.nlogo.lex.{ LexOperations, StandardLexer }

import org.scalatest.FunSuite

class NetLogoTokenMakerTest extends FunSuite {
  trait SegInputHelper {
    var text: String = ""
    lazy val seg = new Segment(text.toCharArray, 0, text.length)
    lazy val input = new SegmentWrappedInput(seg)
  }

  trait Helper {
    var text: String = ""
    def seg = new Segment(text.toCharArray, 0, text.length)
    val nlTokenMaker = new NetLogoTokenMaker()
    def tokens: RstaToken = nlTokenMaker.getTokenList(seg, TokenTypes.NULL, 0)
  }

  test("wrapped input: empty string") { new SegInputHelper {
    text = ""
    assert(! input.hasNext)
  } }

  test("wrapped input: single character") { new SegInputHelper {
    text = "a"
    assert(input.hasNext)
    assert(input.offset == 0)
  } }

  test("wrapped input: longest prefix") { new SegInputHelper {
    text = "a"
    val (res, nextInput) = input.longestPrefix(LexOperations.characterMatching(c => c == 'a'))
    assert(res == "a")
    assert(! nextInput.hasNext)
    assert(nextInput.offset == 1)
  } }

  test("wrapped input: longest prefix of number") { new SegInputHelper {
    text = "123"
    val (res, nextInput) = input.longestPrefix(StandardLexer.numericLiteral._1)
    assert(res == "123")
    assert(! nextInput.hasNext)
    assert(nextInput.offset == 3)
  } }

  test("wrapped input: assemble token") { new SegInputHelper {
    text = "a"
    val Some((res, nextInput)) =
      input.assembleToken(LexOperations.characterMatching(c => c == 'a'), (s) => Some((s, TokenType.Ident, null)))
    assert(res.start == 0)
    assert(res.end == 1)
    assert(res.tpe == TokenType.Ident)
    assert(! nextInput.hasNext)
    assert(nextInput.offset == 1)
  } }

  test("NetLogoTokenMaker: empty") { new Helper {
    assert(tokens.getNextToken == null)
  } }

  test("NetLogoTokenMaker: single token") { new Helper {
    text = "123"
    val t = tokens
    assert(t.getType == TokenTypes.LITERAL_NUMBER_FLOAT)
    assert(t.getLexeme == "123")
    assert(t.getNextToken == null)
  } }

  test("NetLogoTokenMaker: whitespace") { new Helper {
    text = " "
    val t = tokens
    assert(t.getType == TokenTypes.WHITESPACE)
    assert(t.getLexeme == " ")
    assert(t.getNextToken == null)
  } }

  test("NetLogoTokenMaker: single token followed by whitespace") { new Helper {
    text = "123 "
    val t = tokens
    val t2 = tokens.getNextToken
    assert(t2 != null)
    assert(t2.getType == TokenTypes.WHITESPACE)
    assert(t2.getLexeme == " ")
    assert(t2.getNextToken == null)
  } }

  test("NetLogoTokenMaker: two tokens separated by whitespace") { new Helper {
    text = "123 \"456\""
    val t = tokens
    val t2 = tokens.getNextToken
    assert(t2 != null)
    val t3 = t2.getNextToken
    assert(t3.getType == TokenTypes.LITERAL_STRING_DOUBLE_QUOTE)
    assert(t3.getLexeme == "\"456\"")
    assert(t3.getNextToken == null)
  } }

  test("NetLogoTokenMaker: open literal string") { new Helper {
    text = "\""
    val t = tokens
    assert(t.getNextToken == null)
    assert(t.getLexeme == "\"")
  } }

  test("NetLogoTokenMaker: ident and number separated by whitespace") { new Helper {
    text = "abc 123"
    val t = tokens
    assert(t.getType == TokenTypes.IDENTIFIER)
    val t2 = tokens.getNextToken
    assert(t2 != null)
    val t3 = t2.getNextToken
    assert(t3.getType == TokenTypes.LITERAL_NUMBER_FLOAT)
    assert(t3.getLexeme == "123")
    assert(t3.getNextToken == null)
  } }
}
