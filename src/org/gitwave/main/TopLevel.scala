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
import org.eclipse.swt.widgets._
import org.gitwave.common.EncryptedPrivateKeySshCredentialsProvider


class TopLevel (parent : Composite, style : Int) extends Composite(parent, style) { 

	private var text : Text = null
	private var fetchButton : Button = null
	private var remHead : ObjectId = null
	private var head : ObjectId = null
	private var theDisplay : Display = null
	private var runtimeProperties = scala.collection.mutable.Map[String, String]()
	
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
	
	var debug = true;
	def dbg(f : => Unit) { 	if (debug) { f } }
	def dbprintln(s : String) { dbg { println(s) } 	}
	
	def setTextFromFile(fileName : String) = {
		import io._
		val f = new File(fileName)
		val fileText = if (f.isFile && f.exists) Source.fromFile(fileName).getLines.mkString("\n") else ""
		//println ("new text is " + fileText)
		text.setText(fileText)
	}
	
	def updateHead = { head = repository.resolve("HEAD"); dbprintln(wkdir + ": head = " + head) }
	
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
	
	initProperties
	
	theDisplay = Display.getDefault()
	
	val gridData = new GridData()
	gridData.horizontalAlignment = GridData.FILL
	gridData.grabExcessHorizontalSpace = true
	gridData.verticalAlignment = GridData.FILL
	gridData.grabExcessVerticalSpace = true
		
	val wkdir = getProperty(WKDIR)
	val gitCommonBase = getProperty(BASEDIR)
	val gitRepoName = getProperty(REPO_NAME)
	val gitRemote = gitCommonBase + File.separator + gitRepoName
	//val gitRemote = "git@github.com:gitbm/rem.git"
	val gitRemoteAlias = getProperty(REMOTE_ALIAS)
	val gitDirBase = gitCommonBase + File.separator + wkdir + File.separator + gitRepoName
	val gitDirRepo = gitDirBase + "/.git"
	val fileName = getProperty(FILE_NAME)
	val fullFileName = gitDirBase + File.separator + fileName
	val builder = new RepositoryBuilder();
	val gdFile = new File(gitDirRepo)
	val repository = builder.setGitDir(gdFile)
		.readEnvironment() // scan environment GIT_* variables
		.findGitDir() // scan up the file system tree
		.build();
	val git = new Git(repository)
	val cp = new EncryptedPrivateKeySshCredentialsProvider { def getPassphrase() = getProperty(PASSPHRASE) }
	
	updateHead

	parent.contains (
		_.setLayout(new GridLayout(1, false)),
		//_.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false)),
		label("@Local  Git Repository = " + gitDirBase),
		label("@Remote Git Repository = " + gitRemoteAlias),
		group(
		    _.setLayout(new GridLayout(2, false)),
			button("Send",
				_.setLayoutData(new GridData(100, 30)),
				{ e : SelectionEvent =>
					val fw = new FileWriter(fullFileName)
					try{ fw.write(text.getText) } finally { fw.close }
					try { 
						git.add().addFilepattern(fileName).call()
						git.commit().setMessage("Committing stuff " + new Date()).call()
						updateHead
						git.push()
							.setRemote(gitRemoteAlias)
							.setRefSpecs(new RefSpec("master"))
							.setCredentialsProvider(cp)
							.call()
						dbprintln(wkdir + ": after push")
					}
					catch {
						case e => { dbprintln("Git Exception: " + e); e.printStackTrace() }
					}
				}
			),
			button("Merge Remote Updates",
				fetchButton = _,
				_.setEnabled(false),
				{ e : SelectionEvent => 
				  dbprintln(wkdir + ": Merge")
				  git.merge().setStrategy(MergeStrategy.RESOLVE).include(remHead).call()
				  updateHead
				  setTextFromFile(fullFileName)
				  fetchButton.setEnabled(false)
				}
		    )
		),
		textStyle(SWT.WRAP | SWT.MULTI,
			text = _,
			_.setLayoutData(gridData))

	)
	setTextFromFile(fullFileName)

	dbprintln(wkdir + ": After: " + fullFileName)
	
	threadRun {
		while (true) {
//				val remFile = new File(gitRemote)
//				val remRepos = builder.setGitDir(remFile)
//					.readEnvironment() // scan environment GIT_* variables
//					.findGitDir() // scan up the file system tree
//					.build();

			val prevRemHead = remHead
			git.fetch()
			.setRemote(gitRemoteAlias)
			.setCredentialsProvider(cp)
			.call()
			remHead = repository.resolve("refs/remotes/" + gitRemoteAlias + "/master");
			dbprintln(wkdir + ": remHead = " + remHead)
			if (remHead != prevRemHead && head != remHead) {
				dbprintln(wkdir + ": change detected - enabling button, head = " + head + ", remHead = " + remHead)
				asyncExec { fetchButton.setEnabled(true); }
			}
			Thread.sleep(5000)
		}
	}
}