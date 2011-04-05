package org.gitwave.common

trait Conversations {
	def convList : List[Conversation]
}

//object Conversations {
//	private var theConvList = List.empty[Conversation]
//	private var init = false
//	def convList = {
//		if (!init) {
//			init = true
//		}
//		theConvList
//	}
//}