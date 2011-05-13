package org.gitwave.common

import scala.collection.immutable.List
import org.gitwave._
import Factory._
//import Implicits._
import java.io.File

class ConversationsFromDisk extends Conversations {
	import ConversationsFromDisk._
  
	lazy val baseDir = create[File, String](Properties.getMandatory("gw.base.dir"))
	  
    def getSubDirs = { baseDir.listFiles().toList.filter(_.isDirectory()) }

	var cachedConvList : Option[List[Conversation]] = None
	
    def convList: List[Conversation] = {
    	if (cachedConvList.isEmpty) {
    		val convs = for { 
    			subDir <- getSubDirs
    			id = getIdFromSubDirName(subDir.getName)
    			conv = create[Conversation, ConversationInitArgs](ConversationInitArgs(subDir.getAbsolutePath, false, Some(id)))
    		} yield conv

    		cachedConvList = Some(sort(convs))
    	}
    	cachedConvList.get
    }
  
	def sort(l : List[Conversation]) = l.sort((lh, rh) => lh.id < rh.id)
	
    def createNewConversation : Conversation = {
    	val id = getNewId
    	val dir = create[File, File, String](baseDir, getSubDirNameFromId(id))
    	dir.mkdir
    	val conv = create[Conversation, ConversationInitArgs](ConversationInitArgs(dir.getAbsolutePath, true, Some(id)))
    	cachedConvList = Some(sort(conv :: cachedConvList.get))
    	conv
    }

    def getNewId : Long = {
    	val subDirs = getSubDirs
    	if (subDirs.isEmpty) 1 else getSubDirs.map((f) => getIdFromSubDirName(f.getName)).max + 1
    }
}

object ConversationsFromDisk {
    defineImpl[Conversation, ConversationFromDir]

	val CONVERSATION_DIR_PREFIX = "conv_"
	
	def getIdFromSubDirName(subDirName : String) : Long = {
		new File(subDirName).getName.filter(_ .isDigit).toLong
	}
	
	def getSubDirNameFromId(id : Long) : String = {
		String.format("%s%08d", CONVERSATION_DIR_PREFIX, id.asInstanceOf[Object])
	}
}