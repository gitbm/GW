package org.gitwave.common

//class Factory1Default[T : ClassManifest, CtrArg : ClassManifest] extends Factory1[T, CtrArg] {
//	private val cons = classManifest[T].erasure.asInstanceOf[Class[T]].getConstructor(classManifest[CtrArg].erasure)
//
//	def create[C <: CtrArg](arg: C): T = cons.newInstance(arg.asInstanceOf[Object])
//}
//
//object Factory1Default {
//	def apply[T : ClassManifest, C : ClassManifest] = new Factory1Default[T, C]
//}
