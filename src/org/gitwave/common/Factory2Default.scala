package org.gitwave.common

//class Factory2Default[T : ClassManifest, CtrArg1 : ClassManifest, CtrArg2 : ClassManifest] extends Factory2[T, CtrArg1, CtrArg2] {
//	
//	private val cons = classManifest[T].erasure.asInstanceOf[Class[T]].getConstructor(classManifest[CtrArg1].erasure, classManifest[CtrArg2].erasure)
//	
// 	def create[C1 <: CtrArg1, C2 <: CtrArg2](arg1 : C1, arg2 : C2) : T = {
//		cons.newInstance(arg1.asInstanceOf[Object], arg2.asInstanceOf[Object])
//	}
//}
//
//object Factory2Default {
//	def apply[T : ClassManifest, C1 : ClassManifest, C2 : ClassManifest] = new Factory2Default[T, C1, C2]
//}
//
