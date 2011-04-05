package org.gitwave.common

class ConversationAlternate extends Conversation {
	private var cId = Count.current
	def id(): Long = cId

	def getText = cId + "_Alternate"

}

object Count {
	private var count = 0L
	def current = { count +=1 ; count }
}