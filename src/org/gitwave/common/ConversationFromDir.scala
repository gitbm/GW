package org.gitwave.common

import java.io._
import java.util._
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{ObjectId, RepositoryBuilder}
import org.eclipse.jgit.transport.{RefSpec} //URIish, CredentialItem, CredentialsProvider

import Imports._

private object Constants {
	val SUBJECT_FILE_NAME = "subject.txt"
	val BODY_FILE_NAME = "body.txt"
	val PROP_PASSPHRASE = "passphrase"
	val	PROP_REMOTE_ALIAS_PREFIX = "remote.alias.prefix"
}

import Constants._

class ConversationFromDir(private val initArgs : ConversationInitArgs) extends Conversation {

	val dirName = initArgs.initStr;
	private val dirFile = new File(dirName)
	private val bodyFile = new File(dirFile, BODY_FILE_NAME)
	private val subjectFile = new File(dirFile, SUBJECT_FILE_NAME)
	private val builder = new RepositoryBuilder().setWorkTree(dirFile).readEnvironment()
	if (!initArgs.create) {
		builder.findGitDir(dirFile)
	}
	private val repository = builder.build();

	if (initArgs.create) { 
		repository.create()
		bodyFile.createNewFile
		subjectFile.createNewFile
	}
		
	private val git = new Git(repository)
	private val cp = new EncryptedPrivateKeySshCredentialsProvider { def getPassphrase() = Properties.get(PROP_PASSPHRASE).getOrElse("") }
	private var remHead : ObjectId = null
	private var head : ObjectId = null
	private val gitRemoteAlias = Properties.getMandatory(PROP_REMOTE_ALIAS_PREFIX) + dirFile.getName
	
	private def updateHead = { head = repository.resolve("HEAD"); dbprintln(dirName + ": head = " + head) }

	def id: Long = initArgs.id.get //ConversationsFromDisk.getIdFromSubDirName(dirName)

    def getText(): String = { 
   		import io._
		if (bodyFile.isFile && bodyFile.exists) Source.fromFile(bodyFile.getAbsolutePath).getLines.mkString("\n") else "No File !"
    }

    override def updateText(text : String) = {
    	var result : Option[String] = None
    	val fw = new FileWriter(bodyFile)
    	try { 
    		fw.write(text) 
    	}
    	catch {	case e => result = Some(e.getMessage) }
    	finally { fw.close }
  
    	if (result.isEmpty) {
    		try { 
    			git.add().addFilepattern(bodyFile.getName).call()
    			git.commit().setMessage("Committing stuff " + new Date()).call()
    			updateHead
    			//    			git.push()
    			//    			.setRemote(gitRemoteAlias)
    			//    			.setRefSpecs(new RefSpec("master"))
    			//    			.setCredentialsProvider(cp)
    			//    			.call()
    			//    			dbprintln(dirName + ": after push")
    		}
    		catch {
    		case e => { 
    			result = Some(e.getMessage)
    			dbprintln("Git Exception: " + e); e.printStackTrace() }
    		}
    	}
    	result
    }
    	
}