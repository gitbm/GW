package org.gitwave.common


class FactoryMockito extends Factory {

	def create[T : ClassManifest]: T = FactoryMockito.getMock[T]
	def create[T : ClassManifest, A : ClassManifest](arg : A) = FactoryMockito.getMock[T](List(arg.asInstanceOf[AnyRef]))
   	def create[T : ClassManifest, A1 : ClassManifest, A2 : ClassManifest](arg1 : A1, arg2 : A2) = {
    	FactoryMockito.getMock[T](List(arg1.asInstanceOf[AnyRef], arg2.asInstanceOf[AnyRef]))
	}
    
    def defineImpl[T : ClassManifest, I <: T : ClassManifest] : Unit = {}   
}

object FactoryMockito {
	import scala.collection._
	import org.mockito._
	import org.mockito.Matchers._
	def apply() = new FactoryMockito
	
	case class MockWithArgs[T](val mockObj : T, var argList : List[AnyRef])

	private val mockMap  = mutable.Map[Class[_], mutable.Queue[FactoryMockito.MockWithArgs[_]]]()

	def createAndRegisterMock[T <: AnyRef : ClassManifest] : MockWithArgs[T] = {
		val mockClass = classManifest[T].erasure.asInstanceOf[Class[T]]
		val mockWithArgs = MockWithArgs(Mockito.mock(mockClass), Nil)
		var q = mockMap.getOrElseUpdate(mockClass, mutable.Queue[MockWithArgs[_]]()) += mockWithArgs
		mockWithArgs
	}
	
	def mock[T <: AnyRef : ClassManifest] : T = createAndRegisterMock[T].mockObj
	def mockWithArgs[T <: AnyRef : ClassManifest] : MockWithArgs[T] = createAndRegisterMock[T]
	
	def getMockWithArgs[T : ClassManifest] :  MockWithArgs[T] = {
		val mockClass = classManifest[T].erasure.asInstanceOf[Class[T]]
		val mockWithArgs = mockMap.getOrElse(mockClass, throw new RuntimeException("No Mock registered for " + mockClass.getName))
			.dequeue.asInstanceOf[MockWithArgs[T]]
		mockWithArgs
	}
	def getMock[T : ClassManifest] : T = getMockWithArgs[T].mockObj
	def getMock[T : ClassManifest](argList : List[AnyRef]) : T = {
		val mockWithArgs = getMockWithArgs[T]
		mockWithArgs.argList = argList
		mockWithArgs.mockObj
	}
	
	def validateArgs[A : ClassManifest](argList : List[AnyRef], arg : A) : Boolean = {
		argList match {
			case head :: Nil => {
				// println(head.getClass.getName + ", " + classManifest[A].erasure + ", " + arg + ", " + head.toString)
				head.isInstanceOf[A] && arg == head.asInstanceOf[A]
			}
			case _ => false
		}
	}
	def validateArgs[A1 : ClassManifest, A2 : ClassManifest](argList : List[AnyRef], arg1 : A1, arg2 : A2) : Boolean = {
		argList match {
			case head :: second :: Nil => {
				println(head.getClass.getName + ", " + classManifest[A1].erasure + ", " + arg1 + ", " + head.toString)
				head.isInstanceOf[A1] && arg1 == head.asInstanceOf[A1] && second.isInstanceOf[A2] && arg2 == second.asInstanceOf[A2]
			}
			case _ => { println("match _"); false }
		}
	}
}