package org.gitwave.common

import org.gitwave._
import Implicits._
import PropertiesFromFile._
import java.io.File
import java.io.FileWriter

import org.junit.Assert._
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

object TestPropertiesFromFile {
	val PROP_FILENAME_VIA_PROPERTY = "viaproperty.properties"
	val PROP_FILENAME_EXPLICIT = "explicit.properties"

	val propsToPopulateA = Map("Ap1" -> "Aval1")
	
	val propsToPopulateB = Map("Bp1" -> "Bval1", "Bp2" -> "Bval2")
	
	val propsToPopulateC = Map("Cp1" -> "Cval1", "Cp2" -> "Cval2", "Cp3" -> "Cval3")
	
	def createPropertyFile(name : String, props : Map[String, String]) {
		def propertiesToString = {
			props.foldLeft(""){ (acc, e) => acc + e._1 + " = " + e._2 + "\n"}
		}
		try {
		val fw = new FileWriter(name)
		try{ fw.write(propertiesToString) } finally { fw.close }
		}
		catch {
			case e => println("Exception caught: " + e)
		}
	}
	
	def initPropertyFiles() {
		createPropertyFile(DEFAULT_PROPERTY_FILE_NAME, propsToPopulateA)
		createPropertyFile(PROP_FILENAME_VIA_PROPERTY, propsToPopulateB)
		createPropertyFile(PROP_FILENAME_EXPLICIT, propsToPopulateC)
	}
	
	@BeforeClass
	def setUpBeforeClass { 
		// Ensure we are testing Properties from file
		implicitPropertiesFactory = FactoryDefault(classOf[PropertiesFromFile])
		initPropertyFiles() 
	}

//	@AfterClass
//	def tearDownAfterClass { }
}

class TestPropertiesFromFile {
	
	import TestPropertiesFromFile._
	
	@After
	def tearDown { 
		System.clearProperty(SYSPROP_PROPERTY_FILE)
	}

	@Test
	def testDefaultPropertyFile() {
		Properties.initialiseOverwrite(None)
		val s1 = Properties.get("Ap1")
		val s2 = Properties.get("Bp1")
		val s3 = Properties.get("Cp1")

		assertEquals(Some("Aval1"), s1)
		assertEquals(None, s2)
		assertEquals(None, s3)
	}
	
	@Test
	def testDefaultPropertyFileWithDefaultMap() {
		Properties.initialiseOverwrite(None, Map("Ap1" -> "Aval1def", "Ap2" -> "Aval2def"))
		val s1 = Properties.get("Ap1")
		val s2 = Properties.get("Ap2")
		
		assertEquals(Some("Aval1"), s1)
		assertEquals(Some("Aval2def"), s2)
	}
	
	@Test
	def testFilenameViaPropertyFile() {
		System.setProperty(SYSPROP_PROPERTY_FILE, PROP_FILENAME_VIA_PROPERTY)
		Properties.initialiseOverwrite(None)
		val s1 = Properties.get("Ap1")
		val s2 = Properties.get("Bp1")
		val s3 = Properties.get("Cp1")

		assertEquals(None, s1)
		assertEquals(Some("Bval1"), s2)
		assertEquals(None, s3)
	}
	
	@Test
	def testExplictPropertyFile() {
		Properties.initialiseOverwrite(Some(PROP_FILENAME_EXPLICIT))
		val s1 = Properties.get("Ap1")
		val s2 = Properties.get("Bp1")
		val s3 = Properties.get("Cp1")

		assertEquals(None, s1)
		assertEquals(None, s2)
		assertEquals(Some("Cval1"), s3)
	}
	
}

object MainTest {
	def main(args: Array[String]): Unit = {
		val t = new TestPropertiesFromFile
		t.testDefaultPropertyFileWithDefaultMap
	}
}