package org.gitwave.common

class ConversationDefault extends Conversation {
	private var cId = System.currentTimeMillis()
	def id = cId
	
	def getText = cId + "_Default"
}