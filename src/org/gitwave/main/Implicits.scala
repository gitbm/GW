package org.gitwave.main

import org.gitwave._
import common.FactoryDefault
import common.ConversationAlternate
import common.ConversationsAlternate
import common.Factory
import common.Conversations
import org.gitwave.common.ConversationsFromDisk

object Implicits {
	
	def overrideCommonImplicits {
		common.Implicits.implicitConversationsFactory = FactoryDefault[ConversationsFromDisk]
	}
}