package org.gitwave.common

case class ConversationInitArgs(initStr : String, create : Boolean = false, id : Option[Long] = None)