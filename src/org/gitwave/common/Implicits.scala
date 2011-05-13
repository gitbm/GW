package org.gitwave.common

object Implicits {
	implicit var implicitConversationFactory : Factory[Conversation] = FactoryDefault[ConversationDefault]

	implicit var implicitConversationsFactory : Factory[Conversations] = FactoryDefault[ConversationsFromDisk]
	
	implicit var implicitPropertiesFactory : Factory[Properties] = FactoryDefault[PropertiesFromFile]	
}