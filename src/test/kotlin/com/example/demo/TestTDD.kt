package com.example.demo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.test.assertFails
import io.kotest.property.checkAll
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char

class TestTDD : FunSpec({
    test("a should be C") {
        val res = cypher('A', 2)
        res shouldBe 'C'
    }
    test("a should be F") {
        val res = cypher('A', 5)
        res shouldBe 'F'
    }

    test("a should be UpperCase") {

        shouldThrow<IllegalArgumentException> {
            cypher('a', 2)
        }
    }
    test("a should be B") {
        val res = cypher('Y', 3)
        res shouldBe 'B'
    }
    test("a should be C") {
        val res = cypher('A',  52)
        res shouldBe 'A'
    }

    test("b should be positive") {
        shouldThrow<IllegalArgumentException> {
            cypher('A', -2)
        }
    }
    test("b should be positive") {
        shouldThrow<IllegalArgumentException> {
            cypher('#', 2)
        }
    }

    test ("b should be positive") {
        checkAll<Int> { b ->
            if (b < 0) {
                shouldThrow<IllegalArgumentException> {
                    cypher('A', b)
                }
            }
        }
    }

    test ("b should be positive") {
        checkAll<Int> { b ->
            if (b < 0) {
                shouldThrow<IllegalArgumentException> {
                    cypher('A', b)
                }
            }
        }
    }

    test ("a should be a Letter") {
        checkAll<Char> { a ->
            if (!a.isLetter()) {
                shouldThrow<IllegalArgumentException> {
                    cypher(a, 0)
                }
            }
        }
    }

    test ("if b = 0 a should be a") {
        checkAll(Arb.char('A'..'Z')) { a ->
            var res = cypher(a, 0)
            res shouldBe a
        }
    }
})