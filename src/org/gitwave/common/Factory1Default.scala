package org.gitwave.common

//class Factory1Default[T, CtrArg](val theClass : Class[T], val argClass : Class[CtrArg]) extends Factory1[T, CtrArg] {
//	private val cons = theClass.getConstructor(argClass)
//	def create[C >: CtrArg](arg: C): T = {
//		
//		val t = cons.newInstance(arg.asInstanceOf[Object])
//		println("created " + theClass.getName)
//		t
//	}
//}
//
//object Factory1Default {
//	def apply[T, C](defaultClass : Class[T], argClass : Class[C]) = {
//		new Factory1Default[T, C](defaultClass, argClass)
//	}
//}

class Factory1Default[T : ClassManifest, CtrArg : ClassManifest] extends Factory1[T, CtrArg] {
	private val cons = classManifest[T].erasure.asInstanceOf[Class[T]].getConstructor(classManifest[CtrArg].erasure)

	def create[C <: CtrArg](arg: C): T = cons.newInstance(arg.asInstanceOf[Object])
}

object Factory1Default {
	def apply[T : ClassManifest, C : ClassManifest] = new Factory1Default[T, C]
}
