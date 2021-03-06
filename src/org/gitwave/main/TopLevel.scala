package org.gitwave.main

import com.coconut_palm_software.xscalawt._
import XScalaWTAPI._
import XScalaWT._
import java.io._
import java.util.Date
import org.eclipse.core.runtime.{Status, IStatus, IProgressMonitor}
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.errors.UnsupportedCredentialItem
import org.eclipse.jgit.lib.{ObjectId, RepositoryBuilder}
import org.eclipse.jgit.merge.MergeStrategy
import org.eclipse.jgit.transport.{URIish, CredentialItem, CredentialsProvider, RefSpec}
import org.eclipse.swt._
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets.{ List => SWTList, _ }
import org.gitwave.common.EncryptedPrivateKeySshCredentialsProvider
import org.eclipse.jface.viewers.ListViewer
import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.jface.viewers.ArrayContentProvider
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.Viewer
import org.eclipse.jface.viewers.SelectionChangedEvent
import org.eclipse.jface.viewers.ISelectionChangedListener
import org.eclipse.jface.viewers.IStructuredSelection
import org.gitwave._
import common.Imports._
import Implicits._
import common.Conversations
import common.Conversation
import common.ConversationsFromDisk
import org.gitwave.common.ConversationAlternate
import org.gitwave.common.ConversationsAlternate
import org.eclipse.jface.viewers.StructuredSelection

import org.acme.scalautil.Factory._
import org.acme.scalautil.Properties
import org.acme.scalautil.PropertiesFromFile



class ScalaListContentProvider extends IStructuredContentProvider {
	def getElements(inputElement: Object) = { inputElement.asInstanceOf[ConversationListWrapper].list.asInstanceOf[List[Object]].toArray }
    def inputChanged(viewer : Viewer, oldInput : Object, newInput : Object) { }
    def dispose() { }
}

case class ConversationListWrapper(var list : List[Conversation]) 

class TopLevel (parent : Composite) { // , style : Int) extends Composite(parent, style) { 

	private var text : Text = null
	private var fetchButton : Button = null
//	private var remHead : ObjectId = null
//	private var head : ObjectId = null
	private var theDisplay : Display = null
	private var runtimeProperties = scala.collection.mutable.Map[String, String]()
	private var viewer : ListViewer = _	
	private var currentConv : Option[Conversation] = None

	val PROPERTY_FILE = "propertyFile"
	val DEFAULT_PROPERTY_FILE_NAME = "runtime.properties"
	val WKDIR = "wkdir"
	val BASEDIR = "basedir"
	val REMOTE_ALIAS = "remoteAlias"
	val REPO_NAME = "repoName"
	val FILE_NAME = "fileName"
	val PASSPHRASE = "passphrase"
		
	private val defaultProperties = Map(
		WKDIR -> "gr1",
		BASEDIR -> "/home/user1/workspace-rap",
		REMOTE_ALIAS -> "ghrem",
		REPO_NAME -> "rem",
		FILE_NAME -> "temp3.txt",
		PASSPHRASE -> "OPTIONAL"
		)
	
	def runIt(f : => Unit) = { new Runnable { def run() { f } } }

	def runItJob(f : => Unit) = { new Job("TL Job") {  def run(m: IProgressMonitor) = { f; Status.OK_STATUS } }.schedule() }
	
	def syncExec(f : => Unit) { theDisplay.syncExec(runIt(f)) }

	def asyncExec(f : => Unit) { theDisplay.asyncExec(runIt(f)) }
	
	def threadRun(f : => Unit) { runItJob(f) }
	
	def textStyle(style: Int, setups:(Text => Unit)*)(parent:Composite) = {  
    	setupAndReturn(new Text(parent, SWT.BORDER | style), setups : _*)  
	}
	
	
	def setTextFromFile(fileName : String) = {
		import io._
		val f = new File(fileName)
		val fileText = if (f.isFile && f.exists) Source.fromFile(fileName).getLines.mkString("\n") else ""
		//println ("new text is " + fileText)
		text.setText(fileText)
	}
	
	//def updateHead = { head = repository.resolve("HEAD"); dbprintln(wkdir + ": head = " + head) }
	
	def initProperties = {
		val propFileName = Option(System.getProperty(PROPERTY_FILE)).getOrElse(DEFAULT_PROPERTY_FILE_NAME)
		
		try {
			val pFile = new java.io.FileInputStream(propFileName)
			val jRuntimeProperties = new java.util.Properties
			try {
				jRuntimeProperties.load(pFile)
			}
			finally {
				pFile.close()
			}
			val it = jRuntimeProperties.entrySet().iterator()
			while (it.hasNext()) {
				val e = it.next()
				runtimeProperties += (e.getKey().asInstanceOf[String] -> e.getValue().asInstanceOf[String])
			}
		}
		catch {
			case _ => throw new RuntimeException("No Properties file found.  Hint: use -DpropertyFile (runtime.properties is the default)")
		}
	}
	
	def getProperty(name : String) : String = {
		runtimeProperties.getOrElse(name, defaultProperties.getOrElse(name, 
				throw new RuntimeException("Property value for " + name + " could not be found")))
	}
	
//	def getTextData(conv : Conversation) = { conv.getText }
//	def setTextData(conv : Conversation) { text.setText(getTextData(conv)) }
//	def getTextData(id : Long) = { id + " - ZYYYYYYYYYY" }
//	def getTextData(id : String) : String = { getTextData(getTextId(id)) }
//	def getTextId(id : String) = { try { id.toLong } catch { case _ => 0 } }
//	def setTextData(id : Long) { text.setText(getTextData(id)) }
//	def setTextData(id : String) { setTextData(getTextId(id)) }
	
	def setFocus() = {
		text.setFocus()
	}
	
	def listViewer(setups:(ListViewer => Unit)*)(parent:Composite) = {  
    	setupAndReturn(new ListViewer(parent), setups : _*) 
	}
	
	implicit def funcToISelectionChangedListener[T]( f : (SelectionChangedEvent) => T) : ISelectionChangedListener = {
		new ISelectionChangedListener {
			def selectionChanged(event : SelectionChangedEvent) { f(event) }
		}
	}
	implicit def funcToLabelProvider( f : (Object) => String) : LabelProvider = {
		new LabelProvider {
			override def getText(element : Object) = { f(element) }
		}
	}

	initProperties

	//overrideCommonImplicits
	defineImpl[Properties, PropertiesFromFile]
//	val properties = create[Properties]
//	properties.initialise(None)
//	register(properties)
	defineImpl[Conversations, ConversationsFromDisk]
	val conversations = inject[Conversations]

	theDisplay = Display.getDefault()
	
	val gridData = new GridData()
	gridData.horizontalAlignment = GridData.FILL
	gridData.grabExcessHorizontalSpace = true
	gridData.verticalAlignment = GridData.FILL
	gridData.grabExcessVerticalSpace = true
	gridData.heightHint = 200
		
	// retrieve list of existing conversations
	var convListWrapper = ConversationListWrapper(conversations.convList)
	currentConv = convListWrapper.list match { case h :: t => Some(h); case Nil => None }
	parent.contains (
		_.setLayout(new GridLayout(1, false)),
		label("Conversations"),
		listViewer(
		    viewer = _,
			_.setContentProvider(new ScalaListContentProvider),	
			_.setLabelProvider ({ o: Object => o.asInstanceOf[Conversation].id.toString }),
		    _.setInput(convListWrapper),
		    _.addSelectionChangedListener { (event : SelectionChangedEvent)  =>
		    	currentConv = Some(event.getSelection().asInstanceOf[IStructuredSelection].iterator().next().asInstanceOf[Conversation])
		    	text.setText(currentConv.get.getText)
		    	text.setFocus()
		    }
		),
		button("New Conversation",
			{ e : SelectionEvent =>	
			    text.setText("")
			    val newConv = conversations.createNewConversation
			    currentConv = Some(newConv)
			    convListWrapper.list = conversations.convList
			    viewer.refresh(false)
			    viewer.setSelection(new StructuredSelection(Array[Object](newConv)))
			    text.setFocus()
			}
		),
		group(
		    _.setLayout(new GridLayout(2, false)),
			button("Update",
				_.setLayoutData(new GridData(100, 30)),
				{ event : SelectionEvent =>
					val result = currentConv.get.updateText(text.getText)
					println (result match {
						case Some(x) => "Error: " + x
						case None => "Update Succeeded"
					})

//					val fw = new FileWriter(fullFileName)
//					try{ fw.write(text.getText) } finally { fw.close }
//					try { 
//						git.add().addFilepattern(fileName).call()
//						git.commit().setMessage("Committing stuff " + new Date()).call()
//						updateHead
//						git.push()
//							.setRemote(gitRemoteAlias)
//							.setRefSpecs(new RefSpec("master"))
//							.setCredentialsProvider(cp)
//							.call()
//						dbprintln(wkdir + ": after push")
//					}
//					catch {
//						case e => { dbprintln("Git Exception: " + e); e.printStackTrace() }
//					}
				}
			),
			button("Merge Remote Updates",
				fetchButton = _,
				_.setEnabled(false) //,
//				{ e : SelectionEvent => 
//				  dbprintln(wkdir + ": Merge")
//				  git.merge().setStrategy(MergeStrategy.RESOLVE).include(remHead).call()
//				  updateHead
//				  setTextFromFile(fullFileName)
//				  fetchButton.setEnabled(false)
//				}
		    )
		),
		textStyle(SWT.WRAP | SWT.MULTI,
			_.setLayoutData(gridData),
			text = _,
			_.setText(currentConv.map(_.getText).getOrElse(""))
		)
	)
	if (currentConv.isDefined) {
	    viewer.setSelection(new StructuredSelection(Array[Object](currentConv.get)))
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	val wkdir = getProperty(WKDIR)
//	val gitCommonBase = getProperty(BASEDIR)
//	val gitRepoName = getProperty(REPO_NAME)
//	val gitRemote = gitCommonBase + File.separator + gitRepoName
//	//val gitRemote = "git@github.com:gitbm/rem.git"
//	val gitRemoteAlias = getProperty(REMOTE_ALIAS)
//	val gitDirBase = gitCommonBase + File.separator + wkdir + File.separator + gitRepoName
//	val gitDirRepo = gitDirBase + "/.git"
//	val fileName = getProperty(FILE_NAME)
//	val fullFileName = gitDirBase + File.separator + fileName
//	val builder = new RepositoryBuilder();
//	val gdFile = new File(gitDirRepo)
//	val repository = builder.setGitDir(gdFile)
//		.readEnvironment() // scan environment GIT_* variables
//		.findGitDir() // scan up the file system tree
//		.build();
//	val git = new Git(repository)
//	val cp = new EncryptedPrivateKeySshCredentialsProvider { def getPassphrase() = getProperty(PASSPHRASE) }
//	
//	updateHead
//
//	parent.contains (
//		_.setLayout(new GridLayout(1, false)),
//		//_.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false)),
//		label("@Local  Git Repository = " + gitDirBase),
//		label("@Remote Git Repository = " + gitRemoteAlias),
//		group(
//		    _.setLayout(new GridLayout(2, false)),
//			button("Send",
//				_.setLayoutData(new GridData(100, 30)),
//				{ e : SelectionEvent =>
//					val fw = new FileWriter(fullFileName)
//					try{ fw.write(text.getText) } finally { fw.close }
//					try { 
//						git.add().addFilepattern(fileName).call()
//						git.commit().setMessage("Committing stuff " + new Date()).call()
//						updateHead
//						git.push()
//							.setRemote(gitRemoteAlias)
//							.setRefSpecs(new RefSpec("master"))
//							.setCredentialsProvider(cp)
//							.call()
//						dbprintln(wkdir + ": after push")
//					}
//					catch {
//						case e => { dbprintln("Git Exception: " + e); e.printStackTrace() }
//					}
//				}
//			),
//			button("Merge Remote Updates",
//				fetchButton = _,
//				_.setEnabled(false),
//				{ e : SelectionEvent => 
//				  dbprintln(wkdir + ": Merge")
//				  git.merge().setStrategy(MergeStrategy.RESOLVE).include(remHead).call()
//				  updateHead
//				  setTextFromFile(fullFileName)
//				  fetchButton.setEnabled(false)
//				}
//		    )
//		),
//		textStyle(SWT.WRAP | SWT.MULTI,
//			text = _,
//			_.setLayoutData(gridData))
//
//	)
//	setTextFromFile(fullFileName)
//
//	dbprintln(wkdir + ": After: " + fullFileName)
//	
//	threadRun {
//		while (true) {
////				val remFile = new File(gitRemote)
////				val remRepos = builder.setGitDir(remFile)
////					.readEnvironment() // scan environment GIT_* variables
////					.findGitDir() // scan up the file system tree
////					.build();
//
//			val prevRemHead = remHead
//			git.fetch()
//			.setRemote(gitRemoteAlias)
//			.setCredentialsProvider(cp)
//			.call()
//			remHead = repository.resolve("refs/remotes/" + gitRemoteAlias + "/master");
//			dbprintln(wkdir + ": remHead = " + remHead)
//			if (remHead != prevRemHead && head != remHead) {
//				dbprintln(wkdir + ": change detected - enabling button, head = " + head + ", remHead = " + remHead)
//				asyncExec { fetchButton.setEnabled(true); }
//			}
//			Thread.sleep(5000)
//		}
//	}
}