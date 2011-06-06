package org.gitwave.common

import scala.collection.immutable.List
import org.acme.scalautil.Factory._

class ConversationsAlternate extends Conversations {

	def convList(): List[Conversation] = {
	  var l = List.empty[Conversation]
	  for(i <- 1 to 4) {
	 	  l ::= create[Conversation]
	 	  Thread.sleep(50);
	  }
	  l.reverse 
  }

	def createNewConversation : Conversation = create[Conversation]

}