package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

fun cypher(a: Char, b: Int) : Char {
	require(a.isUpperCase())
	require(a <= 'Z' && a >= 'A')
	require(b >= 0 )

	println(a)
	val mynum = b%26
	if ((a + mynum).isLetter()) return a+mynum

	return (a+mynum)-26

}
