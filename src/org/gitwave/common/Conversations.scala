package org.gitwave.common

import Factory._
//import Implicits._

trait Conversations {
	def convList : List[Conversation]
	def createNewConversation : Conversation
}

object Conversations extends Conversations {
	private var theConvs : Option[Conversations] = None
	
	private def convs = {
		if (theConvs.isEmpty) {
			theConvs = Some(create[Conversations])
		}
		theConvs.get
	}
	
	def convList = convs.convList
	def createNewConversation = convs.createNewConversation
}