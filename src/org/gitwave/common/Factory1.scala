package org.gitwave.common

//trait Factory1[+T, ConstructorArg] extends Factory[T] {
//	def create[C >: ConstructorArg](arg : C) = createWithId(null, arg)
//	def createWithId[C >: ConstructorArg](id : String, arg : C) : T
//}

trait Factory1[+T, CtrArg] {
 	def create[C >: CtrArg](arg : C) : T	
}

class Factory1Helper[T] { 
	def apply[C](arg: C)(implicit f : Factory1[T, C]) : T = f.create(arg)
}

