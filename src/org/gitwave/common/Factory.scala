package org.gitwave.common

trait Factory[+T] {
	def create() : T = create(null)
	def create(id : String) : T 
}
