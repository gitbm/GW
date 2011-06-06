package org.gitwave.common

import java.io._
import java.util.Date
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.{ObjectId, RepositoryBuilder}
import org.eclipse.jgit.transport.{RefSpec}
import Imports._
import org.acme.scalautil.Factory._
import org.acme.scalautil.Properties
import scala.collection.immutable.Queue


import Constants._
import org.eclipse.jgit.lib.Repository

private object Constants {
	val SUBJECT_FILE_NAME = "subject.txt"
	val BODY_FILE_NAME = "body.txt"
	val PROP_PASSPHRASE = "passphrase"
	val	PROP_REMOTE_ALIAS_PREFIX = "remote.alias.prefix"
}

class ConversationFromDir extends Conversation {
		var dirName : String = _
		var bodyFile : File = _
		var subjectFile : File = _
		var initArgs : ConversationInitArgs = _
		var head : ObjectId = _
		var repository : Repository = _
		var git : Git = _
		var cp : EncryptedPrivateKeySshCredentialsProvider = _
		//var remHead : ObjectId = null
		var gitRemoteAlias : String = _ 		

	override def init(args : ConversationInitArgs) = {
		initArgs = args
		dirName = initArgs.initStr;
		val dirFile = create[File, String](dirName)
		bodyFile = create[File, File, String](dirFile, BODY_FILE_NAME)
		subjectFile = create[File, File, String](dirFile, SUBJECT_FILE_NAME)
		val builder = create[RepositoryBuilder].setWorkTree(dirFile).readEnvironment()
		if (!initArgs.create) {
			builder.findGitDir(dirFile)
		}
		repository = builder.build();

		if (initArgs.create) { 
			repository.create()
			bodyFile.createNewFile
			subjectFile.createNewFile
		}

		git = create[Git, Repository](repository)
		val properties = inject[Properties]
		cp = new EncryptedPrivateKeySshCredentialsProvider { def getPassphrase() = properties.get(PROP_PASSPHRASE).getOrElse("") }
		gitRemoteAlias = properties.getMandatory(PROP_REMOTE_ALIAS_PREFIX) + dirFile.getName
		this
	}
	
	private def updateHead = { head = repository.resolve("HEAD"); dbprintln(dirName + ": head = " + head) }

	def id: Long = initArgs.id.get //ConversationsFromDisk.getIdFromSubDirName(dirName)

	def getLines1(f  : File) : List[String] = {
		var (br, more, lines) = (create[BufferedReader, Reader](create[FileReader, File](f)), true, Queue[String]() )
		do { val s = br.readLine; if (s != null) { lines += s } else { more = false } } while (more)		
		lines.toList
	}
	
	def getLines2(f  : File) : List[String] = {
		var br = create[BufferedReader, Reader](create[FileReader, File](f))
		def gl : Stream[String] = { val s = br.readLine ; if (s != null) Stream.cons(s, gl) else Stream.empty }
		gl.toList
	}
	
	def getLines3(f  : File) : List[String] = {
		var br = create[BufferedReader, Reader](create[FileReader, File](f))
		def gl(acc : List[String]) : List[String] = { val s = br.readLine ; if (s == null) acc else gl(s :: acc) }
		gl(Nil).reverse
	}
	
	import scala.collection.immutable.Queue
	def getLines(f  : File) : Queue[String] = {
		var br = create[BufferedReader, Reader](create[FileReader, File](f))
			def gl(acc : Queue[String]) : Queue[String] = { val s = br.readLine ; if (s == null) acc else gl(acc + s) }
		gl(Queue())
	}
	
	def getText(): String = { 
   		import io._
//		if (bodyFile.isFile && bodyFile.exists) Source.fromFile(bodyFile).getLines.mkString("\n") else "No File !"
		if (bodyFile.isFile && bodyFile.exists) getLines(bodyFile).mkString("\n") else "No File !"
    }

    override def updateText(text : String) = {
    	var result : Option[String] = None
    	val fw = create[FileWriter, File](bodyFile)
    	try { 
    		fw.write(text) 
    	}
    	catch {	case e => result = Some(e.getMessage) }
    	finally { fw.close }
  
    	if (result.isEmpty) {
    		try { 
    			git.add().addFilepattern(bodyFile.getName).call()
    			git.commit().setMessage("Committing stuff " + create[Date]).call()
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