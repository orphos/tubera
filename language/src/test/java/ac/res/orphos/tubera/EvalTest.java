// Copyright (C) 2020 Takezoe,Tomoaki <tomoaki3478@res.ac>
//
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
package ac.res.orphos.tubera;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.*;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EvalTest {
    Context c;

    @BeforeAll
    public void up() {
        c = Context.create("orphos");
    }

    @AfterAll
    public void down() {
        c.close();
    }

    public Value eval(String src) {
        return c.eval("orphos", src);
    }

    public void check(String src, String expected) {
        try {
            assertEquals(expected, eval(src).toString());
        } catch (PolyglotException e) {
            if (e.getCause() != null && e.getCause() instanceof RuntimeException)
                throw (RuntimeException) e.getCause();
            throw e;
        }
    }

    @Test
    public void testIntegerLiteral() {
        check("100", "100");
    }

    @Test
    public void testBooleanLiteral() {
        check("true", "true");
        check("false", "false");
    }

    @Test
    public void testIf() {
        check("if true then 2 else 3", "2");
        check("if false then 2 else 3", "3");
    }

    @Test
    public void testString() {
        check("\"test\"", "test");
        check("\"\"", "");
    }

    @Test
    public void testParen() {
        check("(1)", "1");
        check("(1 + 1) + 1", "3");
    }

    @Test
    public void testAdd() {
        check("1 + 1", "2");
        check("9223372036854775807 + 1", "9223372036854775808");
    }

    @Test
    public void testLet() {
        check("let a = 1 in a", "1");
        check("let a = 1 in let a = 2 in a", "2");
        check("let a = 1 in let a = a + 1 in a", "2");
        check("let a = 1 in let b = a + 1 in a", "1");
    }

    @Test
    public void testFn() {
        check("(fn a -> a) 1", "1");
        check("(fn a -> fn b -> b) 1 2", "2");
        check("(fn a -> fn b -> a) 1 2", "1");
        check("(fn a -> fn b -> a + b) 1 2", "3");
        check("let add = fn a -> fn b -> a + b in add 1 2", "3");
    }

    @Test
    public void testRec() {
        check("let rec f = fn a -> fn b -> if a then f false b else b in f true 1", "1");
    }

    @Test
    public void testFib() {
        check("let rec fib = fn n -> if n < 1 then -1 else if n == 1 then 1 else if n == 2 then 1 else fib (n - 2) + fib (n - 1) in fib 10",
                "55");
    }

    @Test
    public void testRecord() {
        check("let xs = { a = 10; b = 20; } in xs.b", "20");
        check("let xs = { a = 10; b = 20; } in let ys = { xs with c = 30; } in ys.c", "30");
        check("let xs = { a = 10; b = 20; } in let ys = { xs with c = 30; } in ys.b", "20");
    }

    @Test
    public void testUnit() {
        check("()", "()");
        assertTrue(eval("()").isNull());
    }

}