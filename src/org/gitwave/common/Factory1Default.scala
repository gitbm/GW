package org.gitwave.common

//class Factory1Default[T, ConstructorArg](val theClass : Class[T]) extends Factory1[T, ConstructorArg] {
//
//	//override def create(id : String): T = { theClass.newInstance }
//	def createWithId(id : String) : T = {
//		println ("id = " + id)
//		theClass.newInstance
//	}
//	def createWithId[C >: ConstructorArg](id : String, arg : C) : T = {
//		println("arg = " + arg)
//		theClass.newInstance
//	}
//
//}
//
//object Factory1Default {
//	def apply[T, C](defaultClass : Class[T], argClass : Class[C]) = {
//		new Factory1Default[T, C](defaultClass)
//	}
//}

class Factory1Default[T, CtrArg](val theClass : Class[T], val argClass : Class[CtrArg]) extends Factory1[T, CtrArg] {
	private val cons = theClass.getConstructor(argClass)
	def create[C >: CtrArg](arg: C): T = {
		cons.newInstance(arg.asInstanceOf[Object])
	}
}

object Factory1Default {
	def apply[T, C](defaultClass : Class[T], argClass : Class[C]) = {
		new Factory1Default[T, C](defaultClass, argClass)
	}
}
