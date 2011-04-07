package org.gitwave.common

object Implicits {
	implicit var implicitConversationFactory : Factory[Conversation] = FactoryDefault(classOf[ConversationDefault])	

	implicit var implicitConversationsFactory : Factory[Conversations] = FactoryDefault(classOf[ConversationsFromDisk])
	
	implicit var implicitPropertiesFactory : Factory[Properties] = FactoryDefault(classOf[PropertiesFromFile])	
}