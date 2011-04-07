package org.gitwave.common

import Imports._
import Implicits._

trait Properties {
	def initialise(initString : Option[String], defaults : Map[String, String]) : Unit
	def get(name : String) : Option[String]
}

object Properties {
	private var theProperties : Properties = _
	private var init = false
	
	def initialise(initString : Option[String] = None, defaults : Map[String, String] = Map[String, String](), overwrite : Boolean = false) = 
		if (!init || overwrite) {
			theProperties = neu[Properties]
			theProperties.initialise(initString, defaults); 
			init = true 
		}
	
	def get(name : String) = { if (!init) { initialise() }; theProperties.get(name) }
	def getOrElse(name : String, elseValue: String) = get(name).getOrElse(elseValue)
}