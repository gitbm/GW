package org.gitwave.common

import java.io._
import org.mockito._
//import org.mockito.Mockito._
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


class FactoryMockito[T <: AnyRef : ClassManifest]/*(val mockClass : Class[T])*/ extends Factory[T] {
    def create(meta : Option[Either[String, AnyRef]]): T = {
    	FactoryMockito.getMock[T] //(mockClass)
    }
}
object FactoryMockito {
	def apply[T <: AnyRef : ClassManifest]/*(mockClass : Class[T])*/ = new FactoryMockito[T] //(mockClass)
	
	private val mockMap  = mutable.Map[Class[_], mutable.Queue[AnyRef]]()
//	def registerMock(mockObj : AnyRef, mockClass : Class[_], allowMultiple : Boolean = false) {
//		mockMap(mockClass) = (mockObj :: (if (allowMultiple) mockMap.getOrElse(mockClass, Nil) else Nil))
//	}
//	def registerMock[T <: AnyRef : ClassManifest](mockObj : T, allowMultiple : Boolean = false) {
//		val mockClass = classManifest[T].erasure.asInstanceOf[Class[T]]
//		mockMap(mockClass) = (mockObj :: (if (allowMultiple) mockMap.getOrElse(mockClass, Nil) else Nil))
//	}
//	def registerMock[T <: AnyRef : ClassManifest](mockObjs : List[T]) {
//		val mockClass = classManifest[T].erasure.asInstanceOf[Class[T]]
//		mockMap(mockClass) = (mockObjs ::: mockMap.getOrElse(mockClass, Nil))
//	}
	def createAndRegisterMock[T <: AnyRef : ClassManifest] : T = {
		val mockClass = classManifest[T].erasure.asInstanceOf[Class[T]]
		val mockObj = Mockito.mock(mockClass)
		var q = mockMap.getOrElseUpdate(mockClass, mutable.Queue[AnyRef]()) += mockObj
		mockObj
	}
	
	def mock[T <: AnyRef : ClassManifest] : T = createAndRegisterMock[T]
	
	def getMock[T <: AnyRef : ClassManifest]/*(mockClass : Class[T])*/ : T = {
		val mockClass = classManifest[T].erasure.asInstanceOf[Class[T]]
		val mockObj = mockMap.getOrElse(mockClass, throw new RuntimeException("No Mock registered for " + mockClass.getName))
			.dequeue.asInstanceOf[T]
		mockObj
//		val ol = mockMap.get(mockClass)
//		if (ol.isEmpty || ol.get.isEmpty) {
//			val t = mock(mockClass)
//			registerMock[T](t)//, mockClass)
//			t
//		}
//		else {
//			ol.get.tail match {
//				case h2 :: t2 => mockMap.put(mockClass, t2)
//				case _ =>
//			}
//			ol.get.head.asInstanceOf[T]
//		}
	}
}


object TestConversationsFromDisk {
	def main(arg : Array[String]): Unit = { 
//		ConversationsFromDisk.convFactory = Factory1Default[TestConversation, ConversationInitArgs]
//		ConversationsFromDisk.fileFactory1 = Factory1Default[TestFile, String]
//		ConversationsFromDisk.fileFactory2 = Factory2Default[TestFile, File, String]
//		Implicits.implicitPropertiesFactory = FactoryDefault[TestProperties]
//		Implicits.implicitPropertiesFactory = FactoryMockito[Properties]
		
//		val x = new ConversationsFromDisk
//		println("subs: " + x.getSubDirs)
//		println("CL: " + x.convList)
//		
//		val nc = x.createNewConversation
//		println("nc: " + nc)
//		println("CL: " + x.convList)
		
		println("B4")
		import FactoryMockito._
		import Mockito.{mock=>_, _}
		val mp = mock[Properties]
		when(mp.get("Test")).thenReturn(Some("Foos"))
		val x = Properties.get("gw.base.dir")
//		import Implicits._
//		val p = Imports.neu[Properties]
//		val x = p.get("Test")
		
		println("x = " + x)
		
//		verify(mp).initialise(any(), any())
	}
}