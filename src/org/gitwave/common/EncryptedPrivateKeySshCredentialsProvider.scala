package org.gitwave.common

import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.CredentialItem
import org.eclipse.jgit.transport.URIish
import org.eclipse.jgit.errors.UnsupportedCredentialItem

abstract trait EncryptedPrivateKeySshCredentialsProvider extends CredentialsProvider {
	def isInteractive() = true
	
	def supports(items : CredentialItem *) = {
		var supported = true
		for (i <- items) {
			if (!i.isInstanceOf[CredentialItem.StringType] ) {
				supported = false
			}
		}
		supported
	}
	
	def get(uri : URIish, items : CredentialItem *) = {
		var ok = true
		for (i <- items) {
			if (i.isInstanceOf[CredentialItem.StringType] ) {
				i.asInstanceOf[CredentialItem.StringType].setValue(getPassphrase());
			}
			else
				throw new UnsupportedCredentialItem(uri, i.getPromptText());
		}

		ok
	}
	
	def getPassphrase() : String
}
