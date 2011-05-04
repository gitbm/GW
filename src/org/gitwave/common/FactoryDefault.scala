package org.gitwave.common

//class FactoryDefault[T](val theClass : Class[T]) extends Factory[T] {
//
//  override def createWithId(id : String): T = { theClass.newInstance }
//
//}
//
//object FactoryDefault {
//	def apply[T](defaultClass : Class[T]) = {
//		new FactoryDefault[T](defaultClass)
//	}
//}

class FactoryDefault[T](val theClass : Class[T]) extends Factory[T] {
	private val cons = theClass.getConstructor()
    def create(meta : Option[Either[String, AnyRef]]): T = cons.newInstance()
}

object FactoryDefault {
	def apply[T](defaultClass : Class[T]) = new FactoryDefault[T](defaultClass)
}
