package org.gitwave.common

import java.io._
import org.mockito._
import org.mockito.Matchers._
import scala.collection._

class TestConversationsFromDisk {

}

case class TestConversation(val initArgs : ConversationInitArgs) extends ConversationDefault {
}

class TestFile(var name : String) extends File(name) {
	def this (f : File, s : String) = this (s)
	
	override def listFiles() : Array[File] = {
		Array[File](new TestFile("foo11"), new TestFile("bar23"))
	}
	override def mkdir() : Boolean = true
	override def getAbsolutePath() : String = "/foofoo/" + name
	override def getName() = name
	override def isDirectory() = true
}

class TestProperties extends Properties {
	def initialise(initString : Option[String], defaults : immutable.Map[String, String]) {
		println("Initialise !!!")
	}
	def addDefaults(defaults : immutable.Map[String, String]) {}
	def get(name : String) : Option[String] = Some("/blah/gw")

}




object TestConversationsFromDisk {
	def main(arg : Array[String]): Unit = { 			
		import FactoryMockito._
		import Mockito.{mock=>_, _}
		Factory.iFact = new FactoryMockito
		val mp = mock[Properties]
		when(mp.get("gw.base.dir")).thenReturn(Some("Foos"))
		val x = Properties.get("gw.base.dir")
		
		println("x = " + x)
		
		verify(mp).initialise(any(), any())
	}
}