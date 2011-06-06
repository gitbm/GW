package org.gitwave.common

import java.io._
import org.mockito._
import org.mockito.Matchers._
import scala.collection._
import org.junit.Assert._
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import org.acme.scalautil._
import org.acme.scalautil.test.FactoryMockito

class TestConversationsFromDisk {
	import FactoryMockito._
	import Mockito._
	

	private def getId(name : String) = name.filter(_.isDigit).toLong
	
	private val BASE_DIR_PROP = "gw.base.dir"
	private val BASE_DIR = "Foo"
	private val C1_NAME = "f1"
	private val C1_ABS_PATH = "/root/" + C1_NAME
	private val C1_ID = getId(C1_NAME)
	private val C2_NAME = "f2"
	private val C2_ABS_PATH = "/root/" + C2_NAME
	private val C2_ID = getId(C2_NAME)
	
		
	Factory.implicitFactory = new FactoryMockito
	
	@Test
	def test_convList {
		val mp = createMock[Properties]
		when(mp.get(BASE_DIR_PROP)).thenReturn(Some(BASE_DIR))
		val dirFile = createMock[File]
		val f1 = createMock[File]
		when(f1.isDirectory).thenReturn(true)
		when(f1.getName).thenReturn(C1_NAME)
		when(f1.getAbsolutePath).thenReturn(C1_ABS_PATH)
		val f2 = createMock[File]
		when(f2.isDirectory).thenReturn(true)
		when(f2.getName).thenReturn(C2_NAME)
		when(f2.getAbsolutePath).thenReturn(C2_ABS_PATH)
		when(dirFile.listFiles).thenReturn(Array(f1, f2))
		val cv1 = createMock[Conversation]
		when (cv1.init(any())).thenReturn(cv1)
		when (cv1.id).thenReturn(1)
		val cv2 = createMock[Conversation]
		when (cv2.init(any())).thenReturn(cv2)
		when (cv2.id).thenReturn(2)
		
		val cs = new ConversationsFromDisk
		val cl = cs.convList
		
		verify(cv1).init(Matchers.eq(ConversationInitArgs(C1_ABS_PATH, false, Some(C1_ID))))
		verify(cv2).init(Matchers.eq(ConversationInitArgs(C2_ABS_PATH, false, Some(C2_ID))))
		
		println(cl)

	}
}


//class TestFile(var name : String) extends File(name) {
//	def this (f : File, s : String) = this (s)
//	
//	override def listFiles() : Array[File] = {
//		Array[File](new TestFile("foo11"), new TestFile("bar23"))
//	}
//	override def mkdir() : Boolean = true
//	override def getAbsolutePath() : String = "/foofoo/" + name
//	override def getName() = name
//	override def isDirectory() = true
//}
//
//class TestProperties extends Properties {
//	def initialise(initString : Option[String], defaults : immutable.Map[String, String]) {
//		println("Initialise !!!")
//	}
//	def addDefaults(defaults : immutable.Map[String, String]) {}
//	def get(name : String) : Option[String] = Some("/blah/gw")
//
//}
//
//
//
//
//object TestConversationsFromDisk {
//	def main(arg : Array[String]): Unit = { 			
//		import FactoryMockito._
//		import Mockito.{mock=>_, _}
//		Factory.iFact = new FactoryMockito
//		val mp = mock[Properties]
//		when(mp.get("gw.base.dir")).thenReturn(Some("Foos"))
//		val x = Properties.get("gw.base.dir")
//		
//		println("x = " + x)
//		
//		verify(mp).initialise(any(), any())
//	}
//}