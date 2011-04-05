package org.gitwave.common

class FactoryDefault[T](val theClass : Class[T]) extends Factory[T] {

  override def create(id : String): T = { theClass.newInstance }

}

object FactoryDefault {
	def apply[T](defaultClass : Class[T]) = {
		new FactoryDefault[T](defaultClass)
	}
}