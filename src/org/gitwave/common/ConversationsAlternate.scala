package org.gitwave.common

import scala.collection.immutable.List
import org.gitwave._
import common.Imports._
import Implicits._

class ConversationsAlternate extends Conversations {

	def convList(): List[Conversation] = {
	  var l = List.empty[Conversation]
	  for(i <- 1 to 4) {
	 	  l ::= neu[Conversation]
	 	  Thread.sleep(50);
	  }
	  l.reverse 
  }


}