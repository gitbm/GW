package org.gitwave.main

import org.gitwave._
import common.FactoryDefault
import common.ConversationAlternate
import common.ConversationsAlternate
import common.Factory
import common.Conversations

object Implicits {
	
	def overrideCommonImplicits {
//		common.Implicits.implicitConversationFactory = FactoryDefault(classOf[ConversationAlternate])
//		common.Implicits.implicitConversationsFactory = FactoryDefault(classOf[ConversationsAlternate])
	}
}