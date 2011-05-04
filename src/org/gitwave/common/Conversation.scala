package org.gitwave.common

trait Conversation {
	def id : Long
	def getText : String
	def updateText(text : String) : Option[String] = Some("Update Not Implemented")
}