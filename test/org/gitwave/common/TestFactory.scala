package org.gitwave.common
import org.gitwave._
import Imports._

import org.junit.Assert._
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

object constants {
	val NO_CTR = "TestImpl"
	val CTR_DEFAULT = "default"
	val CTR_VALUE = "FOO"
	val CTR_VALUE2 = "BAR"
}

import constants._

trait TestTrait {
	def test : String
}

class TestImpl extends TestTrait {
	def test = NO_CTR
}

class TestImplCons(val cons : String) extends TestTrait {
	def this() { this(CTR_DEFAULT) }
	def test = cons
	
}

class TestFactory {

	@Test
	def testFactoryDefault() {
		implicit val imp : Factory[TestTrait] = FactoryDefault[TestImpl]
	
		val o = neu[TestTrait]	
        assertEquals("class name", "org.gitwave.common.TestImpl", o.getClass.getName) 
		assertEquals("method", NO_CTR, o.test) 
	}

	@Test
	def testFactoryDefault2() {
		implicit val imp : Factory[TestTrait] = FactoryDefault[TestImplCons]	

		val o = neu[TestTrait]
		//println("o2 cl = " + o2.getClass.getName + ", " + o2.test) 
		assertEquals("class name", "org.gitwave.common.TestImplCons", o.getClass.getName) 
		assertEquals("method", CTR_DEFAULT, o.test) 	
	}

	
	@Test
	def testFactory1Default() {
		implicit val imp : Factory1[TestTrait, String] = Factory1Default[TestImplCons, String]
	
		val o = neu1[TestTrait, String](CTR_VALUE)
		assertEquals("class name", "org.gitwave.common.TestImplCons", o.getClass.getName) 
		assertEquals("method", CTR_VALUE, o.test) 

		val o2 = neu1[TestTrait](CTR_VALUE2)
		assertEquals("class name", "org.gitwave.common.TestImplCons", o2.getClass.getName) 
		assertEquals("method", CTR_VALUE2, o2.test) 

	}
}