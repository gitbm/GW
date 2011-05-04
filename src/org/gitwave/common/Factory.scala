package org.gitwave.common

//trait Factory[+T] {
//	def create() : T = createWithId(null)
//	def createWithId(id : String) : T
//}
trait Factory[+T] {
	def create() : T = create(None)
	def createWithId(id : String) : T = create(Some(Left(id)))
	def createWithAnyRef(meta : AnyRef) : T = create(Some(Right(meta)))
	
	protected def create(meta : Option[Either[String, AnyRef]]) : T
}
