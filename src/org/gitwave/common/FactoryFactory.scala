package org.gitwave.common

// Not currently in use.  Based on some earlier ideas which have been replaced by the use of implicits

//trait FactoryFactory {
//	private var factoryMap = Map.empty[String, Factory[AnyRef]]
//	def getFactory[T](c : Class[T]) : Factory[T] = {
//		factoryMap.getOrElse(c.getCanonicalName, new FactoryDefault[T](c)).asInstanceOf[Factory[T]]
//	}
//	def registerFactory[T <: AnyRef, DT <: T](c : Class[T], f : Factory[DT]) {
//		factoryMap += (c.getCanonicalName -> f) 
//	}
//	def init : Unit
//}
//
//object FactoryFactory {
//	var instance : FactoryFactory = _
//	
//	def setInstance(theInstance : FactoryFactory) { 
//		synchronized {
//			instance = theInstance;
//		}
//	}
//	
//	def getFactory[T](c : Class[T]) = {
//		instance.getFactory(c)
//	}
//	
//	def create[T](c: Class[T]) : T = {
//		getFactory(c).create
//	}
//	
//	def nu[T](implicit m : Manifest[T]) : Class[T] = null
// }