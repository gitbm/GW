package org.gitwave.common

object Imports {
	def neu[T](implicit c : Factory[T]) : T = { c.create }
	def neu[T](id : String)(implicit c : Factory[T]) : T = { c.create(id) }
}