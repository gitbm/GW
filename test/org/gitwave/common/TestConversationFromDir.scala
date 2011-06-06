package org.gitwave.common

import java.io._
import org.mockito._
import org.mockito.Matchers._
import org.junit.Assert._
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.eclipse.jgit.lib.RepositoryBuilder
import org.acme.scalautil._
import org.acme.scalautil.Factory._
import org.acme.scalautil.test.FactoryMockito
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository


object TestConversationFromDir{
	@BeforeClass
	def setUpBeforeClass {
		Factory.implicitFactory = new FactoryMockito
	}
	
}

class TestConversationFromDir {
	import FactoryMockito._
	import Mockito._
	
	private val INIT_ID = 2
	private val INIT_DIR = "/root/foo" + INIT_ID

	@Test
	def test_init_withCreate {
		val dirFile = createMock[File]
		val bodyFile = createMock[File]
		val subjectFile = createMock[File]
		val builder = createMock[RepositoryBuilder]
		when (builder.setWorkTree(any())).thenReturn(builder)
		when (builder.readEnvironment()).thenReturn(builder)

		val repository = createMock[Repository]
		when (builder.build()).thenReturn(repository)


		val git = createMock[Git]
		val properties = createMock[Properties]

		val conversation = new ConversationFromDir
		conversation.init(ConversationInitArgs(INIT_DIR, true, Some(INIT_ID)))
		
		verify(repository).create()
		verify(bodyFile).createNewFile
		verify(subjectFile).createNewFile
		
		assertEquals("id", INIT_ID, conversation.id)
		
		when (bodyFile.isFile).thenReturn(true)
		when (bodyFile.exists).thenReturn(true)
		//val fr = createMock[FileReader]
		val br = createMock[BufferedReader]
		val (s1, s2)  = ("foo", "bar")
		when (br.readLine).thenReturn(s1, s2, null)
		assertEquals("getText", s1 + "\n" + s2, conversation.getText)
	}
}