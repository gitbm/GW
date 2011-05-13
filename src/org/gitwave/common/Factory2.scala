package org.gitwave.common

trait Factory2[+T, CtrArg1, CtrArg2] {
 	def create[C1 <: CtrArg1, C2 <: CtrArg2](arg1 : C1, arg2 : C2) : T	
}

class Factory2Helper[T] { 
	def apply[C1, C2](arg1: C1, arg2 : C2)(implicit f : Factory2[T, C1, C2]) : T = f.create(arg1, arg2)
}

